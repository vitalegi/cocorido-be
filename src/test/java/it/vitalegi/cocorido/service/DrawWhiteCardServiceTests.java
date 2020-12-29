package it.vitalegi.cocorido.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import it.vitalegi.cocorido.UnitTest;
import it.vitalegi.cocorido.util.AssertUtil;
import it.vitalegi.cocorido.util.ModelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestPropertySource(properties = { "game.config.cards-in-hand=2", })
public class DrawWhiteCardServiceTests extends UnitTest {

	private static final Long BOARD_ID = 100L;
	private static final Long PLAYER_ID = 200L;

	@Autowired
	ModelBuilder models;

	@Test
	public void testEnoughCardsInDeck() {
		DrawWhiteCardService service = setup(list(1, 2, 3, 4, 5), list(1, 2, 3), list(1, 2));

		List<Long> cards = service.getNewWhiteCards(BOARD_ID, 2);

		assertEquals(2, cards.size());
		AssertUtil.assertContains(4l, cards);
		AssertUtil.assertContains(5l, cards);

		verify(service.boardPlayedWhiteCardService, times(1)).addCard(BOARD_ID, 4);
		verify(service.boardPlayedWhiteCardService, times(1)).addCard(BOARD_ID, 5);
		verify(service.boardPlayedWhiteCardService, times(0)).resetCards(BOARD_ID);
		verify(service.boardPlayedWhiteCardService, times(0)).addCards(anyLong(), anyList());
	}

	@Test
	public void testNotEnoughCardsInDeckEnoughInHand() {
		DrawWhiteCardService service = setup(list(1, 2, 3, 4, 5), list(1, 2, 3, 4), list(1, 2));

		List<Long> cards = service.getNewWhiteCards(BOARD_ID, 2);

		assertEquals(2, cards.size());
		AssertUtil.assertNotContains(1l, cards);
		AssertUtil.assertNotContains(2l, cards);

		verify(service.boardPlayedWhiteCardService, times(1)).resetCards(BOARD_ID);
		verify(service.boardPlayedWhiteCardService, times(1)).addCards(anyLong(), anyList());
	}

	protected DrawWhiteCardService setup(long[] cards, long[] alreadyDrawnCards, long[] cardsInGame) {
		DrawWhiteCardService service = new DrawWhiteCardService();

		service.whiteCardService = mock(WhiteCardService.class);
		when(service.whiteCardService.getWhiteCards()).thenReturn(models.whiteCards(cards));

		service.boardPlayedWhiteCardService = mock(BoardPlayedWhiteCardService.class);
		when(service.boardPlayedWhiteCardService.getCards(BOARD_ID))
				.thenReturn(models.boardPlayedWhiteCards(BOARD_ID, alreadyDrawnCards));

		service.tablePlayerWhiteCardService = mock(TablePlayerWhiteCardService.class);
		when(service.tablePlayerWhiteCardService.getAll(BOARD_ID))
				.thenReturn(models.tablePlayerWhiteCards(BOARD_ID, PLAYER_ID, cardsInGame));

		return service;
	}

	protected long[] list(long... values) {
		long[] out = new long[values.length];
		for (int i = 0; i < values.length; i++) {
			out[i] = values[i];
		}
		return out;
	}
}
