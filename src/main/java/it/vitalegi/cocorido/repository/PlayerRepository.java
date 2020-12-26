package it.vitalegi.cocorido.repository;

import org.springframework.data.repository.CrudRepository;

import it.vitalegi.cocorido.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {

}