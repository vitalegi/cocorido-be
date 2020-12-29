package it.vitalegi.cocorido.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtil {

	@SuppressWarnings("unchecked")
	public static <E> E[] toArray(Class<E> clazz, List<E> list) {
		if (list == null) {
			return (E[]) Array.newInstance(clazz, 0);
		}
		E[] array = (E[]) Array.newInstance(clazz, list.size());

		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static <E> List<E> toList(E... array) {
		if (array == null) {
			return null;
		}
		List<E> list = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	public static <E> List<E> diff(List<E> list, List<E> less) {
		return list.stream() //
				.filter(value -> !contains(less, value)) //
				.collect(Collectors.toList());
	}

	public static <E> boolean contains(List<E> list, E value) {
		return list.stream().anyMatch(element -> element.equals(value));
	}

	public static <E> List<E> copy(List<E> list, Function<E, E> copyValue) {
		if (list == null) {
			return null;
		}
		List<E> copy = new ArrayList<>();
		for (E e : list) {
			copy.add(copyValue.apply(e));
		}
		return copy;
	}

	public static List<Long> copy(List<Long> list) {
		Function<Long, Long> fn = (value) -> {
			if (value == null) {
				return null;
			}
			return value.longValue();
		};
		return copy(list, fn);
	}
}
