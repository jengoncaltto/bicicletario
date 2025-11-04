package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private TokenRepository tokenRepository;
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
}
