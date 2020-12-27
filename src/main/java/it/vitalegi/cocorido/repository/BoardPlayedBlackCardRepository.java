package it.vitalegi.cocorido.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.model.BoardPlayedBlackCard;

public interface BoardPlayedBlackCardRepository extends CrudRepository<BoardPlayedBlackCard, Long> {

	List<BoardPlayedBlackCard> findAllByBoardId(long boardId);

	void deleteByBoardId(long boardId);
}