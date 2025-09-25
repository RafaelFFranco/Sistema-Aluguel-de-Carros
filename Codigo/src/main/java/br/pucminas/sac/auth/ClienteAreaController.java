package br.pucminas.sac.auth;

import br.pucminas.sac.cliente.model.Cliente;
import br.pucminas.sac.cliente.Service.ClienteService;
import br.pucminas.sac.pedido.model.PedidoAluguel;
import br.pucminas.sac.pedido.Service.PedidoAluguelService;
import br.pucminas.sac.automovel.service.AutomovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteAreaController {
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private PedidoAluguelService pedidoService;
    
    @Autowired
    private AutomovelService automovelService;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");
        if (cliente == null || !"CLIENTE".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        
        // Buscar pedidos do cliente
        List<PedidoAluguel> pedidos = pedidoService.listarPorCliente(cliente.getId());
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("totalPedidos", pedidos.size());
        model.addAttribute("pedidosPendentes", pedidos.stream().filter(p -> p.getStatus() == PedidoAluguel.StatusPedido.PENDENTE).count());
        model.addAttribute("pedidosAprovados", pedidos.stream().filter(p -> p.getStatus() == PedidoAluguel.StatusPedido.APROVADO).count());
        
        return "cliente/dashboard";
    }
    
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");
        if (cliente == null || !"CLIENTE".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        
        model.addAttribute("cliente", cliente);
        return "cliente/perfil";
    }
    
    @PostMapping("/perfil")
    public String atualizarPerfil(@ModelAttribute Cliente clienteAtualizado, 
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");
        if (cliente == null || !"CLIENTE".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        
        try {
            // Atualizar apenas campos permitidos
            cliente.setNomeCompleto(clienteAtualizado.getNomeCompleto());
            cliente.setEmail(clienteAtualizado.getEmail());
            cliente.setTelefone(clienteAtualizado.getTelefone());
            cliente.setEndereco(clienteAtualizado.getEndereco());
            cliente.setProfissao(clienteAtualizado.getProfissao());
            cliente.setRendaTotal(clienteAtualizado.getRendaTotal());
            
            clienteService.salvar(cliente);
            session.setAttribute("usuarioLogado", cliente);
            
            redirectAttributes.addFlashAttribute("msg", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar perfil: " + e.getMessage());
        }
        
        return "redirect:/cliente/perfil";
    }
    
    @GetMapping("/pedidos")
    public String meusPedidos(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");
        if (cliente == null || !"CLIENTE".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        
        List<PedidoAluguel> pedidos = pedidoService.listarPorCliente(cliente.getId());
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("cliente", cliente);
        
        return "cliente/pedidos";
    }
    
    @GetMapping("/novo-pedido")
    public String novoPedido(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");
        if (cliente == null || !"CLIENTE".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        
        model.addAttribute("pedido", new PedidoAluguel());
        model.addAttribute("automoveis", automovelService.listarDisponiveis());
        model.addAttribute("cliente", cliente);
        
        return "cliente/novo-pedido";
    }
    
    @PostMapping("/novo-pedido")
    public String criarPedido(@ModelAttribute PedidoAluguel pedido,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");
        if (cliente == null || !"CLIENTE".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        
        try {
            pedido.setCliente(cliente);
            pedidoService.salvar(pedido);
            
            redirectAttributes.addFlashAttribute("msg", "Pedido criado com sucesso!");
            return "redirect:/cliente/pedidos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar pedido: " + e.getMessage());
            return "redirect:/cliente/novo-pedido";
        }
    }
}