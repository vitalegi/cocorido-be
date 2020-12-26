package it.vitalegi.cocorido.repository;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.BlackCard;

public interface BlackCardRepository extends CrudRepository<BlackCard, Long> {

	BlackCard findByText(String text);
}