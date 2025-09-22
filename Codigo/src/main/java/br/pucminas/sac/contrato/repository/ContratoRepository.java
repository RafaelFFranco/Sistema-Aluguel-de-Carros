package br.pucminas.sac.contrato.repository;

import br.pucminas.sac.contrato.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    List<Contrato> findByClienteId(Long clienteId);
    
    List<Contrato> findByAutomovelId(Long automovelId);
    
    List<Contrato> findByAgenteId(Long agenteId);
    
    List<Contrato> findByStatus(Contrato.StatusContrato status);
    
    @Query("SELECT c FROM Contrato c WHERE c.dataInicio <= :data AND c.dataFim >= :data")
    List<Contrato> findContratosAtivosNaData(@Param("data") LocalDate data);
    
    @Query("SELECT c FROM Contrato c WHERE c.automovel.id = :automovelId AND c.status = 'ATIVO'")
    List<Contrato> findContratosAtivosPorAutomovel(@Param("automovelId") Long automovelId);
}

