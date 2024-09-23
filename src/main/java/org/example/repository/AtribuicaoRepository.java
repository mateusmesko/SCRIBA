package org.example.repository;

import org.example.model.Atribuicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtribuicaoRepository extends JpaRepository<Atribuicao, String> {
    Optional<Atribuicao> findByCodigo(Long codigo);
}
