package com.bikeunirio.bicicletario.equipamento.service;

import com.bikeunirio.bicicletario.equipamento.repository.TotemRepository;
import org.springframework.stereotype.Service;

@Service
public class TotemService {

    private TotemRepository totemRepository;
    public TotemService(TotemRepository totemRepository) {
        this.totemRepository = totemRepository;
    }
}
