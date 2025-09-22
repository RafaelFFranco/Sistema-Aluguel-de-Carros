package br.pucminas.sac.contrato.model;

import br.pucminas.sac.cliente.model.Cliente;
import br.pucminas.sac.agente.model.Agente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contratos_credito")
public class ContratoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    private Agente banco; // Banco que concedeu o crédito

    @ManyToOne
    private Contrato contrato; // Contrato de aluguel associado

    @NotNull
    private LocalDate dataInicio;

    @NotNull
    private LocalDate dataVencimento;

    @Positive
    private Double valorPrincipal;

    @Positive
    private Double taxaJuros;

    @Positive
    private Integer numeroParcelas;

    @Positive
    private Double valorParcela;

    @Positive
    private Double valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusContratoCredito status;

    private LocalDateTime dataCriacao;

    private String observacoes;

    public enum StatusContratoCredito {
        PENDENTE, APROVADO, ATIVO, QUITADO, CANCELADO, EM_ATRASO
    }

    public ContratoCredito() {
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusContratoCredito.PENDENTE;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Agente getBanco() { return banco; }
    public void setBanco(Agente banco) { this.banco = banco; }
    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public Double getValorPrincipal() { return valorPrincipal; }
    public void setValorPrincipal(Double valorPrincipal) { this.valorPrincipal = valorPrincipal; }
    public Double getTaxaJuros() { return taxaJuros; }
    public void setTaxaJuros(Double taxaJuros) { this.taxaJuros = taxaJuros; }
    public Integer getNumeroParcelas() { return numeroParcelas; }
    public void setNumeroParcelas(Integer numeroParcelas) { this.numeroParcelas = numeroParcelas; }
    public Double getValorParcela() { return valorParcela; }
    public void setValorParcela(Double valorParcela) { this.valorParcela = valorParcela; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public StatusContratoCredito getStatus() { return status; }
    public void setStatus(StatusContratoCredito status) { this.status = status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}

