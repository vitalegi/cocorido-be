package it.vitalegi.cocorido.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import it.vitalegi.cocorido.UnitTest;
import it.vitalegi.cocorido.util.ListUtil;
import it.vitalegi.cocorido.util.ModelBuilder;
import it.vitalegi.cocorido.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetRoundPlayersServiceTests extends UnitTest {

	private static final Long ROUND_ID = 150L;
	private static final Long BOARD_ID = 100L;

	private static final List<Long> LIST_1 = ListUtil.toList(1l, 2l, 5l, 3l, 4l);
	@Autowired
	ModelBuilder models;

	@Test
	public void testSyncOrderShouldBeUsed() {
		GetRoundPlayersService service = new GetRoundPlayersService();

		RoundService roundService = mock(RoundService.class);
		service.setRoundService(roundService);
		when(roundService.getRound(anyLong())).thenReturn(models.round(null, null, null, null, 0L, null, LIST_1));

		TablePlayerService tablePlayerService = mock(TablePlayerService.class);
		service.setTablePlayerService(tablePlayerService);
		when(tablePlayerService.getPlayers(anyLong())).thenReturn(models.tablePlayers(BOARD_ID, 1L, 2L, 3L, 4L, 5L));

		List<Long> order = service.getPlayersOrder(ROUND_ID);

		assertEquals(LIST_1, order);

		verify(roundService, times(0)).updateRound(Mockito.any());
	}

	@Test
	public void testUnsynchedOrderShouldBeUpdated() {
		GetRoundPlayersService service = new GetRoundPlayersService();

		RoundService roundService = mock(RoundService.class);
		service.setRoundService(roundService);
		when(roundService.getRound(anyLong())).thenReturn(models.round(null, null, null, null, 0L, null, LIST_1));

		TablePlayerService tablePlayerService = mock(TablePlayerService.class);
		service.setTablePlayerService(tablePlayerService);
		when(tablePlayerService.getPlayers(anyLong())).thenReturn(models.tablePlayers(BOARD_ID, 1L, 2L, 3L, 4L));

		RandomUtil randomUtil = mock(RandomUtil.class);
		service.setRandomUtil(randomUtil);
		when(randomUtil.random(anyInt())).thenReturn(3, 2, 1, 0);

		List<Long> order = service.getPlayersOrder(ROUND_ID);

		assertEquals(ListUtil.toList(4L, 3L, 2L, 1L), order);

		verify(roundService, times(1)).updateRound(any());
		verify(randomUtil, times(4)).random(anyInt());
	}
}
