package service;

import expections.RegraNegocioException;
import model.entity.Usuario;
import model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import services.UsuarioService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("tests")
public class UsuarioServiceTeste {

    @Autowired
    UsuarioService service;

    @Autowired
    UserRepository repository;

    //    @Test(expected = Test.None.class) Deveria ser assim
    // porém, nessa versão não tem o test.none
    @Test
    public void shouldValidateEmail() {
        repository.deleteAll();

        service.validateEmail("email@gmail.com");
    }

//    @Test(expected = RegraNegocioException.class)
//    public void shouldThrowErrorWhenValidatingEmailIfEmailIsAlreadyRegister() {
//        Usuario usuario = Usuario.builder()
//                .nome("usuario")
//                .email("email@email.com")
//                .build();
//        repository.save(usuario);
//
//        service.validateEmail("email@email.com");
//    }
//

}
