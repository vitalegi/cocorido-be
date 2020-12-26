package it.vitalegi.cocorido.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.Round;

public interface RoundRepository extends CrudRepository<Round, Long> {

	List<Round> findByTableId(Long tableId);

	void deleteByTableId(Long tableId);

}