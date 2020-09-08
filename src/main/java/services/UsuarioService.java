package services;

import model.entity.Usuario;
import org.springframework.stereotype.Service;

public interface UsuarioService {
    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validateEmail(String email);
}
