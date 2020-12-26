package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		List<WhiteCard> whitecards = whiteCardService.getWhiteCards();

		List<Long> blacklist = repository.findByTableId(tableId)//
				.stream().map(TablePlayerWhiteCard::getWhiteCardId) //
				.collect(Collectors.toList());

		List<Long> whitelist = whitecards.stream().map(WhiteCard::getId)
				.filter(id -> blacklist.stream().noneMatch(blacklistId -> {
					return blacklistId.longValue() == id.longValue();
				}))//
				.collect(Collectors.toList());

		List<Long> newCards = new ArrayList<>();
		for (int i = 0; i < cards; i++) {
			int index = RandomUtil.random(whitelist.size());
			long newCard = whitelist.get(index);
			newCards.add(newCard);
			whitelist.remove(index);
		}
		return newCards;
	}

	public List<Long> getWhiteCards(Long tableId, Long playerId) {
		return repository.findByTableIdAndPlayerId(tableId, playerId)//
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
