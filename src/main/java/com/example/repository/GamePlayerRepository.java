package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.enums.RoleName;
import com.example.model.GamePlayer;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long>{

	@Query("SELECT gp FROM GamePlayer gp WHERE gp.player.id = :id AND gp.role = :role")
	Page<GamePlayer> findAllGamesForPlayerWithId(@Param("id") long id, @Param("role")RoleName guesser,Pageable pageable);
	
	@Query("SELECT gp FROM GamePlayer gp WHERE gp.player.id = :id AND gp.role = :role")
	List<GamePlayer> findAllGamesForPlayerWithId(@Param("id") long id, @Param("role")RoleName guesser);

	void deleteByGameId(long id);

}
