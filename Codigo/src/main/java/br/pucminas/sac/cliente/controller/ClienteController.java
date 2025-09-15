package br.pucminas.sac.cliente.controller;

import br.pucminas.sac.cliente.model.Cliente;
import br.pucminas.sac.cliente.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", service.listarTodos());
        return "clientes/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return "clientes/form";
        }
        service.salvar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable("id") Long id, Model model) {
        var cliente = service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/detalhe";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        var cliente = service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id, @Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return "clientes/form";
        }
        cliente.setId(id);
        service.salvar(cliente);
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("msg", "Cliente excluído com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível excluir o cliente.");
        }
        return "redirect:/clientes";
    }
}


