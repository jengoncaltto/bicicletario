package com.bikeunirio.bicicletario.equipamento.repository;

import com.bikeunirio.bicicletario.equipamento.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
