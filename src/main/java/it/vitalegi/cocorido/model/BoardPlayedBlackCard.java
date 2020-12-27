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
@EqualsAndHashCode(of = { "blackCardId", "boardId" })
@Entity
public class BoardPlayedBlackCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long boardPlayedId;
	Long blackCardId;
	Long boardId;
}
