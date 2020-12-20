package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.service.BlackCardService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
public class BlackCardController {

	@Autowired
	BlackCardService blackCardService;

	@LogExecutionTime
	@PutMapping("/blackCard")
	public BlackCard addBlackCard(@RequestBody BlackCard card) {
		return blackCardService.addBlackCard(card.getText());
	}

	@LogExecutionTime
	@GetMapping("/blackCard/{blackCardId}")
	public BlackCard getBlackCard(@PathVariable long blackCardId) {
		return blackCardService.getBlackCard(blackCardId);
	}
	
	@LogExecutionTime
	@GetMapping("/blackCards")
	public List<BlackCard> getBlackCards() {
		return blackCardService.getBlackCards();
	}
}
