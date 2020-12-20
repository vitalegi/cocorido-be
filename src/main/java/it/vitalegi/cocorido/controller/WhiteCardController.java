package it.vitalegi.cocorido.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.WhiteCard;
import it.vitalegi.cocorido.service.WhiteCardService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
public class WhiteCardController {

	@Autowired
	WhiteCardService whiteCardService;

	@LogExecutionTime
	@PutMapping("/whiteCard")
	public WhiteCard addWhiteCard(@RequestBody WhiteCard card) {
		return whiteCardService.addWhiteCard(card.getText());
	}

	@LogExecutionTime
	@GetMapping("/whiteCard/{whiteCardId}")
	public WhiteCard getWhiteCard(@PathVariable long whiteCardId) {
		return whiteCardService.getWhiteCard(whiteCardId);
	}
}
