package com.teste.minhasfinancas.repository;

import com.teste.minhasfinancas.model.entity.Usuario;
import com.teste.minhasfinancas.model.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("tests")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static Usuario createUser() {
        return Usuario.builder()
                .nome("usuario")
                .email("usuario@mail.com")
                .senha("senha")
                .build();
    }

    @Test
    public void verifyIfEmailExists() {
        Usuario usuario = createUser();
        entityManager.persist(usuario);

        boolean result = repository.existByEmail("usuario@mail.com");

        Assertions.assertTrue(result);
    }

    @Test
    public void returnFalseIfEmailIsNotRegisterd() {
        boolean result = repository.existByEmail("usuario@mail.com");

        Assertions.assertFalse(result);
    }

    @Test
    public void shouldPersistUserInDataBase() {
        Usuario user = createUser();

        Usuario userSaved = repository.save(user);

        Assertions.assertNotNull(userSaved.getId());
    }

    @Test
    public void shouldFindUserByEmail() {
        Usuario user = createUser();
        entityManager.persist(user);

        Optional<Usuario> result = repository
                .findByEmail("usuario@mail.com");

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void shouldFindUserEmailThatDoenstExist() {
        Optional<Usuario> result = repository
                .findByEmail("usuario@mail.com");

        Assertions.assertFalse(result.isPresent());
    }
}
