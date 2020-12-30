package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.BoardPlayedWhiteCard;
import it.vitalegi.cocorido.model.TablePlayerWhiteCard;
import it.vitalegi.cocorido.model.WhiteCard;
import it.vitalegi.cocorido.util.ListUtil;
import it.vitalegi.cocorido.util.RandomUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@Service
public class DrawWhiteCardService {

	@Value("${game.config.cards-in-hand}")
	private int cardsInHand;

	@Autowired
	private WhiteCardService whiteCardService;

	@Autowired
	private BoardPlayedWhiteCardService boardPlayedWhiteCardService;

	@Autowired
	private TablePlayerWhiteCardService tablePlayerWhiteCardService;

	@Autowired
	private RandomUtil randomUtil;

	@Transactional
	public List<TablePlayerWhiteCard> drawCards(long tableId, long playerId) {
		int cards = tablePlayerWhiteCardService.getAll(tableId, playerId).size();
		int missingCards = cardsInHand - cards;
		List<Long> newCards = getNewWhiteCards(tableId, missingCards);
		log.info("Assigning white cards to table {}, player {}. Missing cards: {}", tableId, playerId, missingCards);

		return newCards.stream().map(cardId -> tablePlayerWhiteCardService.addWhiteCard(tableId, playerId, cardId)) //
				.collect(Collectors.toList());
	}

	protected List<Long> getNewWhiteCards(long tableId, int cards) {
		log.info("Draw {} cards for board {}", cards, tableId);
		if (cards < 0) {
			return new ArrayList<>();
		}
		List<Long> whitecards = getWhiteCards();
		List<Long> cardsUsed = getCardsUsed(tableId);
		List<Long> cardsInUse = getCardsInUse(tableId);
		List<Long> deck = getAndUpdateAvailableDeck(tableId, cards, whitecards, cardsUsed, cardsInUse);

		if (log.isDebugEnabled()) {
			List<Long> cardsNotUsed = ListUtil.diff(whitecards, cardsUsed);
			List<Long> cardsNotInUse = ListUtil.diff(whitecards, cardsInUse);

			log.debug("cards remaining {}", cards);
			log.debug("whitecards {}", whitecards);
			log.debug("cardsInUse {}", cardsInUse);
			log.debug("cardsNotUsed {}", cardsNotUsed);
			log.debug("cardsNotInUse {}", cardsNotInUse);
			log.debug("availableCards {}", deck);
		}
		return drawCardsAndUpdateDeck(tableId, deck, cards);
	}

	protected List<Long> getWhiteCards() {
		return whiteCardService.getWhiteCards().stream()//
				.map(WhiteCard::getId)//
				.collect(Collectors.toList());
	}

	protected List<Long> getCardsInUse(long tableId) {
		return tablePlayerWhiteCardService.getAll(tableId)//
				.stream().map(TablePlayerWhiteCard::getWhiteCardId) //
				.collect(Collectors.toList());
	}

	protected List<Long> getCardsUsed(long tableId) {
		return boardPlayedWhiteCardService.getCards(tableId).stream()//
				.map(BoardPlayedWhiteCard::getWhiteCardId) //
				.collect(Collectors.toList());
	}

	protected List<Long> getAndUpdateAvailableDeck(long tableId, int expectedCards, List<Long> whitecards,
			List<Long> cardsUsed, List<Long> cardsInUse) {

		List<Long> cardsNotUsed = ListUtil.diff(whitecards, cardsUsed);
		List<Long> cardsNotInUse = ListUtil.diff(whitecards, cardsInUse);

		if (cardsNotUsed.size() >= expectedCards) {
			log.info("Enough cards remaining, pick from not used");
			return cardsNotUsed;
		}
		log.info("Not enough cards available in the remaining deck, re-shuffle and pick from not in use");
		boardPlayedWhiteCardService.resetCards(tableId);
		boardPlayedWhiteCardService.addCards(tableId, cardsUsed);
		if (cardsNotInUse.size() >= expectedCards) {
			return cardsNotInUse;
		}
		log.error("Not enough cards, expected {}, available {}, pick also duplicates", expectedCards,
				cardsNotInUse.size());
		return whitecards.stream().collect(Collectors.toList());
	}

	protected List<Long> drawCardsAndUpdateDeck(long tableId, List<Long> deck, int cards) {
		if (deck.size() < cards) {
			log.info("Cards: {} Deck: {}", cards, deck);
			throw new IllegalArgumentException("Deck doesn't have all the needed cards");
		}
		deck = ListUtil.copy(deck);
		List<Long> newCards = new ArrayList<>();
		for (int i = 0; i < cards; i++) {
			int newCardIndex = nextCardIndex(deck);
			Long newCard = deck.get(newCardIndex);
			newCards.add(newCard);
			deck.remove(newCardIndex);
			boardPlayedWhiteCardService.addCard(tableId, newCard);
		}
		return newCards;
	}

	protected int nextCardIndex(List<Long> deck) {
		return randomUtil.random(deck.size());
	}
}
