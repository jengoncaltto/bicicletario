package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Bicicleta;
import com.bikeunirio.bicicletario.equipamento.entity.Totem;
import com.bikeunirio.bicicletario.equipamento.service.BicicletaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/totem")
public class TotemController {

    private BicicletaService service;

    public TotemController(BicicletaService service) {}

    @GetMapping
    public List<Bicicleta> listarTotensCadastrados(){return List.of();}

    @PostMapping
    public void CadastrarTotem(@RequestBody Totem totem){}

    @PutMapping("/{idTotem}")
    public void EditarTotem(@PathVariable int idTotem, @RequestBody Totem totem){}

    @DeleteMapping("/{idTotem}")
    public void ExcluirTotem(@PathVariable int idTotem){}

    @GetMapping("/{idTotem}/trancas")
    public void ListarTrancasDeUmTotem(@PathVariable int idTotem, @PathVariable int idTranca){}

    @GetMapping("/{idTotem}/bicicletas")
    public void ListarBicicletasDeUmTotem(@PathVariable int idTotem,  @PathVariable int idBicicleta){}



}
