package it.vitalegi.cocorido.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.model.BoardPlayedBlackCard;
import it.vitalegi.cocorido.repository.BlackCardRepository;
import it.vitalegi.cocorido.util.RandomUtil;
import it.vitalegi.cocorido.util.SqlUtil;
import it.vitalegi.cocorido.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BlackCardService {

	final String PLACEHOLDER = "__";
	final String REPLACE_PLACEHOLDER = "___";

	@Autowired
	private BlackCardRepository repository;

	@Autowired
	private SanitizationService sanitizationService;

	@Transactional
	public BlackCard addBlackCard(String text) {
		if (StringUtil.isNullOrEmpty(text)) {
			throw new NullPointerException("Text cannot be empty");
		}
		text = sanitizationService.sanitize(text);
		text = preprocessText(text);
		int whitecards = countWhiteCards(text);
		if (whitecards == 0) {
			throw new IllegalArgumentException("No whitecards present");
		}
		if (hasBlackCard(text)) {
			log.error("Duplicated text: {}", text);
			throw new IllegalArgumentException("Duplicate text");
		}
		BlackCard entry = new BlackCard();
		entry.setText(text);
		entry.setWhitecards(whitecards);

		return repository.save(entry);
	}

	public void deleteCards() {
		repository.deleteAll();
	}

	public BlackCard getBlackCard(long blackCardId) {
		return repository.findById(blackCardId).get();
	}

	public List<BlackCard> getBlackCards() {
		return SqlUtil.convert(repository.findAll());
	}

	public boolean hasBlackCard(String text) {
		return repository.findByText(text) != null;
	}

	protected int countWhiteCards(String text) {
		int count = 0;
		int lastIndex = 0;
		while (lastIndex != -1) {
			lastIndex = text.indexOf(PLACEHOLDER, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += PLACEHOLDER.length();
			}
		}
		return count;
	}

	protected String preprocessText(String text) {
		while (text.contains(REPLACE_PLACEHOLDER)) {
			text = text.replaceAll(REPLACE_PLACEHOLDER, PLACEHOLDER);
		}
		return text;
	}
}
