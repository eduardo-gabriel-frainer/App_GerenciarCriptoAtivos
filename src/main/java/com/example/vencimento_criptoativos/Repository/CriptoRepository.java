package com.example.vencimento_criptoativos.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.vencimento_criptoativos.Model.Cripto;

@Repository
public interface CriptoRepository extends JpaRepository<Cripto, Long> {
    List<Cripto> findByVencimentoBetween(LocalDate inicio, LocalDate fim);
}
