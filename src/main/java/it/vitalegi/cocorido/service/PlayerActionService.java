package it.vitalegi.cocorido.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.PlayerAction;
import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.repository.PlayerActionRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlayerActionService {

	@Autowired
	RoundService roundService;

	@Autowired
	TablePlayerWhiteCardService tablePlayerWhiteCardService;

	@Autowired
	PlayerActionRepository repository;

	@Transactional
	public PlayerAction addPlayerAction(long roundId, long playerId, long whiteCardId) {
		PlayerAction entry = new PlayerAction();
		entry.setRoundId(roundId);
		entry.setPlayerId(playerId);
		entry.setWhitecardId(whiteCardId);

		entry = repository.save(entry);

		Round round = roundService.getRound(roundId);
		tablePlayerWhiteCardService.removeWhiteCard(round.getTableId(), playerId, whiteCardId);

		return entry;
	}

	public List<Long> findPlayerActions(long roundId, long playerId) {
		return repository.findByRoundIdAndPlayerId(roundId, playerId).stream()//
				.map(PlayerAction::getWhitecardId) //
				.collect(Collectors.toList());
	}

	public List<PlayerAction> getActions(long roundId) {
		return repository.findByRoundId(roundId).stream() //
				.sorted(Comparator.comparing(PlayerAction::getPlayerActionId)) //
				.collect(Collectors.toList());
	}

	public void deletePlayerActionsByRoundId(long roundId) {
		repository.deleteByRoundId(roundId);
	}
}
