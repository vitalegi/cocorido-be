package it.vitalegi.cocorido.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.TablePlayerWhiteCard;

public interface TablePlayerWhiteCardRepository extends CrudRepository<TablePlayerWhiteCard, Long> {

	public List<TablePlayerWhiteCard> findAllByTableIdAndPlayerId(Long tableId, Long playerId);

	public List<TablePlayerWhiteCard> deleteByTableIdAndPlayerIdAndWhiteCardId(Long tableId, Long playerId,
			Long whiteCardId);

	public List<TablePlayerWhiteCard> findAllByTableId(Long tableId);

	public long countByTableIdAndPlayerId(Long tableId, Long playerId);

	public void deleteByTableId(Long tableId);
}