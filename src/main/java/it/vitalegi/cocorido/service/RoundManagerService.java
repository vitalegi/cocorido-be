package it.vitalegi.cocorido.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.GameStatus;
import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.model.Board;
import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.model.TablePlayer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoundManagerService {

	@Autowired
	private BlackCardService blackCardService;

	@Autowired
	private PlayerActionService playerActionService;

	@Autowired
	private RoundService roundService;

	@Autowired
	private TablePlayerService tablePlayerService;
	@Autowired
	private TablePlayerWhiteCardService tablePlayerWhiteCardService;

	@Autowired
	private BoardService tableService;

	@Transactional
	public void removePlayer(long tableId, long playerId) {
		log.info("Delete player {} from {}", playerId, tableId);
		tablePlayerService.removePlayer(tableId, playerId);
		Round round = roundService.getLastRound(tableId);
		if (round.getBlackPlayerId() == playerId) {
			initRound(tableId);
		}
	}

	@Transactional
	public Board createTable(String name, long playerId) {
		log.info("Create table {}", name);
		Board table = tableService.addTable(name, playerId);
		tablePlayerService.addPlayer(table.getId(), playerId);
		initRound(table.getId());
		tablePlayerWhiteCardService.fillWhiteCards(table.getId(), playerId);

		return table;
	}

	@Transactional
	public TablePlayer joinTable(long tableId, long playerId) {
		return tablePlayerService.addPlayerSafe(tableId, playerId);
	}

	@Transactional
	public void deleteTable(long tableId) {
		log.info("Delete table procedure - {}", tableId);

		log.info("Delete TablePlayerWhiteCard entries");
		tablePlayerWhiteCardService.removeEntriesByTableId(tableId);

		log.info("Delete TablePlayer entries");
		tablePlayerService.removePlayers(tableId);

		List<Round> rounds = roundService.getRounds(tableId);
		rounds.forEach(round -> {
			log.info("Delete PlayerAction entries of round {}", round.getRoundId());
			playerActionService.deletePlayerActionsByRoundId(round.getRoundId());
		});
		log.info("Delete Round entries");
		roundService.deleteRoundsByTableId(tableId);
		log.info("Delete Table entries");
		tableService.deleteTable(tableId);
	}

	public GameStatus getStatus(long roundId) {
		Round round = roundService.getRound(roundId);
		return round.getStatus();
	}

	protected boolean hasPlayedAllCards(long roundId, long playerId) {
		Round round = roundService.getRound(roundId);
		List<Long> actions = playerActionService.findPlayerActions(roundId, playerId);
		BlackCard blackCard = blackCardService.getBlackCard(round.getBlackCardId());
		int expectedCards = blackCard.getWhitecards();
		int actualCards = actions.size();
		log.debug("round {}, player {}, expected {}, actual {}", roundId, playerId, expectedCards, actualCards);
		return expectedCards == actualCards;
	}

	protected Round initRound(long tableId) {
		BlackCard blackCard = blackCardService.getRandomBlackCard();
		long nextBlackPlayer = roundService.getNextBlackPlayerId(tableId);
		return roundService.addRound(tableId, blackCard.getId(), nextBlackPlayer,
				GameStatus.WHITE_PLAYERS_CHOOSING_CARD);
	}

	@Transactional
	public GameStatus nextStatus(long roundId, long authId, boolean force) {
		Round round = roundService.getRound(roundId);
		long tableId = round.getTableId();
		tableService.updateSession(tableId);
		tablePlayerService.updatePlayerSession(tableId, authId);
		GameStatus status = round.getStatus();
		log.debug("table {}, force {}, status {}", tableId, force, status);

		if (status == null || status == GameStatus.NEW_ROUND) {

			Round newRound = initRound(tableId);
			log.info("new round {}, new status {}", newRound.getRoundId(), newRound.getStatus());

			List<TablePlayer> players = tablePlayerService.getPlayers(tableId);
			for (TablePlayer player : players) {
				tablePlayerWhiteCardService.fillWhiteCards(tableId, player.getPlayerId());
			}

			return newRound.getStatus();
		}
		if (status == GameStatus.WHITE_PLAYERS_CHOOSING_CARD) {
			List<TablePlayer> players = tablePlayerService.getPlayers(tableId);
			if (players.size() <= 1) {
				log.debug("Players: {}, keep the phase");
				return round.getStatus();
			}
			long blackPlayerId = round.getBlackPlayerId();
			boolean whiteCardsPlayed = players.stream()//
					.map(TablePlayer::getPlayerId) //
					.filter(id -> blackPlayerId != id)//
					.allMatch(playerId -> hasPlayedAllCards(roundId, playerId));

			if (force || whiteCardsPlayed) {
				round.setStatus(GameStatus.BLACK_PLAYER_VOTING);
				roundService.updateRound(round);
				log.info("closed white player phase, force={} whiteCardsPlayed={}", force, whiteCardsPlayed);
			}
			return round.getStatus();
		}
		if (status == GameStatus.BLACK_PLAYER_VOTING) {
			log.debug("black player is voting");

			List<TablePlayer> players = tablePlayerService.getPlayers(tableId);
			boolean whiteCardsPlayed = players.stream()//
					.map(TablePlayer::getPlayerId) //
					.anyMatch(playerId -> hasPlayedAllCards(roundId, playerId));
			if (whiteCardsPlayed) {
				return round.getStatus();
			}
			log.info("No player have completed the white cards selection, skip the round");
			round.setStatus(GameStatus.NEW_ROUND);
			roundService.updateRound(round);
		}
		log.info("Unknown status {}", round.getStatus());
		return round.getStatus();
	}

	@Transactional
	public void playWhiteCard(long tableId, long playerId, long whiteCardId) {
		Round round = roundService.getLastRound(tableId);
		BlackCard blackCard = blackCardService.getBlackCard(round.getBlackCardId());
		int expectedWhitecards = blackCard.getWhitecards();
		List<Long> actualWhitecards = playerActionService.findPlayerActions(round.getRoundId(), playerId);
		if (actualWhitecards.size() >= expectedWhitecards) {
			log.info("Can't play whitecard {} on table {}, player {}. Already played {}, expected {}.", whiteCardId,
					tableId, playerId, actualWhitecards.size(), expectedWhitecards);
			return;
		}
		playerActionService.addPlayerAction(round.getRoundId(), playerId, whiteCardId);
	}

	@Transactional
	public void selectWinner(long tableId, long winnerPlayerId) {
		Round round = roundService.getLastRound(tableId);
		if (round.getWinnerPlayerId() != null) {
			return;
		}
		TablePlayer player = tablePlayerService.getPlayer(tableId, winnerPlayerId);

		log.info("Winner of the round {} is {}", round.getRoundId(), winnerPlayerId);

		player.setScore(player.getScore() + 1);
		tablePlayerService.updatePlayer(player);

		round.setWinnerPlayerId(winnerPlayerId);
		round.setStatus(GameStatus.NEW_ROUND);
		roundService.updateRound(round);
	}
}
