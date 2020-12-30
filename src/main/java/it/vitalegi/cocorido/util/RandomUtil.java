package it.vitalegi.cocorido.util;

import org.springframework.stereotype.Service;

@Service
public class RandomUtil {

	public int random(int value) {
		return (int) (value * Math.random());
	}
}
