package br.pucminas.sac.auth;

import br.pucminas.sac.agente.model.Agente;
import br.pucminas.sac.agente.service.AgenteService;
import br.pucminas.sac.cliente.model.Cliente;
import br.pucminas.sac.cliente.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private AgenteService agenteService;
    
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String senha, 
                       @RequestParam String tipoUsuario,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        
        try {
            if ("CLIENTE".equals(tipoUsuario)) {
                Cliente cliente = clienteService.autenticar(email, senha);
                if (cliente != null) {
                    session.setAttribute("usuarioLogado", cliente);
                    session.setAttribute("tipoUsuario", "CLIENTE");
                    return "redirect:/cliente/dashboard";
                }
            } else if ("AGENTE".equals(tipoUsuario)) {
                Agente agente = agenteService.autenticar(email, senha);
                if (agente != null) {
                    session.setAttribute("usuarioLogado", agente);
                    session.setAttribute("tipoUsuario", "AGENTE");
                    return "redirect:/agente/dashboard";
                }
            }
            
            redirectAttributes.addFlashAttribute("erro", "Credenciais inválidas!");
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro no login: " + e.getMessage());
            return "redirect:/login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "auth/registro";
    }
    
    @PostMapping("/registro")
    public String registrar(@RequestParam String tipoUsuario,
                           @RequestParam String nomeCompleto,
                           @RequestParam String cpf,
                           @RequestParam String rg,
                           @RequestParam String email,
                           @RequestParam String senha,
                           @RequestParam String telefone,
                           @RequestParam String endereco,
                           @RequestParam(required = false) String profissao,
                           @RequestParam(required = false) String dataNascimento,
                           RedirectAttributes redirectAttributes) {
        try {
            if ("CLIENTE".equals(tipoUsuario)) {
                // Verificar se já existe um cliente com esse email
                if (clienteService.existePorEmail(email)) {
                    redirectAttributes.addFlashAttribute("erro", "Já existe um cliente com este email!");
                    return "redirect:/registro";
                }
                
                // Criar cliente
                Cliente cliente = new Cliente();
                cliente.setNomeCompleto(nomeCompleto);
                cliente.setCpf(cpf);
                cliente.setRg(rg);
                cliente.setEmail(email);
                cliente.setSenha(senha);
                cliente.setTelefone(telefone);
                cliente.setEndereco(endereco);
                cliente.setProfissao(profissao != null ? profissao : "");
                
                if (dataNascimento != null && !dataNascimento.isEmpty()) {
                    cliente.setDataNascimento(java.time.LocalDate.parse(dataNascimento));
                }
                
                clienteService.salvar(cliente);
                redirectAttributes.addFlashAttribute("msg", "Cliente registrado com sucesso! Faça o login.");
                
            } else if ("AGENTE".equals(tipoUsuario)) {
                // Verificar se já existe um agente com esse email
                if (agenteService.existePorEmail(email)) {
                    redirectAttributes.addFlashAttribute("erro", "Já existe um agente com este email!");
                    return "redirect:/registro";
                }
                
                // Criar agente
                Agente agente = new Agente();
                agente.setNome(nomeCompleto);
                agente.setCnpj(cpf); // Usando CPF como CNPJ para simplificar
                agente.setEmail(email);
                agente.setSenha(senha);
                agente.setTelefone(telefone);
                agente.setEndereco(endereco);
                agente.setTipo(Agente.TipoAgente.EMPRESA);
                agente.setCapitalSocial(0.0);
                
                agenteService.salvar(agente);
                redirectAttributes.addFlashAttribute("msg", "Agente registrado com sucesso! Faça o login.");
                
            } else {
                redirectAttributes.addFlashAttribute("erro", "Tipo de usuário inválido!");
                return "redirect:/registro";
            }
            
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro no registro: " + e.getMessage());
            return "redirect:/registro";
        }
    }
}