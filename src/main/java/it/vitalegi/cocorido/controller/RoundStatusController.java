package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.Round;
import it.vitalegi.cocorido.service.GetPlayedWhiteCardsService;
import it.vitalegi.cocorido.service.RoundService;
import it.vitalegi.cocorido.util.Entry;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
public class RoundStatusController {

	@Autowired
	private RoundService roundService;

	@Autowired
	private GetPlayedWhiteCardsService getPlayedWhiteCardsService;

	@LogExecutionTime
	@GetMapping("/roundStatus/{tableId}/whiteCards")
	public List<Entry> getWhiteCards(@PathVariable long tableId) {
		Round round = roundService.getLastRound(tableId);
		return getPlayedWhiteCardsService.getPlayedWhiteCards(round.getRoundId());
	}
}
