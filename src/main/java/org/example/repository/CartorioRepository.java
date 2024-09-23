package org.example.repository;

import org.example.model.Cartorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartorioRepository extends JpaRepository<Cartorio, Integer> {
    Cartorio findById(Long id);
    boolean existsByNome(String nome);
    Page<Cartorio> findAll(Pageable pageable);
    Optional<Object> findByNome(String nome);
    boolean existsById(Long id);
    void deleteById(Long id);
}
