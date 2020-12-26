package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.service.TablePlayerWhiteCardService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
public class TablePlayerWhiteCardController {

	@Autowired
	TablePlayerWhiteCardService tablePlayerWhiteCardService;

	@LogExecutionTime
	@GetMapping("/tablePlayerWhiteCard/{tableId}/{playerId}")
	public List<Long> getWhiteCards(@PathVariable long tableId, @PathVariable long playerId) {
		return tablePlayerWhiteCardService.getWhiteCards(tableId, playerId);
	}
}
