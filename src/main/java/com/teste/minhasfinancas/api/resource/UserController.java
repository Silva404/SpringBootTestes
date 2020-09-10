package com.teste.minhasfinancas.api.resource;

import com.teste.minhasfinancas.api.dto.UserDTO;
import com.teste.minhasfinancas.expections.AuthenticationError;
import com.teste.minhasfinancas.expections.RegraNegocioException;
import com.teste.minhasfinancas.model.entity.Usuario;
import com.teste.minhasfinancas.services.LancamentoService;
import com.teste.minhasfinancas.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService service;
    private final LancamentoService lancamentoService;

    @PostMapping("/auth")
    public ResponseEntity authUser(@RequestBody UserDTO dto) {
        try {
            Usuario userAuthenticated = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(userAuthenticated);
        } catch (AuthenticationError e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity saveUser(@RequestBody UserDTO dto) {

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();

        try {
            Usuario userSaved = service.salvarUsuario(usuario);
            return new ResponseEntity(userSaved, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity getTotal(@PathVariable("id") Long id) {

        Optional<Usuario> userFound = service.obterPorId(id);
        if (!userFound.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }
}
