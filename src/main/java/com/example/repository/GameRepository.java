package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

	Optional<Game> findById(long id);
	
	@Query("SELECT g.word.id FROM Game g WHERE g.isFinished = true GROUP BY g.word.id ORDER BY COUNT(g) DESC")
	List<Long> findTopUsedWordIds(Pageable pageable);
	
	@Query("SELECT w.name FROM Game g JOIN g.word w WHERE g.isFinished = true GROUP BY w.name ORDER BY COUNT(g) DESC")
	List<String> getTopTenGames(Pageable pageable);

	List<Game> findByWordId(Long id);

	List<Game> findByIsFinishedTrue();

	Optional<Game> findByToken(String token);


	

}
