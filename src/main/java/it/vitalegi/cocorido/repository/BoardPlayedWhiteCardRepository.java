package it.vitalegi.cocorido.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.BoardPlayedWhiteCard;

public interface BoardPlayedWhiteCardRepository extends CrudRepository<BoardPlayedWhiteCard, Long> {

	List<BoardPlayedWhiteCard> findAllByBoardId(long boardId);

	void deleteByBoardId(long boardId);
}