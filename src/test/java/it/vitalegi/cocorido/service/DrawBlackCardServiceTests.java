package it.vitalegi.cocorido.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.vitalegi.cocorido.UnitTest;
import it.vitalegi.cocorido.util.ModelBuilder;
import it.vitalegi.cocorido.util.RandomUtil;

import static it.vitalegi.cocorido.util.ListUtil.list;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DrawBlackCardServiceTests extends UnitTest {

	private static final Long BOARD_ID = 100L;

	@Autowired
	ModelBuilder models;

	@Autowired
	RandomUtil randomUtil;

	@Test
	public void testEnoughCardsInDeck() {
		DrawBlackCardService service = setup(list(1, 2, 3), list(1, 2));

		Long card = service.drawCard(BOARD_ID);

		assertEquals(3, card);
		verify(service.boardPlayedBlackCardService, times(0)).resetCards(BOARD_ID);
		verify(service.boardPlayedBlackCardService, times(1)).addCard(anyLong(), eq(card));
	}

	@Test
	public void testNotEnoughCardsInDeck() {
		DrawBlackCardService service = setup(list(1, 2, 3), list(1, 2, 3));

		Long card = service.drawCard(BOARD_ID);

		verify(service.boardPlayedBlackCardService, times(1)).resetCards(BOARD_ID);
		verify(service.boardPlayedBlackCardService, times(1)).addCard(anyLong(), eq(card));
	}

	protected DrawBlackCardService setup(long[] cards, long[] alreadyDrawnCards) {
		DrawBlackCardService service = new DrawBlackCardService();

		service.blackCardService = mock(BlackCardService.class);
		when(service.blackCardService.getBlackCards()).thenReturn(models.blackCards(cards));

		service.boardPlayedBlackCardService = mock(BoardPlayedBlackCardService.class);
		when(service.boardPlayedBlackCardService.getCards(BOARD_ID))
				.thenReturn(models.boardPlayedBlackCards(BOARD_ID, alreadyDrawnCards));

		service.setRandomUtil(randomUtil);
		return service;
	}
}
