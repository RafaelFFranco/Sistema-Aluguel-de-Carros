package br.pucminas.sac.teste;

import br.pucminas.sac.cliente.model.Cliente;
import br.pucminas.sac.cliente.Service.ClienteService;
import br.pucminas.sac.agente.model.Agente;
import br.pucminas.sac.agente.service.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private AgenteService agenteService;
    
    @GetMapping("/criar-dados-teste")
    public String criarDadosTeste() {
        try {
            // Criar cliente de teste
            Cliente cliente = new Cliente();
            cliente.setNomeCompleto("Cliente Teste");
            cliente.setCpf("12345678901");
            cliente.setRg("1234567890");
            cliente.setEmail("cliente@teste.com");
            cliente.setSenha("123456789");
            cliente.setTelefone("31999999999");
            cliente.setEndereco("Rua Teste, 123");
            cliente.setProfissao("Desenvolvedor");
            cliente.setDataNascimento(java.time.LocalDate.of(1990, 1, 1));
            
            clienteService.salvar(cliente);
            
            // Criar agente de teste
            Agente agente = new Agente();
            agente.setNome("Agente Teste");
            agente.setCnpj("12345678901234");
            agente.setEmail("agente@teste.com");
            agente.setSenha("123456789");
            agente.setTelefone("31988888888");
            agente.setEndereco("Av. Teste, 456");
            agente.setTipo(Agente.TipoAgente.EMPRESA);
            agente.setCapitalSocial(10000.0);
            
            agenteService.salvar(agente);
            
            return "Dados de teste criados com sucesso!<br>" +
                   "Cliente: cliente@teste.com / senha: 123456789<br>" +
                   "Agente: agente@teste.com / senha: 123456789";
                   
        } catch (Exception e) {
            return "Erro ao criar dados de teste: " + e.getMessage();
        }
    }
    
    @GetMapping("/testar-login-cliente")
    public String testarLoginCliente(@RequestParam String email, @RequestParam String senha) {
        try {
            Cliente cliente = clienteService.autenticar(email, senha);
            if (cliente != null) {
                return "Login de cliente OK! ID: " + cliente.getId() + ", Nome: " + cliente.getNomeCompleto();
            } else {
                return "Credenciais inválidas para cliente!";
            }
        } catch (Exception e) {
            return "Erro no login: " + e.getMessage();
        }
    }
    
    @GetMapping("/testar-login-agente")
    public String testarLoginAgente(@RequestParam String email, @RequestParam String senha) {
        try {
            Agente agente = agenteService.autenticar(email, senha);
            if (agente != null) {
                return "Login de agente OK! ID: " + agente.getId() + ", Nome: " + agente.getNome();
            } else {
                return "Credenciais inválidas para agente!";
            }
        } catch (Exception e) {
            return "Erro no login: " + e.getMessage();
        }
    }
}