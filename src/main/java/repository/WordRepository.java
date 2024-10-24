package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

	Optional<Word> findWordByName(String wordToSet);
}
