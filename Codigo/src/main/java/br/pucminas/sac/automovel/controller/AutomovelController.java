package br.pucminas.sac.automovel.controller;

import br.pucminas.sac.automovel.model.Automovel;
import br.pucminas.sac.automovel.service.AutomovelService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/automoveis")
public class AutomovelController {

    private final AutomovelService service;

    public AutomovelController(AutomovelService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("automoveis", service.listarTodos());
        return "automoveis/lista";
    }

    @GetMapping("/disponiveis")
    public String listarDisponiveis(Model model) {
        model.addAttribute("automoveis", service.listarDisponiveis());
        return "automoveis/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("automovel", new Automovel());
        return "automoveis/form";
    }

    @GetMapping("/form")
    public String form(Model model) {
        // Redirect para /novo para compatibilidade
        return "redirect:/automoveis/novo";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("automovel") Automovel automovel, BindingResult result) {
        if (result.hasErrors()) {
            return "automoveis/form";
        }
        if (service.existePorPlaca(automovel.getPlaca())) {
            result.rejectValue("placa", null, "Placa já cadastrada");
            return "automoveis/form";
        }
        service.salvar(automovel);
        return "redirect:/automoveis";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable("id") Long id, Model model) {
        var automovel = service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Automóvel não encontrado"));
        model.addAttribute("automovel", automovel);
        return "automoveis/detalhe";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        var automovel = service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Automóvel não encontrado"));
        model.addAttribute("automovel", automovel);
        return "automoveis/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id, @Valid @ModelAttribute("automovel") Automovel automovel, BindingResult result) {
        if (result.hasErrors()) {
            return "automoveis/form";
        }
        
        // Verificar se a placa já existe em outro automóvel
        Optional<Automovel> automovelExistente = service.buscarPorId(id);
        if (automovelExistente.isPresent()) {
            Automovel existente = automovelExistente.get();
            // Se a placa mudou, verificar se já existe
            if (!existente.getPlaca().equals(automovel.getPlaca()) && service.existePorPlaca(automovel.getPlaca())) {
                result.rejectValue("placa", null, "Placa já cadastrada");
                return "automoveis/form";
            }
        }
        
        automovel.setId(id);
        service.salvar(automovel);
        return "redirect:/automoveis";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("msg", "Automóvel excluído com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível excluir o automóvel.");
        }
        return "redirect:/automoveis";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable("id") Long id, @RequestParam("status") Automovel.StatusAutomovel status, RedirectAttributes redirectAttributes) {
        service.atualizarStatus(id, status);
        redirectAttributes.addFlashAttribute("msg", "Status atualizado com sucesso.");
        return "redirect:/automoveis";
    }
}

