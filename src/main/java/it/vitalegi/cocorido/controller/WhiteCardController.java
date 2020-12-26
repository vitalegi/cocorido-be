package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.model.WhiteCard;
import it.vitalegi.cocorido.service.WhiteCardService;
import it.vitalegi.cocorido.util.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
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

	@LogExecutionTime
	@GetMapping("/whiteCards")
	public List<WhiteCard> getWhiteCards() {
		return whiteCardService.getWhiteCards();
	}

	@LogExecutionTime
	@DeleteMapping("/whiteCards")
	public void deleteWhiteCards() {
		whiteCardService.deleteCards();
	}

	@LogExecutionTime
	@GetMapping("/whiteCards/txt")
	public String getWhiteCardsExport() {
		List<WhiteCard> cards = whiteCardService.getWhiteCards();
		StringBuffer sb = new StringBuffer();
		cards.forEach(card -> {
			sb.append(card.getText()).append("\n");
		});
		return sb.toString();
	}
}
