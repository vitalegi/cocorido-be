package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.Board;
import it.vitalegi.cocorido.service.BoardService;
import it.vitalegi.cocorido.service.RoundManagerService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
public class BoardController {

	@Autowired
	BoardService tableService;
	
	@Autowired
	RoundManagerService roundManagerService;

	@LogExecutionTime
	@GetMapping("/table/{id}")
	public Board getTable(@PathVariable long id) {
		return tableService.getTable(id);
	}

	@LogExecutionTime
	@GetMapping("/tables")
	public List<Board> getTables() {
		return tableService.getTables();
	}

	@LogExecutionTime
	@DeleteMapping("/table/{tableId}")
	public void deleteTable(@PathVariable long tableId) {
		roundManagerService.deleteTable(tableId);
	}
}
