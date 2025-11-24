package com.bikeunirio.bicicletario.equipamento.repository;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {

    @Query("SELECT MAX(b.numero) FROM Bicicleta b")
    Integer findMaxNumero();

    Optional<Bicicleta> findByNumero(Integer numero);
}
