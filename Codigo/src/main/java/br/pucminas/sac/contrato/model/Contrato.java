package br.pucminas.sac.contrato.model;

import br.pucminas.sac.cliente.model.Cliente;
import br.pucminas.sac.automovel.model.Automovel;
import br.pucminas.sac.agente.model.Agente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    private Automovel automovel;

    @ManyToOne
    private Agente agente; // Agente responsável pelo contrato

    @NotNull
    private LocalDate dataInicio;

    @NotNull
    private LocalDate dataFim;

    @Positive
    private Double valorDiario;

    @Positive
    private Double valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusContrato status;

    @Enumerated(EnumType.STRING)
    private TipoContrato tipo;

    private LocalDateTime dataCriacao;

    private String observacoes;

    public enum StatusContrato {
        PENDENTE, ATIVO, FINALIZADO, CANCELADO, SUSPENSO
    }

    public enum TipoContrato {
        ALUGUEL_SIMPLES, ALUGUEL_COM_FINANCIAMENTO, COMPRA_FINANCIADA
    }

    public Contrato() {
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusContrato.PENDENTE;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Automovel getAutomovel() { return automovel; }
    public void setAutomovel(Automovel automovel) { this.automovel = automovel; }
    public Agente getAgente() { return agente; }
    public void setAgente(Agente agente) { this.agente = agente; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public Double getValorDiario() { return valorDiario; }
    public void setValorDiario(Double valorDiario) { this.valorDiario = valorDiario; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public StatusContrato getStatus() { return status; }
    public void setStatus(StatusContrato status) { this.status = status; }
    public TipoContrato getTipo() { return tipo; }
    public void setTipo(TipoContrato tipo) { this.tipo = tipo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}

