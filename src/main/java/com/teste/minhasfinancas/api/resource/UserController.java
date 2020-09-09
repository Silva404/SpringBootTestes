package com.teste.minhasfinancas.api.resource;

import com.teste.minhasfinancas.api.dto.UserDTO;
import com.teste.minhasfinancas.expections.AuthenticationError;
import com.teste.minhasfinancas.expections.RegraNegocioException;
import com.teste.minhasfinancas.model.entity.Usuario;
import com.teste.minhasfinancas.services.UsuarioService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UsuarioService service;

    public UserController(@Qualifier("usuarioService") UsuarioService service) {
        this.service = service;
    }

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
}
