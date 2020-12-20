package it.vitalegi.cocorido.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "id" })
public class WhiteCard {
	long id;
	String text;
}
