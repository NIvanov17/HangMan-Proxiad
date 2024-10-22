package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
//	public Word getRandomGame() {
//		Word word = gamesList.get(RANDOM.nextInt(gamesList.size()));
//		return word;
//	}
}
