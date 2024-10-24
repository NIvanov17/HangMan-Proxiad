package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

	Optional<Game> findWordById(long id);

	@Query("SELECT g FROM Game g JOIN g.word w GROUP BY g.word ORDER BY COUNT(w.id) DESC")
	List<Game> findTopTenMostUsedGames(Pageable pageable);

}
