package it.vitalegi.cocorido.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.vitalegi.cocorido.model.TablePlayerWhiteCard;
import it.vitalegi.cocorido.repository.TablePlayerWhiteCardRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TablePlayerWhiteCardService {

	@Autowired
	private TablePlayerWhiteCardRepository repository;

	public TablePlayerWhiteCard addWhiteCard(long tableId, long playerId, long whiteCardId) {
		TablePlayerWhiteCard entry = new TablePlayerWhiteCard();
		entry.setPlayerId(playerId);
		entry.setTableId(tableId);
		entry.setWhiteCardId(whiteCardId);
		return repository.save(entry);
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

	public List<TablePlayerWhiteCard> getAll(long tableId, long playerId) {
		return repository.findAllByTableIdAndPlayerId(tableId, playerId);
	}
	public List<TablePlayerWhiteCard> getAll(long tableId) {
		return repository.findAllByTableId(tableId);
	}
}
