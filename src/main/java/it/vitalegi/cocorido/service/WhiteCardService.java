package it.vitalegi.cocorido.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.vitalegi.cocorido.model.WhiteCard;
import it.vitalegi.cocorido.repository.WhiteCardRepository;
import it.vitalegi.cocorido.util.SqlUtil;
import it.vitalegi.cocorido.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhiteCardService {

	@Autowired
	WhiteCardRepository repository;
	@Autowired
	SanitizationService sanitizationService;

	@Transactional
	public WhiteCard addWhiteCard(String text) {
		if (StringUtil.isNullOrEmpty(text)) {
			throw new NullPointerException("Text cannot be empty");
		}
		text = sanitizationService.sanitize(text);

		if (hasWhiteCard(text)) {
			log.error("Duplicated text: {}", text);
			throw new IllegalArgumentException("Duplicate text");
		}
		WhiteCard entry = new WhiteCard();
		entry.setText(text);

		return repository.save(entry);
	}

	public void deleteCards() {
		repository.deleteAll();
	}

	public boolean hasWhiteCard(String text) {
		return repository.findByText(text) != null;
	}

	public WhiteCard getWhiteCard(long whiteCardId) {
		return repository.findById(whiteCardId).get();
	}

	public List<WhiteCard> getWhiteCards() {
		return SqlUtil.convert(repository.findAll());
	}
}
