package it.vitalegi.cocorido.util;

import java.util.List;

import org.opentest4j.AssertionFailedError;

public class AssertUtil {

	public static <E> void assertContains(E expected, List<E> actual) {
		if (actual.stream().noneMatch(v -> v.equals(expected))) {
			throw new AssertionFailedError("Array should contain", expected, actual);
		}
	}

	public static <E> void assertNotContains(E expected, List<E> actual) {
		if (actual.stream().anyMatch(v -> v.equals(expected))) {
			throw new AssertionFailedError("Array should not contain", expected, actual);
		}
	}

}
