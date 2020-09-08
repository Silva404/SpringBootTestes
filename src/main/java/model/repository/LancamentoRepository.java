package model.repository;

import model.entity.Lancamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long> {

}
