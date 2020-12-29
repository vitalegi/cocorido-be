package it.vitalegi.cocorido.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.model.Board;
import it.vitalegi.cocorido.model.Player;
import it.vitalegi.cocorido.model.WhiteCard;
import it.vitalegi.cocorido.service.BlackCardService;
import it.vitalegi.cocorido.service.PlayerService;
import it.vitalegi.cocorido.service.RoundManagerService;
import it.vitalegi.cocorido.service.WhiteCardService;

@Service
public class GameBuilder {

	@Autowired
	WhiteCardService whiteCardService;
	@Autowired
	BlackCardService blackCardService;
	@Autowired
	RoundManagerService roundServiceManager;
	@Autowired
	PlayerService playerService;

	public Player addPlayer(String name) {
		return playerService.addPlayer(name);
	}

	public Board addTable(String name, long playerId) {
		return roundServiceManager.createTable(name, playerId);
	}

	public List<WhiteCard> whiteCards(String... values) {
		List<WhiteCard> cards = new ArrayList<>();
		for (String value : values) {
			cards.add(whiteCardService.addWhiteCard(value));
		}
		return cards;
	}

	public List<BlackCard> blackCards(String... values) {
		List<BlackCard> cards = new ArrayList<>();
		for (String value : values) {
			cards.add(blackCardService.addBlackCard(value));
		}
		return cards;
	}
}
