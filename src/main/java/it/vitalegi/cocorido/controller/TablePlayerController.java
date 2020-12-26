package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.TablePlayer;
import it.vitalegi.cocorido.service.RoundManagerService;
import it.vitalegi.cocorido.service.TablePlayerService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
public class TablePlayerController {

	@Autowired
	TablePlayerService tablePlayerService;
	@Autowired
	RoundManagerService roundManagerService;

	@LogExecutionTime
	@PutMapping("/tablePlayer/{tableId}/{playerId}")
	public TablePlayer addTablePlayer(@PathVariable("tableId") long tableId, @PathVariable("playerId") long playerId) {
		return roundManagerService.joinTable(tableId, playerId);
	}

	@LogExecutionTime
	@DeleteMapping("/tablePlayer/{tableId}/{playerId}")
	public void removePlayer(@PathVariable("tableId") long tableId, @PathVariable("playerId") long playerId) {
		roundManagerService.removePlayer(tableId, playerId);
	}

	@LogExecutionTime
	@GetMapping("/tablePlayers/{tableId}/{playerId}")
	public TablePlayer getTablePlayer(@PathVariable long tableId, @PathVariable long playerId) {
		return tablePlayerService.getPlayer(tableId, playerId);
	}

	@LogExecutionTime
	@GetMapping("/tablePlayers/{tableId}/{playerId}/phase")
	public TablePlayer getTablePlayerPhase(@PathVariable long tableId, @PathVariable long playerId) {
		return tablePlayerService.getPlayer(tableId, playerId);
	}

	@LogExecutionTime
	@GetMapping("/tablePlayers/{tableId}")
	public List<TablePlayer> getTablePlayers(@PathVariable long tableId) {
		return tablePlayerService.getPlayers(tableId);
	}
}
