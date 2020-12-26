package it.vitalegi.cocorido.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.Board;
import it.vitalegi.cocorido.repository.BoardRepository;
import it.vitalegi.cocorido.util.SqlUtil;
import it.vitalegi.cocorido.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {

	@Autowired
	BoardRepository repository;
	@Autowired
	SanitizationService sanitizationService;

	@Transactional
	public Board addTable(String name, long boardOwnerId) {
		log.info("addTable {}", name);
		if (StringUtil.isNullOrEmpty(name)) {
			throw new NullPointerException("Name cannot be empty");
		}
		name = sanitizationService.sanitize(name);

		if (repository.findByName(name) != null) {
			throw new IllegalArgumentException("Name already in use");
		}

		Board table = new Board();
		table.setName(name);
		table.setLastUpdate(LocalDateTime.now());
		table.setBoardOwnerId(boardOwnerId);
		table = repository.save(table);
		log.info("addTable {}, id= {}", name, table.getId());
		return table;
	}

	@Transactional
	public Board updateSession(long tableId) {
		Board board = getTable(tableId);
		board.setLastUpdate(LocalDateTime.now());
		return updateTable(board);
	}

	public Board getTable(long tableId) {
		return repository.findById(tableId).get();
	}

	public List<Board> getTables() {
		return SqlUtil.convert(repository.findAll());
	}

	public Board updateTable(Board entry) {
		return repository.save(entry);
	}

	public void deleteTable(long tableId) {
		repository.deleteById(tableId);
	}
}
