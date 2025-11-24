package com.bikeunirio.bicicletario.equipamento.repository;

import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrancaRepository extends JpaRepository<Tranca, Long> {
    @Query("SELECT MAX(t.numero) FROM Tranca t")
    Integer findMaxNumero();

    Optional<Tranca> findByNumero(Integer numero);
}
