package it.vitalegi.cocorido.repository;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {

	public Board findByName(String name);
}