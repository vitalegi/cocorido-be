package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import it.vitalegi.cocorido.model.BlackCard;
import it.vitalegi.cocorido.util.RandomUtil;
import it.vitalegi.cocorido.util.StringUtil;

@Service
public class BlackCardService extends StorageProxy<BlackCard> {

	final String PLACEHOLDER = "__";
	final String REPLACE_PLACEHOLDER = "___";

	public BlackCard addBlackCard(String text) {
		if (StringUtil.isNullOrEmpty(text)) {
			throw new NullPointerException("Text cannot be empty");
		}

		List<BlackCard> values = getValues();

		text = preprocessText(text);
		BlackCard entry = new BlackCard();
		entry.setId(nextId(values));
		entry.setText(text);
		entry.setWhitecards(getRequestedWhiteCards(text));

		addValue(entry);

		return entry;
	}

	public BlackCard getBlackCard(long blackCardId) {
		List<BlackCard> values = getValues();
		return values.stream().filter(t -> t.getId() == blackCardId) //
				.findFirst().orElseThrow(() -> new RuntimeException("card " + blackCardId + " doesn't exist"));
	}

	public BlackCard getRandomBlackCard() {
		List<BlackCard> values = getValues();
		if (values.isEmpty()) {
			throw new NullPointerException("Missing configuration, no blackcards provided");
		}
		return values.get(RandomUtil.random(values.size()));
	}

	public List<BlackCard> getBlackCards() {
		return getValues();
	}

	protected int getRequestedWhiteCards(String text) {
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

	protected long nextId(List<BlackCard> values) {
		return 1 + values.stream().mapToLong(BlackCard::getId).max().orElse(10000);
	}

	protected List<BlackCard> getValues() {
		return storageUtil.getValue(getStorageName(), new TypeReference<List<BlackCard>>() {
		}, new ArrayList<BlackCard>());
	}
}
