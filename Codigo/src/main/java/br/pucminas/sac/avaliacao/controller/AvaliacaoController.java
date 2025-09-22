package br.pucminas.sac.avaliacao.controller;

import br.pucminas.sac.pedido.Service.PedidoAluguelService;
import br.pucminas.sac.agente.service.AgenteService;
import br.pucminas.sac.pedido.model.PedidoAluguel;
import br.pucminas.sac.agente.model.Agente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/avaliacoes")
public class AvaliacaoController {
    
    private final PedidoAluguelService pedidoService;
    private final AgenteService agenteService;
    
    public AvaliacaoController(PedidoAluguelService pedidoService, AgenteService agenteService) {
        this.pedidoService = pedidoService;
        this.agenteService = agenteService;
    }
    
    @GetMapping
    public String listarPendentes(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPorStatus(PedidoAluguel.StatusPedido.PENDENTE));
        return "avaliacoes/lista";
    }
    
    @GetMapping("/{id}")
    public String formAvaliacao(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PedidoAluguel> pedidoOpt = pedidoService.buscarPorId(id);
        if (pedidoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Pedido não encontrado.");
            return "redirect:/avaliacoes";
        }
        
        PedidoAluguel pedido = pedidoOpt.get();
        if (pedido.getStatus() != PedidoAluguel.StatusPedido.PENDENTE) {
            redirectAttributes.addFlashAttribute("erro", "Este pedido já foi avaliado.");
            return "redirect:/avaliacoes";
        }
        
        // Calcular score baseado na renda do cliente
        double scoreCalculado = calcularScoreFinanceiro(pedido.getCliente().getRendaTotal());
        
        model.addAttribute("pedido", pedido);
        model.addAttribute("agentes", agenteService.listarTodos());
        model.addAttribute("scoreCalculado", scoreCalculado);
        
        return "avaliacoes/form";
    }
    
    @PostMapping("/{id}")
    public String processarAvaliacao(@PathVariable("id") Long id,
                                   @RequestParam("agenteId") Long agenteId,
                                   @RequestParam("score") Double score,
                                   @RequestParam("parecer") String parecer,
                                   @RequestParam(value = "observacoes", required = false) String observacoes,
                                   RedirectAttributes redirectAttributes) {
        try {
            Optional<PedidoAluguel> pedidoOpt = pedidoService.buscarPorId(id);
            if (pedidoOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Pedido não encontrado.");
                return "redirect:/avaliacoes";
            }
            
            Optional<Agente> agenteOpt = agenteService.buscarPorId(agenteId);
            if (agenteOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Agente não encontrado.");
                return "redirect:/avaliacoes/" + id;
            }
            
            PedidoAluguel pedido = pedidoOpt.get();
            Agente agente = agenteOpt.get();
            
            // Atualizar o pedido com a avaliação
            pedido.setAgenteAvaliador(agente);
            pedido.setScoreFinanceiro(score);
            pedido.setParecerFinanceiro(parecer);
            pedido.setObservacoesAvaliacao(observacoes);
            pedido.setDataAvaliacao(LocalDateTime.now());
            
            // Definir status baseado no parecer
            if ("APROVADO".equals(parecer)) {
                pedido.setStatus(PedidoAluguel.StatusPedido.APROVADO);
            } else if ("REPROVADO".equals(parecer)) {
                pedido.setStatus(PedidoAluguel.StatusPedido.RECUSADO);
            }
            // Para outros pareceres, mantém como PENDENTE
            
            pedidoService.salvar(pedido);
            redirectAttributes.addFlashAttribute("msg", "Avaliação processada com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar avaliação: " + e.getMessage());
        }
        
        return "redirect:/avaliacoes";
    }
    
    private double calcularScoreFinanceiro(Double rendaTotal) {
        if (rendaTotal == null || rendaTotal <= 0) {
            return 0.0;
        }
        
        // Lógica simples de cálculo de score baseado na renda
        if (rendaTotal >= 10000) {
            return 9.0;
        } else if (rendaTotal >= 5000) {
            return 7.0;
        } else if (rendaTotal >= 3000) {
            return 5.0;
        } else if (rendaTotal >= 1500) {
            return 3.0;
        } else {
            return 1.0;
        }
    }
}
