package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.PlayerAction;
import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.service.PlayerActionService;
import it.vitalegi.cocorido.service.RoundService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
public class RoundController {

	@Autowired
	RoundService roundService;

	@Autowired
	PlayerActionService playerActionService;

	@LogExecutionTime
	@GetMapping("/round/{tableId}/latest")
	public Round getLastRound(@PathVariable long tableId) {
		return roundService.getLastRound(tableId);
	}

	@LogExecutionTime
	@PostMapping("/round/{tableId}/{playerId}/play/{whiteCardId}")
	public void playWhiteCards(@PathVariable long tableId, @PathVariable long playerId,
			@PathVariable long whiteCardId) {
		Round round = roundService.getLastRound(tableId);
		playerActionService.addPlayerAction(round.getRoundId(), playerId, whiteCardId);
	}

	@LogExecutionTime
	@PutMapping("/round/{tableId}/new")
	public Round startNewRound(@PathVariable long tableId) {
		return roundService.startNewRound(tableId);
	}

	@LogExecutionTime
	@GetMapping("/actions/{tableId}")
	public List<PlayerAction> getActions(@PathVariable long tableId) {
		Round round = roundService.getLastRound(tableId);
		return playerActionService.getActions(round.getRoundId());
	}
}
