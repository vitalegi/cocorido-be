package it.vitalegi.cocorido.util;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Entry {
	Map<String, Object> values;

	@JsonAnyGetter
	public Map<String, Object> getValues() {
		return values;
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}

}
