package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import model.Category;
import model.Game;
import model.Word;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

	Optional<Game> findWordById(long id);

	

}
