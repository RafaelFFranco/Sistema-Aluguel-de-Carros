package br.pucminas.sac.pedido.controller;

import br.pucminas.sac.pedido.Service.PedidoAluguelService;
import br.pucminas.sac.cliente.Service.ClienteService;
import br.pucminas.sac.pedido.model.PedidoAluguel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
public class PedidoAluguelController {
    private final PedidoAluguelService service;
    private final ClienteService clienteService;

    public PedidoAluguelController(PedidoAluguelService service, ClienteService clienteService) {
        this.service = service;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", service.listarTodos());
        return "pedidos/lista";
    }

    @GetMapping("/novo")
    public String novoPedido(Model model) {
        model.addAttribute("pedidoAluguel", new PedidoAluguel());
        return "pedidos/form";
    }

    @PostMapping
    public String criarPedido(@Valid @ModelAttribute("pedidoAluguel") PedidoAluguel pedidoAluguel, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "pedidos/form";
        }
        // Buscar o cliente pelo ID informado no formulário
        if (pedidoAluguel.getCliente() == null || pedidoAluguel.getCliente().getId() == null) {
            result.rejectValue("cliente", null, "Cliente inválido");
            return "pedidos/form";
        }
        var clienteOpt = clienteService.buscarPorId(pedidoAluguel.getCliente().getId());
        if (clienteOpt.isEmpty()) {
            result.rejectValue("cliente", null, "Cliente não encontrado");
            return "pedidos/form";
        }
        pedidoAluguel.setCliente(clienteOpt.get());
        service.salvar(pedidoAluguel);
        redirectAttributes.addFlashAttribute("msg", "Pedido criado com sucesso!");
        return "redirect:/pedidos/status/" + pedidoAluguel.getId();
    }

    @GetMapping("/status/{id}")
    public String visualizarStatus(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PedidoAluguel> pedidoOpt = service.buscarPorId(id);
        if (pedidoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Pedido não encontrado.");
            return "redirect:/clientes";
        }
        model.addAttribute("pedidoAluguel", pedidoOpt.get());
        return "pedidos/status";
    }
}
