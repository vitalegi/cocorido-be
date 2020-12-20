package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import it.vitalegi.cocorido.model.Table;
import it.vitalegi.cocorido.util.StringUtil;

@Service
public class TableService extends StorageProxy<Table> {

	@Autowired
	RoundService roundService;

	public Table addTable(String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			throw new NullPointerException("Name cannot be empty");
		}

		List<Table> tables = getValues();

		if (tables.stream().anyMatch(t -> t.getName().equals(name))) {
			throw new IllegalArgumentException("Name already in use");
		}

		Table table = new Table();
		table.setId(nextId(tables));
		table.setName(name);

		addValue(table);
		roundService.startNewRound(table.getId());
		return table;
	}

	public Table getTable(long tableId) {
		List<Table> tables = getValues();
		return tables.stream().filter(t -> t.getId() == tableId)//
				.findFirst().orElseThrow(() -> new RuntimeException("Table " + tableId + " doesn't exist"));
	}

	public List<Table> getTables() {
		return getValues();
	}

	protected long nextId(List<Table> tables) {
		return 1 + tables.stream().mapToLong(Table::getId).max().orElse(10000);
	}

	protected List<Table> getValues() {
		return storageUtil.getValue(getStorageName(), new TypeReference<List<Table>>() {
		}, new ArrayList<Table>());
	}
}
