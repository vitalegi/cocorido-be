package it.vitalegi.cocorido.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PlayerAction {
	long playerActionId;
	long roundId;
	long playerId;
	long whitecardId;
}
