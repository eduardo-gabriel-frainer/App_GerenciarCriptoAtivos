package com.example.vencimento_criptoativos.Services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.vencimento_criptoativos.Model.Cripto;
import com.example.vencimento_criptoativos.Repository.CriptoRepository;

@Service
public class CriptoService {

    @Autowired
    private CriptoRepository criptoRepository;

    public Cripto salvarCripto(Cripto criptoAtivo) {
        return criptoRepository.save(criptoAtivo);
    }

    public List<Cripto> listarTodas() {
        return criptoRepository.findAll();
    }

    public Double calcularValorFuturo(Cripto cripto) {
        try {
            // Como agora são LocalDate, não precisa de parsing
            LocalDate inicio = cripto.getDatainicio();
            LocalDate fim = cripto.getVencimento();
            long dias = ChronoUnit.DAYS.between(inicio, fim);

            // Cálculo do valor futuro usando juros compostos
            Double valorPresente = cripto.getValor();
            Double taxa = cripto.getTaxa() / 100; // Converte a porcentagem para decimal

            // Fórmula do valor futuro: FV = PV * (1 + i)^n
            Double valorFuturo = valorPresente * Math.pow(1 + taxa, dias / 365.0); // Considerando 365 dias por ano

            return valorFuturo;
        } catch (Exception e) {
            return null; // Se algo der errado
        }
    }

    public List<Cripto> getTodosCripto() {
        return criptoRepository.findAll(); // Supondo que você tenha um repositório JPA para Cripto
    }

    public List<Cripto> obterCriptoPorData(LocalDate inicio, LocalDate fim) {
        return criptoRepository.findByVencimentoBetween(inicio, fim); // Exemplo de consulta com JPA
    }

    public void deletarPorId(Long id) {
        criptoRepository.deleteById(id);
    }

    public Cripto buscarPorId(Long id) {
        Optional<Cripto> aplicativo = criptoRepository.findById(id);
        return aplicativo.orElseThrow(() -> new RuntimeException("Cripto não encontrada"));
    }

    public void salvar(Cripto cripto) {
        criptoRepository.save(cripto); // O JPA detecta se é novo ou edição
    }

}