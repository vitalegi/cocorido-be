package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.model.TablePlayer;
import it.vitalegi.cocorido.util.ListUtil;
import it.vitalegi.cocorido.util.RandomUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Service
public class GetRoundPlayersService {

	@Autowired
	private RoundService roundService;

	@Autowired
	private TablePlayerService tablePlayerService;

	@Autowired
	private RandomUtil randomUtil;

	@Transactional
	public List<Long> getPlayersOrder(long roundId) {
		Round round = roundService.getRound(roundId);
		List<Long> order = round.getPlayersOrder();
		List<Long> players = getTablePlayers(round.getTableId());
		if (ListUtil.containSame(order, players)) {
			return order;
		}
		order = randomize(players);
		round.setPlayersOrder(order);
		roundService.updateRound(round);
		return order;
	}

	protected List<Long> getTablePlayers(long boardId) {
		return tablePlayerService.getPlayers(boardId).stream()//
				.map(TablePlayer::getPlayerId).collect(Collectors.toList());
	}

	protected List<Long> randomize(List<Long> list) {
		list = ListUtil.copy(list);
		List<Long> randomized = new ArrayList<>();
		while (!list.isEmpty()) {
			int nextIndex = randomUtil.random(list.size());
			randomized.add(list.remove(nextIndex));
		}
		return randomized;
	}
}
