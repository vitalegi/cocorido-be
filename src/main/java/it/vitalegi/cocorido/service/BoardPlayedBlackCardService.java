package it.vitalegi.cocorido.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.BoardPlayedBlackCard;
import it.vitalegi.cocorido.repository.BoardPlayedBlackCardRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardPlayedBlackCardService {

	@Autowired
	BoardPlayedBlackCardRepository repository;

	@Transactional
	public void addCard(long boardId, long blackCardId) {
		BoardPlayedBlackCard entity = new BoardPlayedBlackCard();
		entity.setBoardId(boardId);
		entity.setBlackCardId(blackCardId);
		log.info("Add entry {} {}", boardId, blackCardId);
		repository.save(entity);
	}

	public void resetCards(long boardId) {
		log.info("Reset entries for {}", boardId);
		repository.deleteByBoardId(boardId);
	}

	public List<BoardPlayedBlackCard> getCards(long boardId) {
		return repository.findAllByBoardId(boardId);
	}
}
