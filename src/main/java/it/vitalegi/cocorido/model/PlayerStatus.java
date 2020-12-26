package it.vitalegi.cocorido.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlayerStatus {
	Player player;
	TablePlayer tablePlayer;
	List<Long> whitecards;
}
