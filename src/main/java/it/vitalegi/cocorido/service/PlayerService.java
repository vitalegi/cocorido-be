package it.vitalegi.cocorido.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.Player;
import it.vitalegi.cocorido.repository.PlayerRepository;
import it.vitalegi.cocorido.util.SqlUtil;
import it.vitalegi.cocorido.util.StringUtil;

@Service
public class PlayerService {

	@Autowired
	PlayerRepository repository;
	@Autowired
	SanitizationService sanitizationService;

	@Transactional
	public Player addPlayer(String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			throw new NullPointerException("Name cannot be empty");
		}
		sanitizationService.sanitize(name);
		Player entry = new Player();
		entry.setName(name);

		return repository.save(entry);
	}

	public Player getPlayer(Long playerId) {
		return repository.findById(playerId).get();
	}

	public List<Player> getPlayers() {
		return SqlUtil.convert(repository.findAll());
	}

	public List<Player> getPlayers(List<Long> ids) {
		return getPlayers().stream()//
				.filter(player -> ids.contains(player.getPlayerId()))//
				.collect(Collectors.toList());
	}
}
