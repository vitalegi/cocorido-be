package it.vitalegi.cocorido.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;

import it.vitalegi.cocorido.util.StorageUtil;

public abstract class StorageProxy<E> {

	@Autowired
	StorageUtil storageUtil;

	protected void addValue(E value) {
		List<E> values = getValues();
		values.add(value);
		setValues(values);
	}

	protected void removeValue(E value) {
		List<E> values = getValues();
		values.remove(value);
		setValues(values);
	}

	protected void setValues(List<E> values) {
		storageUtil.setValue(getStorageName(), values);
	}

	protected String getStorageName() {
		return this.getClass().getSimpleName();
	}

	protected abstract List<E> getValues();
}
