package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.model.Round;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoundService extends StorageProxy<Round> {

	@Autowired
	BlackCardService blackCardService;

	@Autowired
	PlayerActionService playerActionService;

	public Round getLastRound(long tableId) {
		List<Round> values = getValues();
		return values.stream() //
				.filter(round -> round.getTableId() == tableId) //
				.sorted(Comparator.comparing(Round::getRoundId).reversed()) //
				.findFirst().orElseThrow(() -> new NullPointerException("Round doesn't exist for table " + tableId));
	}

	public Round startNewRound(long tableId) {
		BlackCard blackCard = blackCardService.getRandomBlackCard();
		return addRound(tableId, blackCard.getId());
	}

	protected Round addRound(long tableId, long blackCardId) {
		List<Round> values = getValues();

		Round entry = new Round();
		entry.setRoundId(nextId(values));
		entry.setTableId(tableId);
		entry.setBlackCardId(blackCardId);
		addValue(entry);

		return entry;
	}

	protected Round getRound(long roundId) {
		return getValues().stream()//
				.filter(round -> round.getRoundId() == roundId)//
				.findFirst() //
				.orElseThrow(() -> new IllegalArgumentException("Round " + roundId + " doesn't exist"));
	}

	protected List<Round> getValues() {
		return storageUtil.getValue(getStorageName(), new TypeReference<List<Round>>() {
		}, new ArrayList<Round>());
	}

	protected long nextId(List<Round> values) {
		return 1 + values.stream().mapToLong(Round::getRoundId).max().orElse(10000);
	}
}
