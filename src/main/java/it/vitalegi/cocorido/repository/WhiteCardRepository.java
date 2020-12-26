package it.vitalegi.cocorido.repository;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.WhiteCard;

public interface WhiteCardRepository extends CrudRepository<WhiteCard, Long> {

	public WhiteCard findByText(String text);

}