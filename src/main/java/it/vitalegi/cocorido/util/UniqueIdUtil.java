package it.vitalegi.cocorido.util;

public class UniqueIdUtil {

	private static long ID = 100000;

	public static String getId() {
		return "" + nextId();
	}

	public synchronized static long nextId() {
		return ID++;
	}
}
