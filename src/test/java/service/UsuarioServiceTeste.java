package service;

import com.teste.minhasfinancas.expections.RegraNegocioException;
import com.teste.minhasfinancas.model.entity.Usuario;
import com.teste.minhasfinancas.model.repository.UserRepository;
import com.teste.minhasfinancas.services.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("tests")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioServiceTeste {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UserRepository repository;

    public static Usuario createUser() {
        return Usuario.builder()
                .id(1)
                .nome("usuario")
                .email("usuario@mail.com")
                .senha("senha")
                .build();
    }

    @Test
    public void shoullSaveUser() {
        Mockito.doNothing().when(service).validateEmail(Mockito.anyString());
        Usuario usuario = createUser();
        Mockito.when(repository.save(Mockito.any(Usuario.class)))
                .thenReturn(usuario);

        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertEquals(usuarioSalvo.getId(), 1);
        Assertions.assertEquals(usuarioSalvo.getNome(), "usuario");
        Assertions.assertEquals(usuarioSalvo.getEmail(), "email@email.com");
        Assertions.assertEquals(usuarioSalvo.getSenha(), "senha");


    }

    @Test
    public void shouldAuthenticateUser() {
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = createUser();

        Mockito.when(repository.findByEmail(email))
                .thenReturn(Optional.of(usuario));

        Usuario result = service.autenticar(email, senha);

        Assertions.assertNotNull(result);
    }

    @Test // expected exception
    public void shouldnSaveUserAlreadyRegistered() {
        String email = "email@email.com";

        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class)
                .when(service).validateEmail(email);

        service.salvarUsuario(usuario);
        Mockito.verify(repository, Mockito.never()).save(usuario);
    }

    @Test
    public void shouldThrowErrorIfPasswordDontMatch() {
        Usuario usuario = createUser();

        Mockito.when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(usuario));


//        Throwable exception = Assertions
//                .assertThrows(() -> service.autenticar
//                        ("email@email.com", "123"));
//
//        Assertions.assertTrue(exception.getMessage().contains("Stuff"));
    }

    @Test
    public void shouldValidateEmail() {
        Mockito.when(repository.existByEmail(Mockito.anyString()))
                .thenReturn(false);

        service.validateEmail("email@gmail.com");
    }

    @Test
    public void shouldThrowErrorWhenValidatingEmailIfEmailIsAlreadyRegister() {
        Mockito.when(repository.existByEmail(Mockito.anyString()))
                .thenReturn(true);

        service.validateEmail("email@email.com");
    }
}

