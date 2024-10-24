package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

	List<Player> findAllByOrderByTotalWinsDesc(Pageable pageable);

	List<Player> findAllByOrderByTotalWinsDesc();

	Optional<Player> findByUsername(String username);

}
