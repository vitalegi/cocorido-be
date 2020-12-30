package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.util.Entry;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Service
public class GetPlayedWhiteCardsService {

	@Autowired
	private PlayerActionService playerActionService;

	@Autowired
	private GetRoundPlayersService getRoundPlayersService;

	@Transactional
	public List<Entry> getPlayedWhiteCards(long roundId) {
		List<Entry> list = new ArrayList<>();
		List<Long> playersOrder = getRoundPlayersService.getPlayersOrder(roundId);
		playersOrder.forEach(playerId -> {
			List<Long> cards = playerActionService.findPlayerActions(roundId, playerId);
			list.add(createEntry(playerId, cards));
		});
		return list;
	}

	public Entry createEntry(long playerId, List<Long> cards) {
		Entry entry = new Entry();
		entry.setValues(new HashMap<>());
		entry.getValues().put("playerId", playerId);
		entry.getValues().put("whiteCards", cards);
		return entry;
	}
}
