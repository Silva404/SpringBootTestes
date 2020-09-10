package com.teste.minhasfinancas.services.impl;

import com.teste.minhasfinancas.expections.RegraNegocioException;
import com.teste.minhasfinancas.model.entity.Lancamento;
import com.teste.minhasfinancas.model.enums.StatusLancamento;
import com.teste.minhasfinancas.model.enums.TipoLancamento;
import com.teste.minhasfinancas.model.repository.LancamentoRepository;
import com.teste.minhasfinancas.services.LancamentoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository repository;

    public LancamentoServiceImpl(LancamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Lancamento save(Lancamento lancamento) {
        authenticate(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento update(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public void delete(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        repository.delete(lancamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> getByFilter(Lancamento lancamentoFilter) {
        Example example = Example.of(lancamentoFilter,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    public void updateStatus(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        update(lancamento);
    }

    @Override
    public void authenticate(Lancamento lancamento) {

        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma descrição válida.");
        }

        if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
            throw new RegraNegocioException("Informe um mês válido.");
        }

        if (lancamento.getAno() == null || lancamento.getAno().toString().length() < 4) {
            throw new RegraNegocioException("Informe um ano válido.");
        }

        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null ) {
            throw new RegraNegocioException("Informa um usuário.");
        }

        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informa um valor válido.");
        }
    }

    @Override
    public Optional<Lancamento> obterPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long id) {
        BigDecimal receitas = repository.obterSaldoPorTipoDeLancamentoEUsuario(id, TipoLancamento.DESPESA);
        BigDecimal despesas = repository.obterSaldoPorTipoDeLancamentoEUsuario(id, TipoLancamento.DESPESA);

        if (receitas == null) {
            receitas = BigDecimal.ZERO;
        }

        if (despesas == null) {
            despesas = BigDecimal.ZERO;
        }

        return receitas.subtract(despesas);
    }
}
