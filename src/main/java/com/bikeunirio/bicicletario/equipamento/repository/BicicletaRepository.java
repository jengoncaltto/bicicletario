package com.bikeunirio.bicicletario.equipamento.repository;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {

}
