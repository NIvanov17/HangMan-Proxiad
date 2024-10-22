package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import model.GamePlayer;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long>{

	@Query("SELECT gp FROM GamePlayer gp WHERE gp.player.id = :id")
	List<GamePlayer> findAllGamesForPlayerWithId(@Param("id") long id);

}
