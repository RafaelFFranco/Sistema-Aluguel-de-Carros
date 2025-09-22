package br.pucminas.sac.contrato.repository;

import br.pucminas.sac.contrato.model.ContratoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ContratoCreditoRepository extends JpaRepository<ContratoCredito, Long> {
    List<ContratoCredito> findByClienteId(Long clienteId);
    
    List<ContratoCredito> findByBancoId(Long bancoId);
    
    List<ContratoCredito> findByContratoId(Long contratoId);
    
    List<ContratoCredito> findByStatus(ContratoCredito.StatusContratoCredito status);
    
    @Query("SELECT cc FROM ContratoCredito cc WHERE cc.dataVencimento < :data AND cc.status = 'ATIVO'")
    List<ContratoCredito> findContratosVencidos(@Param("data") LocalDate data);
    
    @Query("SELECT cc FROM ContratoCredito cc WHERE cc.cliente.id = :clienteId AND cc.status IN ('ATIVO', 'EM_ATRASO')")
    List<ContratoCredito> findContratosAtivosPorCliente(@Param("clienteId") Long clienteId);
}

