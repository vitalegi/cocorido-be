package it.vitalegi.cocorido.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import it.vitalegi.cocorido.GameStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "roundId" })
@Entity
public class Round {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long roundId;
	Long blackCardId;
	Long blackPlayerId;
	GameStatus status;
	Long tableId;
	Long winnerPlayerId;
	@ElementCollection
	List<Long> playersOrder;
}
