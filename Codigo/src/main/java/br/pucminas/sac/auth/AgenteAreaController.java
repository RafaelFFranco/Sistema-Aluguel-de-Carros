package br.pucminas.sac.auth;

import br.pucminas.sac.agente.model.Agente;
import br.pucminas.sac.agente.service.AgenteService;
import br.pucminas.sac.automovel.model.Automovel;
import br.pucminas.sac.automovel.service.AutomovelService;
import br.pucminas.sac.cliente.model.Cliente;
import br.pucminas.sac.cliente.Service.ClienteService;
import br.pucminas.sac.pedido.model.PedidoAluguel;
import br.pucminas.sac.pedido.Service.PedidoAluguelService;
import br.pucminas.sac.contrato.model.Contrato;
import br.pucminas.sac.contrato.service.ContratoService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/agente")
public class AgenteAreaController {

    @Autowired
    private AgenteService agenteService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private AutomovelService automovelService;
    
    @Autowired
    private PedidoAluguelService pedidoService;
    
    @Autowired
    private ContratoService contratoService;

    /**
     * Método auxiliar para verificar se o agente está logado
     */
    private Agente getAgenteLogado(HttpSession session) {
        Agente agente = (Agente) session.getAttribute("usuarioLogado");
        if (agente == null || !"AGENTE".equals(session.getAttribute("tipoUsuario"))) {
            throw new SecurityException("Acesso não autorizado");
        }
        return agente;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        try {
            Agente agente = getAgenteLogado(session);
            
            // Estatísticas do dashboard
            List<PedidoAluguel> pedidosPendentes = pedidoService.listarPorStatus(PedidoAluguel.StatusPedido.PENDENTE);
            List<Cliente> clientes = clienteService.listarTodos();
            List<Automovel> automoveis = automovelService.listarTodos();
            List<Contrato> contratos = contratoService.listarTodos();
            
            model.addAttribute("agente", agente);
            model.addAttribute("pedidosPendentes", pedidosPendentes.size());
            model.addAttribute("totalClientes", clientes.size());
            model.addAttribute("totalAutomoveis", automoveis.size());
            model.addAttribute("totalContratos", contratos.size());
            model.addAttribute("pedidosRecentes", pedidosPendentes.size() > 5 ? pedidosPendentes.subList(0, 5) : pedidosPendentes);
            
            return "agente/dashboard";
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        try {
            Agente agente = getAgenteLogado(session);
            model.addAttribute("agente", agente);
            return "agente/perfil";
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@ModelAttribute Agente agenteAtualizado, 
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            Agente agente = getAgenteLogado(session);
            
            // Atualizar apenas campos permitidos
            agente.setNome(agenteAtualizado.getNome());
            agente.setEmail(agenteAtualizado.getEmail());
            agente.setTelefone(agenteAtualizado.getTelefone());
            
            agenteService.salvar(agente);
            session.setAttribute("usuarioLogado", agente);
            
            redirectAttributes.addFlashAttribute("msg", "Perfil atualizado com sucesso!");
        } catch (SecurityException e) {
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar perfil: " + e.getMessage());
        }
        
        return "redirect:/agente/perfil";
    }

    @GetMapping("/pedidos")
    public String gerenciarPedidos(HttpSession session, Model model) {
        try {
            Agente agente = getAgenteLogado(session);
            
            List<PedidoAluguel> pedidos = pedidoService.listarTodos();
            model.addAttribute("pedidos", pedidos);
            model.addAttribute("agente", agente);
            
            return "agente/pedidos";
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/pedidos/{id}/aprovar")
    public String aprovarPedido(@PathVariable Long id, 
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            getAgenteLogado(session);
            
            PedidoAluguel pedido = pedidoService.buscarPorId(id).orElse(null);
            if (pedido != null) {
                pedido.setStatus(PedidoAluguel.StatusPedido.APROVADO);
                pedidoService.salvar(pedido);
                redirectAttributes.addFlashAttribute("msg", "Pedido aprovado com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Pedido não encontrado!");
            }
        } catch (SecurityException e) {
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao aprovar pedido: " + e.getMessage());
        }
        
        return "redirect:/agente/pedidos";
    }

    @PostMapping("/pedidos/{id}/rejeitar")
    public String rejeitarPedido(@PathVariable Long id, 
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            getAgenteLogado(session);
            
            PedidoAluguel pedido = pedidoService.buscarPorId(id).orElse(null);
            if (pedido != null) {
                pedido.setStatus(PedidoAluguel.StatusPedido.RECUSADO);
                pedidoService.salvar(pedido);
                redirectAttributes.addFlashAttribute("msg", "Pedido rejeitado!");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Pedido não encontrado!");
            }
        } catch (SecurityException e) {
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao rejeitar pedido: " + e.getMessage());
        }
        
        return "redirect:/agente/pedidos";
    }

    @GetMapping("/clientes")
    public String gerenciarClientes(HttpSession session, Model model) {
        try {
            Agente agente = getAgenteLogado(session);
            
            List<Cliente> clientes = clienteService.listarTodos();
            model.addAttribute("clientes", clientes);
            model.addAttribute("agente", agente);
            
            return "agente/clientes";
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/automoveis")
    public String gerenciarAutomoveis(HttpSession session, Model model) {
        try {
            Agente agente = getAgenteLogado(session);
            
            List<Automovel> automoveis = automovelService.listarTodos();
            model.addAttribute("automoveis", automoveis);
            model.addAttribute("agente", agente);
            
            return "agente/automoveis";
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/contratos")
    public String gerenciarContratos(HttpSession session, Model model) {
        try {
            Agente agente = getAgenteLogado(session);
            
            List<Contrato> contratos = contratoService.listarTodos();
            model.addAttribute("contratos", contratos);
            model.addAttribute("agente", agente);
            
            return "agente/contratos";
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/avaliacoes")
    public String gerenciarAvaliacoes(HttpSession session, Model model) {
        try {
            Agente agente = getAgenteLogado(session);
            
            // Aqui você pode implementar a lógica para listar avaliações
            // Por enquanto, vamos apenas retornar a view
            model.addAttribute("agente", agente);
            
            return "agente/avaliacoes";
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }
}