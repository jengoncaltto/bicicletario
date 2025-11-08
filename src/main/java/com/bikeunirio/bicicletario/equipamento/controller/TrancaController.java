package com.bikeunirio.bicicletario.equipamento.controller;

import com.bikeunirio.bicicletario.equipamento.entity.Tranca;
import com.bikeunirio.bicicletario.equipamento.repository.TrancaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tranca")
public class TrancaController {

    private TrancaRepository trancaRepository;
    public TrancaController(TrancaRepository trancaRepository) {}

    @GetMapping
    public List<Tranca> listarTrancasCadastradas(){
        return trancaRepository.findAll();
    }

    @PostMapping
    public void cadastrarTranca(@RequestBody Tranca tranca){}

    @GetMapping("/{idTranca")
    public void buscarTrancaPorId(@PathVariable int idTranca){}

    @PutMapping("/{idTranca}")
    public void EditarTranca(@PathVariable int idTranca){}

    @DeleteMapping("/{idTranca}")
    public void excluirTranca(@PathVariable int idTranca){}

    @GetMapping("/{idTranca}/bicicleta")
    public void retornarBicicletaNaTranca(@PathVariable int idTranca, @PathVariable int idBicicleta){}

    @PostMapping("/{idTranca}/trancar")
    public void trancar(@PathVariable int idTranca){}

    @PostMapping("/{idTRanca}/destrancar")
    public void destrancar(@PathVariable int idTranca){}

    @PostMapping("/{idTranca}/status/{acao}")
    public void alterarStatusDaTranca(@PathVariable int idTranca){}

    @PostMapping("/integrarNaRede")
    public void integrarTrancaNaRede(@PathVariable int idTranca){}

    @PostMapping("/retirarDaRede")
    public void retirarTrancaDaRede(@PathVariable int idTranca){}



}
