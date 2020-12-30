package it.vitalegi.cocorido.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.vitalegi.cocorido.util.Entry;
import it.vitalegi.cocorido.util.ListUtil;
import it.vitalegi.cocorido.util.ModelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class GetPlayedWhiteCardsServiceTests {

	private static final Long ROUND_ID = 100L;
	private static final List<Long> ORDER = ListUtil.toList(5l, 4l, 1l, 2l, 3l);
	private static final List<Long> VALUE_1 = ListUtil.toList(11L);
	private static final List<Long> VALUE_2 = ListUtil.toList(22L, 23L);
	private static final List<Long> VALUE_3 = ListUtil.toList(31L);
	private static final List<Long> VALUE_4 = null;
	private static final List<Long> VALUE_5 = new ArrayList<>();
	@Autowired
	ModelBuilder models;

	@Test
	public void testOrderIsPreserved() {

		GetPlayedWhiteCardsService service = new GetPlayedWhiteCardsService();
		GetRoundPlayersService getRoundPlayersService = mock(GetRoundPlayersService.class);
		service.setGetRoundPlayersService(getRoundPlayersService);
		when(getRoundPlayersService.getPlayersOrder(anyLong())).thenReturn(ORDER);

		PlayerActionService playerActionService = mock(PlayerActionService.class);
		service.setPlayerActionService(playerActionService);
		when(playerActionService.findPlayerActions(eq(ROUND_ID), eq(1L))).thenReturn(VALUE_1);
		when(playerActionService.findPlayerActions(eq(ROUND_ID), eq(2L))).thenReturn(VALUE_2);
		when(playerActionService.findPlayerActions(eq(ROUND_ID), eq(3L))).thenReturn(VALUE_3);
		when(playerActionService.findPlayerActions(eq(ROUND_ID), eq(4L))).thenReturn(VALUE_4);
		when(playerActionService.findPlayerActions(eq(ROUND_ID), eq(5L))).thenReturn(VALUE_5);

		List<Entry> order = service.getPlayedWhiteCards(ROUND_ID);

		assertEquals(5, order.size());

		assertEquals(entry(1, VALUE_1), order.get(2));
		assertEquals(entry(2, VALUE_2), order.get(3));
		assertEquals(entry(3, VALUE_3), order.get(4));
		assertEquals(entry(4, VALUE_4), order.get(1));
		assertEquals(entry(5, VALUE_5), order.get(0));
	}

	protected Entry entry(long playerId, List<Long> values) {
		Entry entry = new Entry();
		entry.setValues(new HashMap<>());
		entry.getValues().put("playerId", playerId);
		entry.getValues().put("whiteCards", ListUtil.copy(values));
		return entry;
	}
}
