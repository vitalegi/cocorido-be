package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import it.vitalegi.cocorido.model.TablePlayer;

@Service
public class TablePlayerService extends StorageProxy<TablePlayer> {

	public void register(long playerId, long tableId) {

	}

	public TablePlayer addPlayer(long playerId, long tableId) {

		List<TablePlayer> values = getValues();

		if (values.stream().anyMatch(tp -> tp.getPlayerId() == playerId && tp.getTableId() == tableId)) {
			throw new IllegalArgumentException("Player already in game");
		}
		TablePlayer tablePlayer = new TablePlayer();
		tablePlayer.setPlayerId(playerId);
		tablePlayer.setTableId(tableId);
		tablePlayer.setWhiteCards(new ArrayList<>());
		tablePlayer.setScore(0);
		addValue(tablePlayer);

		return tablePlayer;
	}

	public void removePlayer(long playerId, long tableId) {
		TablePlayer tablePlayer = getPlayer(playerId, tableId);
		removeValue(tablePlayer);
	}

	public List<TablePlayer> getPlayers(long tableId) {
		List<TablePlayer> values = getValues();
		return values.stream().filter(tp -> tp.getTableId() == tableId).collect(Collectors.toList());
	}

	public TablePlayer getPlayer(long playerId, long tableId) {
		List<TablePlayer> values = getValues();
		return values.stream()//
				.filter(tp -> tp.getTableId() == tableId)//
				.filter(tp -> tp.getPlayerId() == playerId)//
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Player " + playerId + " table " + tableId + " not found"));
	}

	protected List<TablePlayer> getValues() {
		return storageUtil.getValue(getStorageName(), new TypeReference<List<TablePlayer>>() {
		}, new ArrayList<TablePlayer>());
	}
}
