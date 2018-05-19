package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.*;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.model.*;
import ch.fhnw.edu.wodss.tippspielapi.persistence.*;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

@RunWith(JMockit.class)
public class GameServiceTest {

    @Tested
    GameService gameService;

    @Injectable
    TipService tipService;

    @Injectable
    GameRepository gameRepository;

    @Injectable
    GamePhaseRepository gamePhaseRepository;

    @Injectable
    UserRepository userRepository;

    @Injectable
    LocationRepository locationRepository;

    @Injectable
    NationRepository nationRepository;

    @Test
    public void testGetAllGames_shouldReturnAllGames() {
        Game game1 = new Game();
        Game game2 = new Game();
        List<Game> games = Arrays.asList(game1, game2);

        new Expectations() {{
            gameRepository.findAll();
            result = games;
        }};

        List<Game> response = gameService.getAll();
        Assert.assertArrayEquals(games.toArray(), response.toArray());
    }

    @Test
    public void testGetGamesByUserId_shouldReturnAllTippedGameOfTheUser() {
        Game game = new Game();
        Tip tip = new Tip();
        TippedGame tippedGame = new TippedGame(game, tip);
        List<TippedGame> tippedGames = Arrays.asList(tippedGame);

        new Expectations() {{
            gameRepository.findAllTippedGamesByUserId(1L);
            result = tippedGames;
        }};

        List<TippedGame> response = gameService.getGamesByUserId(1L);
        Assert.assertArrayEquals(tippedGames.toArray(), response.toArray());
    }

    @Test
    public void testGetGamesByUsername_shouldReturnAllTippedGameOfTheUser() {
        User userKen = new User();
        userKen.setId(1L);
        userKen.setUsername("ken");
        Game game = new Game();
        Tip tip = new Tip();
        TippedGame tippedGame = new TippedGame(game, tip);
        List<TippedGame> tippedGames = Arrays.asList(tippedGame);

        new Expectations() {{
            userRepository.findByUsername(userKen.getUsername());
            result = userKen;

            gameRepository.findAllTippedGamesByUserId(userKen.getId());
            result = tippedGames;
        }};

        List<TippedGame> response = gameService.getGamesByUsername(userKen.getUsername());
        Assert.assertArrayEquals(tippedGames.toArray(), response.toArray());
    }

    @Test
    public void testCreate_shouldCreateANewGame() {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1L);

        GamePhaseDto gamePhaseDto = new GamePhaseDto();
        gamePhaseDto.setId(1L);

        NationDto host = new NationDto();
        host.setId("ESP");

        NationDto guest = new NationDto();
        guest.setId("SUI");

        NewGameDto newGameDto = new NewGameDto();
        newGameDto.setLocation(locationDto);
        newGameDto.setPhase(gamePhaseDto);
        newGameDto.setHost(host);
        newGameDto.setGuest(guest);
        newGameDto.setDate(new Date());

        Location location = new Location();
        location.setId(1L);
        GamePhase gamePhase = new GamePhase();
        Nation hostNation = new Nation();
        Nation guestNation = new Nation();

        new Expectations() {{
            locationRepository.findById(locationDto.getId());
            result = Optional.of(location);

            gamePhaseRepository.findById(gamePhaseDto.getId());
            result = Optional.of(gamePhase);

            nationRepository.findById(host.getId());
            result = Optional.of(hostNation);

            nationRepository.findById(guest.getId());
            result = Optional.of(guestNation);
        }};

        Game game = gameService.buildGame(newGameDto);

        Assert.assertEquals(location, game.getLocation());
        Assert.assertEquals(gamePhase, game.getPhase());
        Assert.assertEquals(hostNation, game.getHost());
        Assert.assertEquals(guestNation, game.getGuest());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCreate_shouldFailDueToInvalidLocation() {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1L);

        NewGameDto newGameDto = new NewGameDto();
        newGameDto.setLocation(locationDto);

        new Expectations() {{
            locationRepository.findById(locationDto.getId());
            result = new ResourceNotFoundException("Location", "id", 1L);
        }};

        gameService.buildGame(newGameDto);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCreate_shouldFailDueToInvalidGamePhase() {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1L);

        GamePhaseDto gamePhaseDto = new GamePhaseDto();
        gamePhaseDto.setId(1L);

        NewGameDto newGameDto = new NewGameDto();
        newGameDto.setLocation(locationDto);
        newGameDto.setPhase(gamePhaseDto);

        new Expectations() {{
            locationRepository.findById(locationDto.getId());
            result = new ResourceNotFoundException("Phase", "id", 1L);
        }};

        gameService.buildGame(newGameDto);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCreate_shouldFailDueToInvalidHost() {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1L);

        GamePhaseDto gamePhaseDto = new GamePhaseDto();
        gamePhaseDto.setId(1L);

        NationDto host = new NationDto();
        host.setId("ESP");

        NewGameDto newGameDto = new NewGameDto();
        newGameDto.setLocation(locationDto);
        newGameDto.setPhase(gamePhaseDto);
        newGameDto.setHost(host);

        new Expectations() {{
            locationRepository.findById(locationDto.getId());
            result = new ResourceNotFoundException("Nation", "id", 1L);
        }};

        gameService.buildGame(newGameDto);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCreate_shouldFailDueToInvalidGuest() {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1L);

        GamePhaseDto gamePhaseDto = new GamePhaseDto();
        gamePhaseDto.setId(1L);

        NationDto host = new NationDto();
        host.setId("ESP");

        NationDto guest = new NationDto();
        guest.setId("SUI");

        NewGameDto newGameDto = new NewGameDto();
        newGameDto.setLocation(locationDto);
        newGameDto.setPhase(gamePhaseDto);
        newGameDto.setHost(host);
        newGameDto.setGuest(guest);

        new Expectations() {{
            locationRepository.findById(locationDto.getId());
            result = new ResourceNotFoundException("Nation", "id", 1L);
        }};

        gameService.buildGame(newGameDto);
    }

    @Test
    public void testEnterScore_shouldUpdateTheGame() {
        Long gameId = 1L;

        ScoreDto scoreDto = new ScoreDto();
        scoreDto.setHostScore(2);
        scoreDto.setGuestScore(1);

        Game game = new Game();

        new Expectations() {{
            gameRepository.findById(gameId);
            result = Optional.of(game);

            gameRepository.save(game);
            result = game;
        }};

        Game alteredGame = gameService.enterScore(gameId, scoreDto);

        Assert.assertEquals(alteredGame.getHostScore(), scoreDto.getHostScore());
        Assert.assertEquals(alteredGame.getGuestScore(), scoreDto.getGuestScore());

        new Verifications() {{
            tipService.calculatePointsOfTipsByGame(game);
            times = 1;
        }};
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEnterScore_shouldThrowNotFoundException() {
        Long gameId = 1L;

        ScoreDto scoreDto = new ScoreDto();
        scoreDto.setHostScore(2);
        scoreDto.setGuestScore(1);

        Game game = new Game();

        new Expectations() {{
            gameRepository.findById(gameId);
            result = new ResourceNotFoundException("Game", "id", gameId);
        }};

        Game alteredGame = gameService.enterScore(gameId, scoreDto);

        new Verifications() {{
            tipService.calculatePointsOfTipsByGame(game);
            times = 0;
        }};
    }

}
