package com.example.vencimento_criptoativos.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cripto")
public class Cripto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private LocalDate datainicio; // Alterando para LocalDate

    private Double taxa;
    private Double valor;
    private LocalDate vencimento;
    private Double porcentagem;

    private Double valorFuturo;

    // Getters e Setters para o valor futuro
    public Double getValorFuturo() {
        return valorFuturo;
    }

    public void setValorFuturo(Double valorFuturo) {
        this.valorFuturo = valorFuturo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getTaxa() {
        return taxa;
    }

    public void setTaxa(Double taxa) {
        this.taxa = taxa;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    public LocalDate getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(LocalDate datainicio) {
        this.datainicio = datainicio;
    }

    public String getPeriodo() {
        try {
            // Como agora são LocalDate, não precisa de parsing
            long dias = ChronoUnit.DAYS.between(datainicio, vencimento);
            return dias + " dias";
        } catch (Exception e) {
            return "Período inválido";
        }
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

}