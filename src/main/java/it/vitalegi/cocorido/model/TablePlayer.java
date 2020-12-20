package it.vitalegi.cocorido.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "playerId", "tableId" })
public class TablePlayer {
	long playerId;
	long tableId;
	int score;
	List<Long> whiteCards;
}
