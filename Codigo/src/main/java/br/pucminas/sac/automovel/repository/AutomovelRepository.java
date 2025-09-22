package br.pucminas.sac.automovel.repository;

import br.pucminas.sac.automovel.model.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {
    boolean existsByPlaca(String placa);
    
    List<Automovel> findByStatus(Automovel.StatusAutomovel status);
    
    List<Automovel> findByTipoPropriedade(Automovel.TipoPropriedade tipoPropriedade);
    
    @Query("SELECT a FROM Automovel a WHERE a.status = 'DISPONIVEL'")
    List<Automovel> findDisponiveis();
    
    @Query("SELECT a FROM Automovel a WHERE a.proprietarioId = :proprietarioId")
    List<Automovel> findByProprietarioId(@Param("proprietarioId") Long proprietarioId);
}

