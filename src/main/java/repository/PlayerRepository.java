package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

	List<Player> findAllByOrderByTotalWinsDesc(Pageable pageable);

	List<Player> findAllByOrderByTotalWinsDesc();

	Optional<Player> findByUsername(String username);

	@Query("SELECT p.id FROM Player p JOIN GamePlayer gp ON p.id = gp.player.id WHERE gp.game.id = :gameId")
	long findByGameId(@Param("gameId")long id);

}

