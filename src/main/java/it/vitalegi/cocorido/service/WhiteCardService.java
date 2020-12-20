package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import it.vitalegi.cocorido.model.WhiteCard;
import it.vitalegi.cocorido.util.StringUtil;

@Service
public class WhiteCardService extends StorageProxy<WhiteCard> {

	public WhiteCard addWhiteCard(String text) {
		if (StringUtil.isNullOrEmpty(text)) {
			throw new NullPointerException("Text cannot be empty");
		}

		List<WhiteCard> values = getValues();

		WhiteCard entry = new WhiteCard();
		entry.setId(nextId(values));
		entry.setText(text);
		
		addValue(entry);

		return entry;
	}

	public WhiteCard getWhiteCard(long whiteCardId) {
		List<WhiteCard> values = getValues();
		return values.stream().filter(t -> t.getId() == whiteCardId) //
				.findFirst().orElseThrow(() -> new RuntimeException("card " + whiteCardId + " doesn't exist"));
	}

	public List<WhiteCard> getWhiteCards() {
		return getValues();
	}

	protected long nextId(List<WhiteCard> values) {
		return 1 + values.stream().mapToLong(WhiteCard::getId).max().orElse(10000);
	}

	protected List<WhiteCard> getValues() {
		return storageUtil.getValue(getStorageName(), new TypeReference<List<WhiteCard>>() {
		}, new ArrayList<WhiteCard>());
	}
}
