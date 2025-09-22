package br.pucminas.sac.automovel.service;

import br.pucminas.sac.automovel.model.Automovel;
import br.pucminas.sac.automovel.repository.AutomovelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AutomovelService {

    private final AutomovelRepository repository;

    public AutomovelService(AutomovelRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Automovel> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Automovel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Automovel salvar(Automovel automovel) {
        return repository.save(automovel);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Automovel> listarDisponiveis() {
        return repository.findDisponiveis();
    }

    @Transactional(readOnly = true)
    public List<Automovel> listarPorStatus(Automovel.StatusAutomovel status) {
        return repository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Automovel> listarPorProprietario(Long proprietarioId) {
        return repository.findByProprietarioId(proprietarioId);
    }

    public boolean existePorPlaca(String placa) {
        return repository.existsByPlaca(placa);
    }

    public void atualizarStatus(Long id, Automovel.StatusAutomovel status) {
        Optional<Automovel> automovelOpt = repository.findById(id);
        if (automovelOpt.isPresent()) {
            Automovel automovel = automovelOpt.get();
            automovel.setStatus(status);
            repository.save(automovel);
        }
    }
}

