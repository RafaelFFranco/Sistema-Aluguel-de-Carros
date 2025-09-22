package br.pucminas.sac.agente.repository;

import br.pucminas.sac.agente.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
    boolean existsByCnpj(String cnpj);
    
    List<Agente> findByTipo(Agente.TipoAgente tipo);
    
    List<Agente> findByStatus(Agente.StatusAgente status);
    
    @Query("SELECT a FROM Agente a WHERE a.tipo = 'BANCO' AND a.status = 'ATIVO'")
    List<Agente> findBancosAtivos();
    
    @Query("SELECT a FROM Agente a WHERE a.tipo = 'EMPRESA' AND a.status = 'ATIVO'")
    List<Agente> findEmpresasAtivas();
}

