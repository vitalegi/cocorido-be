package it.vitalegi.cocorido.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.vitalegi.cocorido.GameStatus;
import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.model.BoardPlayedBlackCard;
import it.vitalegi.cocorido.model.BoardPlayedWhiteCard;
import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.model.TablePlayer;
import it.vitalegi.cocorido.model.TablePlayerWhiteCard;
import it.vitalegi.cocorido.model.WhiteCard;

@Service
public class ModelBuilder {

	public WhiteCard whiteCard(long id, String text) {
		WhiteCard whiteCard = new WhiteCard();
		whiteCard.setId(id);
		whiteCard.setText(text);
		return whiteCard;
	}

	public List<WhiteCard> whiteCards(long... ids) {
		List<WhiteCard> entries = new ArrayList<>();
		for (long id : ids) {
			entries.add(whiteCard(id, "" + id));
		}
		return entries;
	}

	public BlackCard blackCard(long id, String text) {
		BlackCard blackCard = new BlackCard();
		blackCard.setId(id);
		blackCard.setText(text);
		return blackCard;
	}

	public List<BlackCard> blackCards(long... ids) {
		List<BlackCard> entries = new ArrayList<>();
		for (long id : ids) {
			entries.add(blackCard(id, "" + id));
		}
		return entries;
	}

	public List<BoardPlayedWhiteCard> boardPlayedWhiteCards(long boardId, long... whiteCardIds) {
		List<BoardPlayedWhiteCard> entries = new ArrayList<>();
		for (long id : whiteCardIds) {
			entries.add(boardPlayedWhiteCard(boardId, 0, id));
		}
		return entries;
	}

	public BoardPlayedWhiteCard boardPlayedWhiteCard(long boardId, long boardPlayedId, long whiteCardId) {
		BoardPlayedWhiteCard entry = new BoardPlayedWhiteCard();
		entry.setBoardId(boardId);
		entry.setBoardPlayedId(boardPlayedId);
		entry.setWhiteCardId(whiteCardId);
		return entry;
	}

	public List<BoardPlayedBlackCard> boardPlayedBlackCards(long boardId, long... blackCardIds) {
		List<BoardPlayedBlackCard> entries = new ArrayList<>();
		for (long id : blackCardIds) {
			entries.add(boardPlayedBlackCard(boardId, 0, id));
		}
		return entries;
	}

	public BoardPlayedBlackCard boardPlayedBlackCard(long boardId, long boardPlayedId, long blackCardId) {
		BoardPlayedBlackCard entry = new BoardPlayedBlackCard();
		entry.setBoardId(boardId);
		entry.setBoardPlayedId(boardPlayedId);
		entry.setBlackCardId(blackCardId);
		return entry;
	}

	public List<TablePlayerWhiteCard> tablePlayerWhiteCards(long boardId, long playerId, long... whiteCardIds) {
		List<TablePlayerWhiteCard> entries = new ArrayList<>();
		for (long id : whiteCardIds) {
			entries.add(tablePlayerWhiteCard(boardId, playerId, id));
		}
		return entries;
	}

	public TablePlayerWhiteCard tablePlayerWhiteCard(long boardId, long playerId, long whiteCardId) {
		TablePlayerWhiteCard entry = new TablePlayerWhiteCard();
		entry.setPlayerId(playerId);
		entry.setTableId(boardId);
		entry.setWhiteCardId(whiteCardId);
		return entry;
	}

	public Round round(Long roundId, Long blackCardId, Long blackPlayerId, GameStatus status, Long tableId,
			Long winnerPlayerId, List<Long> playersOrder) {
		Round entry = new Round();
		entry.setBlackCardId(blackCardId);
		entry.setBlackPlayerId(blackPlayerId);
		entry.setStatus(status);
		entry.setTableId(tableId);
		entry.setWinnerPlayerId(winnerPlayerId);
		entry.setPlayersOrder(playersOrder);
		return entry;
	}

	public TablePlayer tablePlayer(Long tablePlayerId, Long playerId, Integer score, Long tableId) {
		TablePlayer entry = new TablePlayer();
		entry.setTablePlayerId(tablePlayerId);
		entry.setPlayerId(playerId);
		entry.setScore(score);
		entry.setTableId(tableId);
		return entry;
	}

	public List<TablePlayer> tablePlayers(Long tableId, Long... playerIds) {
		List<TablePlayer> entries = new ArrayList<>();
		for (long id : playerIds) {
			entries.add(tablePlayer(null, id, null, tableId));
		}
		return entries;
	}
}
