package br.pucminas.sac.pedido.Service;

import br.pucminas.sac.pedido.model.PedidoAluguel;
import br.pucminas.sac.pedido.repository.PedidoAluguelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoAluguelService {
    private final PedidoAluguelRepository repository;

    public PedidoAluguelService(PedidoAluguelRepository repository) {
        this.repository = repository;
    }

    public PedidoAluguel salvar(PedidoAluguel pedido) {
        return repository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Optional<PedidoAluguel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<PedidoAluguel> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<PedidoAluguel> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PedidoAluguel> listarPorStatus(PedidoAluguel.StatusPedido status) {
        return repository.findAll().stream()
                .filter(p -> p.getStatus() == status)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoAluguel> listarPorAgenteAvaliador(Long agenteId) {
        return repository.findAll().stream()
                .filter(p -> p.getAgenteAvaliador() != null && p.getAgenteAvaliador().getId().equals(agenteId))
                .toList();
    }

    public void cancelarPedido(Long id) {
        Optional<PedidoAluguel> pedidoOpt = repository.findById(id);
        if (pedidoOpt.isPresent()) {
            PedidoAluguel pedido = pedidoOpt.get();
            pedido.setStatus(PedidoAluguel.StatusPedido.RECUSADO);
            repository.save(pedido);
        }
    }

    public void finalizarPedido(Long id) {
        Optional<PedidoAluguel> pedidoOpt = repository.findById(id);
        if (pedidoOpt.isPresent()) {
            PedidoAluguel pedido = pedidoOpt.get();
            pedido.setStatus(PedidoAluguel.StatusPedido.FINALIZADO);
            repository.save(pedido);
        }
    }
}
