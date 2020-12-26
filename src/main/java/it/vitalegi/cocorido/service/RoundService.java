package it.vitalegi.cocorido.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.vitalegi.cocorido.GameStatus;
import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.model.TablePlayer;
import it.vitalegi.cocorido.repository.RoundRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoundService {

	@Autowired
	RoundRepository repository;

	@Autowired
	BlackCardService blackCardService;

	@Autowired
	PlayerActionService playerActionService;

	@Autowired
	TablePlayerService tablePlayerService;

	@Autowired
	BoardService tableService;

	public Round addRound(long tableId, long blackCardId, long nextBlackCardHolderId, GameStatus status) {

		Round entry = new Round();
		entry.setTableId(tableId);
		entry.setBlackCardId(blackCardId);
		entry.setStatus(status);
		entry.setBlackPlayerId(nextBlackCardHolderId);
		entry = repository.save(entry);
		log.info("Add to table {} round {}", tableId, entry.getRoundId());

		return entry;
	}

	public Round findLastRound(long tableId) {
		return repository.findByTableId(tableId).stream() //
				.sorted(Comparator.comparing(Round::getRoundId).reversed()) //
				.findFirst().orElse(null);
	}

	public Round getLastRound(long tableId) {
		Round round = findLastRound(tableId);
		if (round == null) {
			throw new NullPointerException("Round doesn't exist for table " + tableId);
		}
		return round;
	}

	public List<Round> getRounds(long tableId) {
		return repository.findByTableId(tableId);
	}

	public long getNextBlackPlayerId(long tableId) {
		List<TablePlayer> players = tablePlayerService.getPlayers(tableId);
		if (players.isEmpty()) {
			return -1;
		}
		Round lastRound = findLastRound(tableId);
		long currentHolder = -1;
		if (lastRound != null) {
			currentHolder = lastRound.getBlackPlayerId();
		}

		int index = -1;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getPlayerId() == currentHolder) {
				index = i;
				break;
			}
		}
		int nextIndex = (index + 1) % players.size();
		log.info("Old index: {}, new index: {}. Total size: {}", index, nextIndex, players.size());
		return players.get(nextIndex).getPlayerId();
	}

	protected Round getRound(long roundId) {
		return repository.findById(roundId).get();
	}

	public boolean isBlackPlayer(Round round, long playerId) {
		return round.getBlackPlayerId() == playerId;
	}

	public Round updateBlackPlayerId(long tableId, long blackCardHolderId) {
		Round round = getLastRound(tableId);
		round.setBlackPlayerId(blackCardHolderId);
		return updateRound(round);
	}

	public Round updateRound(Round round) {
		return repository.save(round);
	}

	public void deleteRoundsByTableId(long tableId) {
		repository.deleteByTableId(tableId);
	}
}
