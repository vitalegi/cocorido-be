package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.Table;
import it.vitalegi.cocorido.service.TableService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
public class TableController {

	@Autowired
	TableService tableService;

	@LogExecutionTime
	@PutMapping("/table/{name}")
	public Table addTable(@PathVariable String name) {
		return tableService.addTable(name);
	}

	@LogExecutionTime
	@GetMapping("/table/{id}")
	public Table getTable(@PathVariable long id) {
		return tableService.getTable(id);
	}

	@LogExecutionTime
	@GetMapping("/tables")
	public List<Table> getTables() {
		return tableService.getTables();
	}
}
