package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.*;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.exception.ToLateException;
import ch.fhnw.edu.wodss.tippspielapi.model.*;
import ch.fhnw.edu.wodss.tippspielapi.model.builder.TipBuilder;
import ch.fhnw.edu.wodss.tippspielapi.persistence.*;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

@RunWith(JMockit.class)
public class TipServiceTest {

    @Tested
    TipService tipService;

    @Injectable
    GameRepository gameRepository;

    @Injectable
    TipRepository tipRepository;

    @Test
    public void testEnterTip_shouldUpdateTheTip() {
        Long gameId = 1L;
        User user = new User();
        user.setId(1L);
        ScoreDto scoreDto = new ScoreDto();
        scoreDto.setHostScore(2);
        scoreDto.setGuestScore(1);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date futureDate = calendar.getTime();

        Game game = new Game();
        game.setDate(futureDate);

        Tip tip = new Tip();

        new Expectations() {{
            tipRepository.findByUserIdAndGameId(user.getId(), gameId);
            result = tip;

            gameRepository.findById(1L);
            result = Optional.of(game);
        }};

        Tip response = tipService.enterTip(gameId, user, scoreDto);
        Assert.assertEquals(scoreDto.getHostScore(), response.getHostScore());
        Assert.assertEquals(scoreDto.getGuestScore(), response.getGuestScore());

        new Verifications() {{
            tipRepository.findByUserIdAndGameId(user.getId(), gameId);
            times = 2;
        }};

    }

    @Test(expected = ToLateException.class)
    public void testEnterTip_shouldThrowATooLateException() {
        Long gameId = 1L;
        User user = new User();
        user.setId(1L);
        ScoreDto scoreDto = new ScoreDto();
        scoreDto.setHostScore(2);
        scoreDto.setGuestScore(1);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        Game game = new Game();
        game.setDate(date);

        Tip tip = new Tip();

        new Expectations() {{
            gameRepository.findById(gameId);
            result = Optional.of(game);
        }};

        Tip response = tipService.enterTip(gameId, user, scoreDto);

        new Verifications() {{
            tipRepository.findByUserIdAndGameId(user.getId(), gameId);
            times = 1;
        }};
    }

    @Test
    public void testEnterTip_shouldCreateANewTip(@Mocked GameRepository gameRepository, @Mocked TipRepository tipRepository) {
        Long gameId = 1L;
        User user = new User();
        user.setId(1L);
        ScoreDto scoreDto = new ScoreDto();
        scoreDto.setHostScore(2);
        scoreDto.setGuestScore(1);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date futureDate = calendar.getTime();

        Game game = new Game();
        game.setDate(futureDate);

        Tip tip = new Tip();
        new Expectations() {{
            gameRepository.findById(1L);
            result = Optional.of(game);

            tipRepository.findByUserIdAndGameId(user.getId(), gameId);
            returns(null, tip);
        }};

        TipBuilder tipBuilder = new TipBuilder() {
            @Override
            public Tip build() {
                return tip;
            }
        };

        TipService tipServiceTest = new TipService(tipBuilder, gameRepository, tipRepository);
        Tip response = tipServiceTest.enterTip(gameId, user, scoreDto);

        Assert.assertEquals(scoreDto.getHostScore(), response.getHostScore());
        Assert.assertEquals(scoreDto.getGuestScore(), response.getGuestScore());

        new Verifications() {{
            tipRepository.findByUserIdAndGameId(user.getId(), gameId);
            times = 2;
        }};

    }

    @Test
    public void testCalculatePointsOfTip_shouldGet20PointsForACorrectResult() {
        Game game = new Game();
        Tip tip = new Tip();

        game.setHostScore(2);
        game.setGuestScore(1);

        tip.setHostScore(2);
        tip.setGuestScore(1);

        tip = tipService.calculatePointsOfTip(tip, game);

        Assert.assertEquals(new Integer(20), tip.getPoints());
        Assert.assertEquals(true, tip.isTippedHostScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedGuestScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedWinnerCorrectly());
        Assert.assertEquals(true, tip.isTippedBalanceAndWinnerCorrectly());
    }

    @Test
    public void testCalculatePointsOfTip_shouldGet16PointsForTheCorrectWinnerPlusBalance() {
        Game game = new Game();
        Tip tip = new Tip();

        game.setHostScore(2);
        game.setGuestScore(1);

        tip.setHostScore(3);
        tip.setGuestScore(2);

        tip = tipService.calculatePointsOfTip(tip, game);

        Assert.assertEquals(new Integer(16), tip.getPoints());
        Assert.assertEquals(false, tip.isTippedHostScoreCorrectly());
        Assert.assertEquals(false, tip.isTippedGuestScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedWinnerCorrectly());
        Assert.assertEquals(true, tip.isTippedBalanceAndWinnerCorrectly());
    }

    @Test
    public void testCalculatePointsOfTip_shouldGet10PointsForTheCorrectWinner() {
        Game game = new Game();
        Tip tip = new Tip();

        game.setHostScore(2);
        game.setGuestScore(1);

        tip.setHostScore(3);
        tip.setGuestScore(0);

        tip = tipService.calculatePointsOfTip(tip, game);

        Assert.assertEquals(new Integer(10), tip.getPoints());
        Assert.assertEquals(false, tip.isTippedHostScoreCorrectly());
        Assert.assertEquals(false, tip.isTippedGuestScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedWinnerCorrectly());
        Assert.assertEquals(false, tip.isTippedBalanceAndWinnerCorrectly());
    }

    @Test
    public void testCalculatePointsOfTip_shouldGet12PointsForTheCorrectWinnerAndHostScore() {
        Game game = new Game();
        Tip tip = new Tip();

        game.setHostScore(2);
        game.setGuestScore(1);

        tip.setHostScore(2);
        tip.setGuestScore(0);

        tip = tipService.calculatePointsOfTip(tip, game);

        Assert.assertEquals(new Integer(12), tip.getPoints());
        Assert.assertEquals(true, tip.isTippedHostScoreCorrectly());
        Assert.assertEquals(false, tip.isTippedGuestScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedWinnerCorrectly());
        Assert.assertEquals(false, tip.isTippedBalanceAndWinnerCorrectly());
    }

    @Test
    public void testCalculatePointsOfTip_shouldGet12PointsForTheCorrectWinnerAndGuestScore() {
        Game game = new Game();
        Tip tip = new Tip();

        game.setHostScore(2);
        game.setGuestScore(1);

        tip.setHostScore(4);
        tip.setGuestScore(1);

        tip = tipService.calculatePointsOfTip(tip, game);

        Assert.assertEquals(new Integer(12), tip.getPoints());
        Assert.assertEquals(false, tip.isTippedHostScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedGuestScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedWinnerCorrectly());
        Assert.assertEquals(false, tip.isTippedBalanceAndWinnerCorrectly());
    }

    @Test
    public void testCalculatePointsOfTip_shouldGet2PointsForHostScore() {
        Game game = new Game();
        Tip tip = new Tip();

        game.setHostScore(2);
        game.setGuestScore(1);

        tip.setHostScore(2);
        tip.setGuestScore(4);

        tip = tipService.calculatePointsOfTip(tip, game);

        Assert.assertEquals(new Integer(2), tip.getPoints());
        Assert.assertEquals(true, tip.isTippedHostScoreCorrectly());
        Assert.assertEquals(false, tip.isTippedGuestScoreCorrectly());
        Assert.assertEquals(false, tip.isTippedWinnerCorrectly());
        Assert.assertEquals(false, tip.isTippedBalanceAndWinnerCorrectly());
    }

    @Test
    public void testCalculatePointsOfTip_shouldGet2PointsForGuestScore() {
        Game game = new Game();
        Tip tip = new Tip();

        game.setHostScore(2);
        game.setGuestScore(1);

        tip.setHostScore(0);
        tip.setGuestScore(1);

        tip = tipService.calculatePointsOfTip(tip, game);

        Assert.assertEquals(new Integer(2), tip.getPoints());
        Assert.assertEquals(false, tip.isTippedHostScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedGuestScoreCorrectly());
        Assert.assertEquals(false, tip.isTippedWinnerCorrectly());
        Assert.assertEquals(false, tip.isTippedBalanceAndWinnerCorrectly());
    }

    @Test
    public void testCalculatePointsOfTip_shouldGet16ForADraw() {
        Game game = new Game();
        Tip tip = new Tip();

        game.setHostScore(1);
        game.setGuestScore(1);

        tip.setHostScore(2);
        tip.setGuestScore(2);

        tip = tipService.calculatePointsOfTip(tip, game);

        Assert.assertEquals(new Integer(16), tip.getPoints());
        Assert.assertEquals(false, tip.isTippedHostScoreCorrectly());
        Assert.assertEquals(false, tip.isTippedGuestScoreCorrectly());
        Assert.assertEquals(true, tip.isTippedWinnerCorrectly());
        Assert.assertEquals(true, tip.isTippedBalanceAndWinnerCorrectly());
    }

}
