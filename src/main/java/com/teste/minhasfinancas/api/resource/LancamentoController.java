package com.teste.minhasfinancas.api.resource;

import com.teste.minhasfinancas.api.dto.LancamentoDTO;
import com.teste.minhasfinancas.expections.RegraNegocioException;
import com.teste.minhasfinancas.model.entity.Lancamento;
import com.teste.minhasfinancas.model.entity.Usuario;
import com.teste.minhasfinancas.model.enums.StatusLancamento;
import com.teste.minhasfinancas.model.enums.TipoLancamento;
import com.teste.minhasfinancas.services.LancamentoService;
import com.teste.minhasfinancas.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private  final LancamentoService service;

    private final UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity find(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
    ) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possível fazer a consulta. ");
        } else {
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = service.getByFilter(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
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
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                service.update(lancamento);

                return new ResponseEntity(lancamento, HttpStatus.OK);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
                new ResponseEntity("Lancamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(entity -> {
            service.delete(entity);

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() ->
                new ResponseEntity("Lancamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @PostMapping("{id}")
    public ResponseEntity updateStatus(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
        return service.obterPorId(id).map( entity -> {
            StatusLancamento statusLancamento = StatusLancamento.valueOf(dto.getStatus());

            if (statusLancamento == null) {
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um " +
                        "status válido.");
            }

            try {
                entity.setStatus(statusLancamento);
                service.update(entity);
                return new ResponseEntity(entity, HttpStatus.OK);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
                new ResponseEntity("Lancamento não encontrado na base de dados.",
                        HttpStatus.BAD_REQUEST));
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
