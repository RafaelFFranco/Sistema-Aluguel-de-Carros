package br.pucminas.sac.agente.controller;

import br.pucminas.sac.agente.model.Agente;
import br.pucminas.sac.agente.service.AgenteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/agentes")
public class AgenteController {

    private final AgenteService service;

    public AgenteController(AgenteService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("agentes", service.listarTodos());
        return "agentes/lista";
    }

    @GetMapping("/bancos")
    public String listarBancos(Model model) {
        model.addAttribute("agentes", service.listarBancosAtivos());
        model.addAttribute("titulo", "Bancos");
        return "agentes/lista";
    }

    @GetMapping("/empresas")
    public String listarEmpresas(Model model) {
        model.addAttribute("agentes", service.listarEmpresasAtivas());
        model.addAttribute("titulo", "Empresas");
        return "agentes/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("agente", new Agente());
        return "agentes/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("agente") Agente agente, BindingResult result) {
        if (result.hasErrors()) {
            return "agentes/form";
        }
        if (service.existePorCnpj(agente.getCnpj())) {
            result.rejectValue("cnpj", null, "CNPJ já cadastrado");
            return "agentes/form";
        }
        service.salvar(agente);
        return "redirect:/agentes";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable("id") Long id, Model model) {
        var agente = service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Agente não encontrado"));
        model.addAttribute("agente", agente);
        return "agentes/detalhe";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        var agente = service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Agente não encontrado"));
        model.addAttribute("agente", agente);
        return "agentes/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id, @Valid @ModelAttribute("agente") Agente agente, BindingResult result) {
        if (result.hasErrors()) {
            return "agentes/form";
        }
        
        // Verificar se o CNPJ já existe em outro agente
        Optional<Agente> agenteExistente = service.buscarPorId(id);
        if (agenteExistente.isPresent()) {
            Agente existente = agenteExistente.get();
            // Se o CNPJ mudou, verificar se já existe
            if (!existente.getCnpj().equals(agente.getCnpj()) && service.existePorCnpj(agente.getCnpj())) {
                result.rejectValue("cnpj", null, "CNPJ já cadastrado");
                return "agentes/form";
            }
        }
        
        agente.setId(id);
        service.salvar(agente);
        return "redirect:/agentes";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("msg", "Agente excluído com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível excluir o agente.");
        }
        return "redirect:/agentes";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable("id") Long id, @RequestParam("status") Agente.StatusAgente status, RedirectAttributes redirectAttributes) {
        service.atualizarStatus(id, status);
        redirectAttributes.addFlashAttribute("msg", "Status atualizado com sucesso.");
        return "redirect:/agentes";
    }
}

