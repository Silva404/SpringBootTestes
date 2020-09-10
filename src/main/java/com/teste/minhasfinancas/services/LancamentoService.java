package com.teste.minhasfinancas.services;

import com.teste.minhasfinancas.model.entity.Lancamento;
import com.teste.minhasfinancas.model.enums.StatusLancamento;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    List<Lancamento> getByFilter(Lancamento lancamentoFilter);

    Lancamento save(Lancamento lancamento);

    Lancamento update(Lancamento lancamento);

    void delete(Lancamento lancamento);

    void updateStatus(Lancamento lancamento, StatusLancamento status);

    void authenticate(Lancamento lancamento);

    Optional<Lancamento> obterPorId(Long id);
}
