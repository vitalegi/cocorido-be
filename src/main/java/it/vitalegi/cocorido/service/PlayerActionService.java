package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import it.vitalegi.cocorido.model.PlayerAction;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlayerActionService extends StorageProxy<PlayerAction> {

	public List<PlayerAction> getActions(long roundId) {
		return getValues().stream() //
				.filter(pa -> pa.getRoundId() == roundId) //
				.sorted(Comparator.comparing(PlayerAction::getPlayerActionId)) //
				.collect(Collectors.toList());
	}

	public List<Long> findPlayerActions(long roundId, long playerId) {
		return getValues().stream() //
				.filter(pa -> pa.getRoundId() == roundId) //
				.filter(pa -> pa.getPlayerId() == playerId) //
				.map(PlayerAction::getWhitecardId) //
				.collect(Collectors.toList());
	}

	public PlayerAction addPlayerAction(long roundId, long playerId, long whiteCardId) {
		PlayerAction playerAction = new PlayerAction();
		playerAction.setPlayerActionId(nextId());
		playerAction.setPlayerId(playerId);
		playerAction.setWhitecardId(whiteCardId);
		List<PlayerAction> values = getValues();
		values.add(playerAction);
		setValues(values);
		return playerAction;
	}

	protected List<PlayerAction> getValues() {
		return storageUtil.getValue(getStorageName(), new TypeReference<List<PlayerAction>>() {
		}, new ArrayList<PlayerAction>());
	}

	protected long nextId() {
		return nextId(getValues());
	}

	protected long nextId(List<PlayerAction> values) {
		return 1 + values.stream().mapToLong(PlayerAction::getPlayerActionId).max().orElse(0);
	}
}
