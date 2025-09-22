package br.pucminas.sac.contrato.service;

import br.pucminas.sac.contrato.model.ContratoCredito;
import br.pucminas.sac.contrato.repository.ContratoCreditoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContratoCreditoService {

    private final ContratoCreditoRepository repository;

    public ContratoCreditoService(ContratoCreditoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ContratoCredito> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ContratoCredito> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public ContratoCredito salvar(ContratoCredito contratoCredito) {
        return repository.save(contratoCredito);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ContratoCredito> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<ContratoCredito> listarPorBanco(Long bancoId) {
        return repository.findByBancoId(bancoId);
    }

    @Transactional(readOnly = true)
    public List<ContratoCredito> listarContratosVencidos() {
        return repository.findContratosVencidos(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<ContratoCredito> listarContratosAtivosPorCliente(Long clienteId) {
        return repository.findContratosAtivosPorCliente(clienteId);
    }

    public void atualizarStatus(Long id, ContratoCredito.StatusContratoCredito status) {
        Optional<ContratoCredito> contratoOpt = repository.findById(id);
        if (contratoOpt.isPresent()) {
            ContratoCredito contrato = contratoOpt.get();
            contrato.setStatus(status);
            repository.save(contrato);
        }
    }

    public void marcarComoVencido(Long id) {
        atualizarStatus(id, ContratoCredito.StatusContratoCredito.EM_ATRASO);
    }
}

