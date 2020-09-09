package com.teste.minhasfinancas.services;

import com.teste.minhasfinancas.model.entity.Usuario;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioService {
    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validateEmail(String email);
}
