package it.vitalegi.cocorido.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.TablePlayer;
import it.vitalegi.cocorido.repository.TablePlayerRepository;
import it.vitalegi.cocorido.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TablePlayerService {

	@Autowired
	TablePlayerRepository repository;

	@Autowired
	RoundService roundService;

	@Autowired
	WhiteCardService whiteCardService;

	@Autowired
	private TablePlayerWhiteCardService tablePlayerWhiteCardService;

	public TablePlayer addPlayer(long tableId, long playerId) {

		log.info("Add to table {} user {}", tableId, playerId);
		if (hasPlayer(tableId, playerId)) {
			throw new IllegalArgumentException("Player already in game");
		}
		TablePlayer tablePlayer = new TablePlayer();
		tablePlayer.setPlayerId(playerId);
		tablePlayer.setTableId(tableId);
		tablePlayer.setScore(0);

		tablePlayer = repository.save(tablePlayer);

		return tablePlayer;
	}

	@Transactional
	public TablePlayer updatePlayerSession(long tableId, long playerId) {
		TablePlayer player = getPlayer(tableId, playerId);
		player.setLastUpdate(LocalDateTime.now());
		return updatePlayer(player);
	}

	public TablePlayer addPlayerSafe(long tableId, long playerId) {
		if (hasPlayer(tableId, playerId)) {
			return getPlayer(tableId, playerId);
		}
		TablePlayer player = addPlayer(tableId, playerId);
		tablePlayerWhiteCardService.fillWhiteCards(tableId, playerId);
		return player;
	}

	public TablePlayer getPlayer(long tableId, long playerId) {
		return repository.findByTableIdAndPlayerId(tableId, playerId);
	}

	public List<TablePlayer> getPlayers(long tableId) {
		return SqlUtil.convert(repository.findByTableId(tableId));
	}

	public boolean hasPlayer(long tableId, long playerId) {
		return getPlayer(tableId, playerId) != null;
	}

	public void removePlayer(long tableId, long playerId) {
		TablePlayer tablePlayer = getPlayer(tableId, playerId);
		repository.delete(tablePlayer);
	}

	public void removePlayers(long tableId) {
		repository.deleteByTableId(tableId);
	}

	public TablePlayer updatePlayer(TablePlayer player) {
		return repository.save(player);
	}
}
