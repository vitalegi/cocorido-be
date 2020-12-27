package it.vitalegi.cocorido.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "whiteCardId", "boardId" })
@Entity
public class BoardPlayedWhiteCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long boardPlayedId;
	Long whiteCardId;
	Long boardId;
}
