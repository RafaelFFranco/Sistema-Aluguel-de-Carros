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
}
