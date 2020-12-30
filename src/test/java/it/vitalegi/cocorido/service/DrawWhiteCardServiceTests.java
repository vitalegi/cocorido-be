package it.vitalegi.cocorido.service;

import static it.vitalegi.cocorido.util.ListUtil.list;
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
import it.vitalegi.cocorido.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestPropertySource(properties = { "game.config.cards-in-hand=2", })
public class DrawWhiteCardServiceTests extends UnitTest {

	private static final Long BOARD_ID = 100L;
	private static final Long PLAYER_ID = 200L;

	@Autowired
	ModelBuilder models;

	@Autowired
	RandomUtil randomUtil;

	@Test
	public void testEnoughCardsInDeck() {
		DrawWhiteCardService service = setup(list(1, 2, 3, 4, 5), list(1, 2, 3), list(1, 2));

		List<Long> cards = service.getNewWhiteCards(BOARD_ID, 2);

		assertEquals(2, cards.size());
		AssertUtil.assertContains(4l, cards);
		AssertUtil.assertContains(5l, cards);

		verify(service.getBoardPlayedWhiteCardService(), times(1)).addCard(BOARD_ID, 4);
		verify(service.getBoardPlayedWhiteCardService(), times(1)).addCard(BOARD_ID, 5);
		verify(service.getBoardPlayedWhiteCardService(), times(0)).resetCards(BOARD_ID);
		verify(service.getBoardPlayedWhiteCardService(), times(0)).addCards(anyLong(), anyList());
	}

	@Test
	public void testNotEnoughCardsInDeckEnoughInHand() {
		DrawWhiteCardService service = setup(list(1, 2, 3, 4, 5), list(1, 2, 3, 4), list(1, 2));

		List<Long> cards = service.getNewWhiteCards(BOARD_ID, 2);

		assertEquals(2, cards.size());
		AssertUtil.assertNotContains(1l, cards);
		AssertUtil.assertNotContains(2l, cards);

		verify(service.getBoardPlayedWhiteCardService(), times(1)).resetCards(BOARD_ID);
		verify(service.getBoardPlayedWhiteCardService(), times(1)).addCards(anyLong(), anyList());
	}

	protected DrawWhiteCardService setup(long[] cards, long[] alreadyDrawnCards, long[] cardsInGame) {
		DrawWhiteCardService service = new DrawWhiteCardService();

		service.setWhiteCardService(mock(WhiteCardService.class));
		when(service.getWhiteCardService().getWhiteCards()).thenReturn(models.whiteCards(cards));

		service.setBoardPlayedWhiteCardService(mock(BoardPlayedWhiteCardService.class));
		when(service.getBoardPlayedWhiteCardService().getCards(BOARD_ID))
				.thenReturn(models.boardPlayedWhiteCards(BOARD_ID, alreadyDrawnCards));

		service.setTablePlayerWhiteCardService(mock(TablePlayerWhiteCardService.class));
		when(service.getTablePlayerWhiteCardService().getAll(BOARD_ID))
				.thenReturn(models.tablePlayerWhiteCards(BOARD_ID, PLAYER_ID, cardsInGame));

		service.setRandomUtil(randomUtil);

		return service;
	}
}
