package it.vitalegi.cocorido.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "playerId", "tableId" })
@Entity
public class TablePlayer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long tablePlayerId;
	Long playerId;
	Integer score;
	Long tableId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime lastUpdate;
}
