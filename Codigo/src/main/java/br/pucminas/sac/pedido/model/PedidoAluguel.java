package br.pucminas.sac.pedido.model;

import br.pucminas.sac.cliente.model.Cliente;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos_aluguel")
public class PedidoAluguel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cliente cliente;

    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private String carro;

    @ManyToOne
    private br.pucminas.sac.automovel.model.Automovel automovel;

    @ManyToOne
    private br.pucminas.sac.agente.model.Agente agenteAvaliador;

    private String parecerFinanceiro;

    private Double scoreFinanceiro;

    private LocalDateTime dataAvaliacao;

    private String observacoesAvaliacao;

    public String getCarro() { return carro; }
    public void setCarro(String carro) { this.carro = carro; }

    public enum StatusPedido {
        PENDENTE, APROVADO, RECUSADO, FINALIZADO
    }

    public PedidoAluguel() {
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public br.pucminas.sac.automovel.model.Automovel getAutomovel() { return automovel; }
    public void setAutomovel(br.pucminas.sac.automovel.model.Automovel automovel) { this.automovel = automovel; }
    public br.pucminas.sac.agente.model.Agente getAgenteAvaliador() { return agenteAvaliador; }
    public void setAgenteAvaliador(br.pucminas.sac.agente.model.Agente agenteAvaliador) { this.agenteAvaliador = agenteAvaliador; }
    public String getParecerFinanceiro() { return parecerFinanceiro; }
    public void setParecerFinanceiro(String parecerFinanceiro) { this.parecerFinanceiro = parecerFinanceiro; }
    public Double getScoreFinanceiro() { return scoreFinanceiro; }
    public void setScoreFinanceiro(Double scoreFinanceiro) { this.scoreFinanceiro = scoreFinanceiro; }
    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDateTime dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
    public String getObservacoesAvaliacao() { return observacoesAvaliacao; }
    public void setObservacoesAvaliacao(String observacoesAvaliacao) { this.observacoesAvaliacao = observacoesAvaliacao; }
}
