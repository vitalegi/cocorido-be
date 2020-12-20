package it.vitalegi.cocorido.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "roundId"})
public class Round {
	long roundId;
	long tableId;
	long blackCardId;
}
