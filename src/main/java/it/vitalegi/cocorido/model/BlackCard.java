package it.vitalegi.cocorido.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "id" })
public class BlackCard {
	long id;
	String text;
	int whitecards;
}
