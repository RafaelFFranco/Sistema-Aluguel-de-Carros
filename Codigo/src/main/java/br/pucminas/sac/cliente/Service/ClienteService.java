package br.pucminas.sac.cliente.Service;

import br.pucminas.sac.cliente.model.Cliente;
import br.pucminas.sac.cliente.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}


