package it.vitalegi.cocorido.util;

import java.lang.reflect.Array;
import java.util.List;

public class ListUtil {

	public static <E> E[] toArray(Class<E> clazz, List<E> list) {
		@SuppressWarnings("unchecked")
		E[] array = (E[]) Array.newInstance(clazz, list.size());

		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}
}
