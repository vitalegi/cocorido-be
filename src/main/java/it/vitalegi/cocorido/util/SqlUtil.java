package it.vitalegi.cocorido.util;

import java.util.ArrayList;
import java.util.List;

public class SqlUtil {

	public static <E> List<E> convert(Iterable<E> resultSet) {
		List<E> list = new ArrayList<E>();
		resultSet.forEach(element -> list.add(element));
		return list;
	}
}
