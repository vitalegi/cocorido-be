package it.vitalegi.cocorido.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.BoardPlayedWhiteCard;
import it.vitalegi.cocorido.repository.BoardPlayedWhiteCardRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardPlayedWhiteCardService {

	@Autowired
	BoardPlayedWhiteCardRepository repository;

	@Transactional
	public void addCard(long boardId, long whiteCardId) {
		BoardPlayedWhiteCard entity = new BoardPlayedWhiteCard();
		entity.setBoardId(boardId);
		entity.setWhiteCardId(whiteCardId);
		log.info("Add entry {} {}", boardId, whiteCardId);
		repository.save(entity);
	}

	public void resetCards(long boardId) {
		log.info("Reset entries for {}", boardId);
		repository.deleteByBoardId(boardId);
	}

	public List<BoardPlayedWhiteCard> getCards(long boardId) {
		return repository.findAllByBoardId(boardId);
	}
}
