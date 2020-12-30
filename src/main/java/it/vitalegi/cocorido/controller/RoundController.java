package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.GameStatus;
import it.vitalegi.cocorido.model.Board;
import it.vitalegi.cocorido.model.PlayerAction;
import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.service.PlayerActionService;
import it.vitalegi.cocorido.service.RoundManagerService;
import it.vitalegi.cocorido.service.RoundService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
public class RoundController {

	@Autowired
	private PlayerActionService playerActionService;

	@Autowired
	private RoundManagerService roundManagerService;

	@Autowired
	private RoundService roundService;

	@LogExecutionTime
	@PostMapping("/round/{playerId}")
	public Board createTable(@PathVariable long playerId, @RequestBody Board table) {
		return roundManagerService.createTable(table.getName(), playerId);
	}

	@LogExecutionTime
	@GetMapping("/actions/{tableId}")
	public List<PlayerAction> getActions(@PathVariable long tableId) {
		Round round = roundService.getLastRound(tableId);
		return playerActionService.getActions(round.getRoundId());
	}

	@LogExecutionTime
	@GetMapping("/round/{tableId}/latest")
	public Round getLastRound(@PathVariable long tableId) {
		return roundService.getLastRound(tableId);
	}

	@LogExecutionTime
	@GetMapping("/round/{tableId}")
	public GameStatus getStatus(@PathVariable long tableId) {
		Round round = roundService.getLastRound(tableId);
		return roundManagerService.getStatus(round.getRoundId());
	}

	@LogExecutionTime
	@PostMapping("/round/{tableId}/{playerId}/play/{whiteCardId}")
	public void playWhiteCards(@PathVariable long tableId, @PathVariable long playerId,
			@PathVariable long whiteCardId) {
		roundManagerService.playWhiteCard(tableId, playerId, whiteCardId);
	}

	@LogExecutionTime
	@PostMapping("/round/{tableId}/winner/{winnerPlayerId}")
	public void selectWinner(@PathVariable long tableId, @PathVariable long winnerPlayerId) {
		roundManagerService.selectWinner(tableId, winnerPlayerId);
	}

	@LogExecutionTime
	@PostMapping("/round/{tableId}/{force}")
	public GameStatus updateStatus(@PathVariable long tableId, @PathVariable boolean force,
			@RequestHeader("userId") long userId) {
		Round round = roundService.getLastRound(tableId);
		return roundManagerService.nextStatus(round.getRoundId(), userId, force);
	}

	@LogExecutionTime
	@PatchMapping("/round/{tableId}/blackCard/change")
	public void changeBlackCard(@PathVariable long tableId) {
		roundManagerService.changeBlackCard(tableId);
	}
}
