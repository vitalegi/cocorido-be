package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.BoardPlayedWhiteCard;
import it.vitalegi.cocorido.model.TablePlayerWhiteCard;
import it.vitalegi.cocorido.model.WhiteCard;
import it.vitalegi.cocorido.repository.TablePlayerWhiteCardRepository;
import it.vitalegi.cocorido.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TablePlayerWhiteCardService {

	public static final long MAX_WHITECARDS = 11;

	@Autowired
	TablePlayerWhiteCardRepository repository;

	@Autowired
	WhiteCardService whiteCardService;

	@Autowired
	BoardPlayedWhiteCardService boardPlayedWhiteCardService;

	@Transactional
	public List<TablePlayerWhiteCard> fillWhiteCards(long tableId, long playerId) {
		long cards = repository.countByTableIdAndPlayerId(tableId, playerId);
		long missingCards = MAX_WHITECARDS - cards;
		List<Long> newCards = getNewWhiteCards(tableId, missingCards);
		log.info("Assigning white cards to table {}, player {}. Missing cards: {}", tableId, playerId, missingCards);

		return newCards.stream().map(cardId -> addWhiteCard(tableId, playerId, cardId)) //
				.collect(Collectors.toList());
	}

	public TablePlayerWhiteCard addWhiteCard(long tableId, long playerId, long whiteCardId) {
		TablePlayerWhiteCard entry = new TablePlayerWhiteCard();
		entry.setPlayerId(playerId);
		entry.setTableId(tableId);
		entry.setWhiteCardId(whiteCardId);
		return repository.save(entry);
	}

	public List<Long> getNewWhiteCards(long tableId, long cards) {
		if (cards == 0) {
			return new ArrayList<>();
		}
		List<Long> whitecards = whiteCardService.getWhiteCards().stream()//
				.map(WhiteCard::getId).sorted()//
				.collect(Collectors.toList());

		List<BoardPlayedWhiteCard> cardsUsed = boardPlayedWhiteCardService.getCards(tableId);

		List<Long> cardsInUse = repository.findAllByTableId(tableId)//
				.stream().map(TablePlayerWhiteCard::getWhiteCardId) //
				.sorted().collect(Collectors.toList());

		List<Long> cardsNotUsed = whitecards.stream() //
				.filter(whiteCardId -> cardsUsed.stream()//
						.noneMatch(usedCard -> usedCard.getWhiteCardId().longValue() == whiteCardId.longValue())) //
				.sorted().collect(Collectors.toList());

		List<Long> cardsNotInUse = whitecards.stream() //
				.filter(id -> cardsInUse.stream().noneMatch(blacklistId -> {
					return blacklistId.longValue() == id.longValue();
				}))//
				.sorted().collect(Collectors.toList());

		if (log.isDebugEnabled()) {
			log.debug("cards remaining {}", cards);
			log.debug("whitecards {}", whitecards);
			log.debug("cardsInUse {}", cardsInUse);
			log.debug("cardsNotUsed {}", cardsNotUsed);
			log.debug("cardsNotInUse {}", cardsNotInUse);
		}
		List<Long> availableCards = null;
		if (cardsNotUsed.size() >= cards) {
			log.info("Enough cards remaining, pick from not used");
			availableCards = cardsNotUsed;
		} else {
			log.info("Not enough cards not used, re-shuffle and pick from not in use");
			boardPlayedWhiteCardService.resetCards(tableId);
			cardsUsed.forEach(cardUsed -> boardPlayedWhiteCardService.addCard(tableId, cardUsed.getWhiteCardId()));
			availableCards = cardsNotInUse;
		}
		if (availableCards.size() < cards) {
			log.error("Not enough cards, expected {}, available {}", cards, availableCards.size());
			availableCards = whitecards.stream().collect(Collectors.toList());
		}
		List<Long> newCards = new ArrayList<>();
		for (int i = 0; i < cards; i++) {
			int index = RandomUtil.random(availableCards.size());
			long newCard = availableCards.get(index);
			newCards.add(newCard);
			availableCards.remove(index);
			boardPlayedWhiteCardService.addCard(tableId, newCard);
		}
		return newCards;
	}

	public List<Long> getWhiteCards(Long tableId, Long playerId) {
		return repository.findAllByTableIdAndPlayerId(tableId, playerId)//
				.stream().map(TablePlayerWhiteCard::getWhiteCardId)//
				.collect(Collectors.toList());
	}

	public void removeWhiteCard(long tableId, long playerId, long whiteCardId) {
		repository.deleteByTableIdAndPlayerIdAndWhiteCardId(tableId, playerId, whiteCardId);
	}

	public void removeEntriesByTableId(long tableId) {
		repository.deleteByTableId(tableId);
	}
}
