package com.teste.minhasfinancas.services.impl;

import com.teste.minhasfinancas.expections.AuthenticationError;
import com.teste.minhasfinancas.expections.RegraNegocioException;
import com.teste.minhasfinancas.model.entity.Usuario;
import com.teste.minhasfinancas.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teste.minhasfinancas.services.UsuarioService;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UserRepository repository;

    @Autowired
    public UsuarioServiceImpl(UserRepository usuarioRepositorio) {
        this.repository = usuarioRepositorio;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if (!usuario.isPresent()) {
            throw new AuthenticationError("User not found.");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new AuthenticationError("Invalid Password.");
        }

        return usuario.get();
    }

    @Override
    @Transactional // após salvar o user,vai comitar no DB que salvou
    public Usuario salvarUsuario(Usuario usuario) {
        validateEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validateEmail(String email) {
        boolean exists = repository.existByEmail(email);

        if (exists) {
            throw new RegraNegocioException
                    ("Já existe um usuário cadastrado com este email.");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }

}
