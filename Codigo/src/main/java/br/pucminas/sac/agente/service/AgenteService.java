package br.pucminas.sac.agente.service;

import br.pucminas.sac.agente.model.Agente;
import br.pucminas.sac.agente.repository.AgenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AgenteService {

    private final AgenteRepository repository;

    public AgenteService(AgenteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Agente> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Agente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Agente salvar(Agente agente) {
        return repository.save(agente);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Agente> listarBancosAtivos() {
        return repository.findBancosAtivos();
    }

    @Transactional(readOnly = true)
    public List<Agente> listarEmpresasAtivas() {
        return repository.findEmpresasAtivas();
    }

    @Transactional(readOnly = true)
    public List<Agente> listarPorTipo(Agente.TipoAgente tipo) {
        return repository.findByTipo(tipo);
    }

    public boolean existePorCnpj(String cnpj) {
        return repository.existsByCnpj(cnpj);
    }

    public void atualizarStatus(Long id, Agente.StatusAgente status) {
        Optional<Agente> agenteOpt = repository.findById(id);
        if (agenteOpt.isPresent()) {
            Agente agente = agenteOpt.get();
            agente.setStatus(status);
            repository.save(agente);
        }
    }
    
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }
    
    public Agente autenticar(String email, String senha) {
        Optional<Agente> agente = repository.findByEmail(email);
        if (agente.isPresent()) {
            Agente a = agente.get();
            // Verificar se o campo senha existe e foi preenchido
            if (a.getSenha() != null && a.getSenha().equals(senha)) {
                return a;
            }
            // Fallback para CNPJ caso seja um agente antigo sem senha
            else if (a.getSenha() == null && a.getCnpj().equals(senha)) {
                return a;
            }
        }
        return null;
    }
}

