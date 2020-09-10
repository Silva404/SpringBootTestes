package com.teste.minhasfinancas.api.resource;

import com.teste.minhasfinancas.api.dto.LancamentoDTO;
import com.teste.minhasfinancas.expections.RegraNegocioException;
import com.teste.minhasfinancas.model.entity.Lancamento;
import com.teste.minhasfinancas.model.entity.Usuario;
import com.teste.minhasfinancas.model.enums.StatusLancamento;
import com.teste.minhasfinancas.model.enums.TipoLancamento;
import com.teste.minhasfinancas.services.LancamentoService;
import com.teste.minhasfinancas.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    private LancamentoService service;

    private UsuarioService usuarioService;

    public LancamentoController(LancamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody LancamentoDTO dto) {
        try {
            Lancamento entity = converter(dto);
            entity = service.save(entity);
            return new ResponseEntity(entity, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
        return service.obterPorId(id).map(entity -> {
            Lancamento lancamento = converter(dto);
            lancamento.setId(entity.getId());
            service.update(lancamento);

            return new ResponseEntity(lancamento, HttpStatus.OK);
        }).orElseGet(() ->
                ResponseEntity.badRequest()
                        .body("Lancamento não encontrado na base de dados."));
    }

    private Lancamento converter(LancamentoDTO dto) {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setMes(dto.getMes());
        lancamento.setAno(dto.getAno());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado pelo o Id informado."));

        lancamento.setUsuario(usuario);
        lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));

        return lancamento;
    }
}
