package model.repository;

import model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long>  {

    boolean existByEmail(String email);

    Optional<Usuario> findByEmail(String email);

}
