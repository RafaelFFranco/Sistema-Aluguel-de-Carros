package br.pucminas.sac.pedido;

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
}
