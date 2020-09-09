package com.teste.minhasfinancas.model.repository;

import com.teste.minhasfinancas.model.entity.Lancamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long> {

}
