package it.vitalegi.cocorido.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.PlayerAction;

public interface PlayerActionRepository extends CrudRepository<PlayerAction, Long> {

	List<PlayerAction> findByRoundId(Long roundId);

	List<PlayerAction> findByRoundIdAndPlayerId(Long roundId, Long playerId);

	void deleteByRoundId(Long roundId);
}