package services.impl;

import expections.AutenticationError;
import expections.RegraNegocioException;
import model.entity.Usuario;
import model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.UsuarioService;

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
            throw new AutenticationError("User not found.");
        }

        if (usuario.get().getSenha().equals(senha)) {
            throw new AutenticationError("Invalid Password.");
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
}
