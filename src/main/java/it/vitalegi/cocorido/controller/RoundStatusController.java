package it.vitalegi.cocorido.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.PlayerStatus;
import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.model.TablePlayer;
import it.vitalegi.cocorido.service.PlayerActionService;
import it.vitalegi.cocorido.service.PlayerService;
import it.vitalegi.cocorido.service.RoundService;
import it.vitalegi.cocorido.service.TablePlayerService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
public class RoundStatusController {

	@Autowired
	private PlayerActionService playerActionService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private RoundService roundService;

	@Autowired
	private TablePlayerService tablePlayerService;

	@LogExecutionTime
	@GetMapping("/roundStatus/{tableId}")
	public List<PlayerStatus> getRoundStatus(@PathVariable long tableId) {
		List<TablePlayer> players = tablePlayerService.getPlayers(tableId);
		Round round = roundService.getLastRound(tableId);
		log.debug("Round {}", round.getRoundId());
		return players.stream().map(player -> {
			long playerId = player.getPlayerId();
			PlayerStatus status = new PlayerStatus();
			status.setPlayer(playerService.getPlayer(playerId));
			status.setTablePlayer(player);
			status.setWhitecards(playerActionService.findPlayerActions(round.getRoundId(), playerId));
			return status;
		}).collect(Collectors.toList());
	}
}
