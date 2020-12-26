package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.Player;
import it.vitalegi.cocorido.service.PlayerService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
public class PlayerController {

	@Autowired
	PlayerService playerService;

	@LogExecutionTime
	@PutMapping("/player")
	public Player addTable(@RequestBody Player player) {
		return playerService.addPlayer(player.getName());
	}

	@LogExecutionTime
	@GetMapping("/player/{playerId}")
	public Player getPlayer(@PathVariable long playerId) {
		return playerService.getPlayer(playerId);
	}

	@LogExecutionTime
	@GetMapping("/players")
	public List<Player> getPlayers() {
		return playerService.getPlayers();
	}
}
