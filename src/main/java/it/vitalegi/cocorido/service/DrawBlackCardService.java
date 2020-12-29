package it.vitalegi.cocorido.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.model.BoardPlayedBlackCard;
import it.vitalegi.cocorido.util.ListUtil;
import it.vitalegi.cocorido.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DrawBlackCardService {

	@Autowired
	BlackCardService blackCardService;

	@Autowired
	BoardPlayedBlackCardService boardPlayedBlackCardService;

	@Transactional
	public long drawCard(long tableId) {

		List<Long> cards = getBlackCards(tableId);

		if (cards.isEmpty()) {
			throw new NullPointerException("Missing configuration, no blackcards provided");
		}
		List<Long> usedCards = getDrawnCards(tableId);
		List<Long> remainingCards = ListUtil.diff(cards, usedCards);
		log.debug("Black cards: total {}, used {}, remaining {}", cards.size(), usedCards.size(), remainingCards.size());
		if (remainingCards.isEmpty()) {
			log.info("Board {} used all available black cards, re-shuffle");
			boardPlayedBlackCardService.resetCards(tableId);
			return drawCard(tableId, cards);
		}
		return drawCard(tableId, remainingCards);
	}

	protected Long drawCard(Long boardId, List<Long> deck) {
		long blackCardId = deck.get(RandomUtil.random(deck.size()));
		boardPlayedBlackCardService.addCard(boardId, blackCardId);
		return blackCardId;
	}

	protected List<Long> getBlackCards(long boardId) {
		return blackCardService.getBlackCards().stream()//
				.map(BlackCard::getId).collect(Collectors.toList());
	}

	protected List<Long> getDrawnCards(long boardId) {
		return boardPlayedBlackCardService.getCards(boardId).stream()//
				.map(BoardPlayedBlackCard::getBlackCardId).collect(Collectors.toList());

	}
}
