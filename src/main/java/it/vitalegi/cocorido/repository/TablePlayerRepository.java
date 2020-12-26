package it.vitalegi.cocorido.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.TablePlayer;

public interface TablePlayerRepository extends CrudRepository<TablePlayer, Long> {

	public TablePlayer findByTableIdAndPlayerId(Long tableId, Long playerId);

	public List<TablePlayer> findByTableId(Long tableId);

	public void deleteByTableId(Long tableId);
}