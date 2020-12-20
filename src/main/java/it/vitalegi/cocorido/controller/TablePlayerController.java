package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.TablePlayer;
import it.vitalegi.cocorido.service.TablePlayerService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
public class TablePlayerController {

	@Autowired
	TablePlayerService tablePlayerService;

	@LogExecutionTime
	@PutMapping("/tablePlayer/{tableId}/{playerId}")
	public TablePlayer addTablePlayer(@PathVariable("tableId") long tableId, @PathVariable("playerId") long playerId) {
		return tablePlayerService.addPlayer(playerId, tableId);
	}

	@LogExecutionTime
	@GetMapping("/tablePlayers/{tableId}")
	public List<TablePlayer> getTablePlayers(@PathVariable long tableId) {
		return tablePlayerService.getPlayers(tableId);
	}
	
	@LogExecutionTime
	@PatchMapping("/tablePlayers/{tableId}/{playerId}/{whiteCardId}")
	public void playWhiteCard() {
		
	}
}
