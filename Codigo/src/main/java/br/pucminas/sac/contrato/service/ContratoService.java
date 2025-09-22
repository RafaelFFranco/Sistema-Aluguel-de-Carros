package br.pucminas.sac.contrato.service;

import br.pucminas.sac.contrato.model.Contrato;
import br.pucminas.sac.contrato.repository.ContratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContratoService {

    private final ContratoRepository repository;

    public ContratoService(ContratoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Contrato> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Contrato salvar(Contrato contrato) {
        return repository.save(contrato);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarPorAutomovel(Long automovelId) {
        return repository.findByAutomovelId(automovelId);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosAtivosNaData(LocalDate data) {
        return repository.findContratosAtivosNaData(data);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosAtivosPorAutomovel(Long automovelId) {
        return repository.findContratosAtivosPorAutomovel(automovelId);
    }

    public void atualizarStatus(Long id, Contrato.StatusContrato status) {
        Optional<Contrato> contratoOpt = repository.findById(id);
        if (contratoOpt.isPresent()) {
            Contrato contrato = contratoOpt.get();
            contrato.setStatus(status);
            repository.save(contrato);
        }
    }

    public boolean verificarDisponibilidadeAutomovel(Long automovelId, LocalDate dataInicio, LocalDate dataFim) {
        List<Contrato> contratosAtivos = repository.findContratosAtivosPorAutomovel(automovelId);
        
        return contratosAtivos.stream().noneMatch(contrato -> 
            !(dataFim.isBefore(contrato.getDataInicio()) || dataInicio.isAfter(contrato.getDataFim()))
        );
    }
}

