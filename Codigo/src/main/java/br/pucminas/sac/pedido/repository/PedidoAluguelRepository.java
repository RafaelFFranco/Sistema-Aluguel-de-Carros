package br.pucminas.sac.pedido.repository;

import br.pucminas.sac.pedido.model.PedidoAluguel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel, Long> {
    List<PedidoAluguel> findByClienteId(Long clienteId);
}
