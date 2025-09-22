package br.pucminas.sac.cliente.repository;

import br.pucminas.sac.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByRg(String rg);
}


