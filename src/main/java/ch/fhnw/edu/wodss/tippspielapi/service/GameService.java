package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewGameDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.ScoreDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.exception.ToLateException;
import ch.fhnw.edu.wodss.tippspielapi.model.*;
import ch.fhnw.edu.wodss.tippspielapi.model.builder.TipBuilder;
import ch.fhnw.edu.wodss.tippspielapi.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class GameService {

  @Autowired
  private TipService tipService;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GamePhaseRepository gamePhaseRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TipRepository tipRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private NationRepository nationRepository;

  private TipBuilder tipBuilder;

  public GameService() {
    this.tipBuilder = new TipBuilder();
  }

  public GameService(TipBuilder tipBuilder) {
    this.tipBuilder = tipBuilder;
  }

  public List<TippedGame> getGamesByUserId(Long userId) {
    List<TippedGame> tippedGames = gameRepository.findAllTippedGamesByUserId(userId);
    List<Tip> allTips = tipRepository.findAll();
    tippedGames.stream().forEach(tippedGame -> {
      allTips.stream()
          .filter(tip -> tippedGame.getId().equals(tip.getGame().getId()))
          .forEach(tip -> {
            if (tip.guestWins()) {
              tippedGame.increaseGuestWinsPercentage();
            } else if (tip.hostWins()) {
              tippedGame.increaseHostWinsPercentage();
            } else {
              tippedGame.increaseDrawPercentage();
            }
          });

      int amountOfFilledTips = Math.max((int) allTips.stream().filter(Tip::isFilledTip).count(), 1);
      int hostWinsPercentage = (tippedGame.getHostWinsPercentage() * 100) / amountOfFilledTips;
      tippedGame.setHostWinsPercentage(hostWinsPercentage);
      int guestWinsPercentage = (tippedGame.getGuestWinsPercentage() * 100) / amountOfFilledTips;
      tippedGame.setGuestWinsPercentage(guestWinsPercentage);
      tippedGame.setDrawPercentage(100 - guestWinsPercentage - hostWinsPercentage);
    });
    return tippedGames;
  }

  public List<TippedGame> getGamesByUsername(String username) {
    User user = userRepository.findByUsername(username);
    List<TippedGame> games = gameRepository.findAllTippedGamesByUserId(user.getId());
    return games;
  }

  public List<Game> getAll() {
    return gameRepository.findAll();
  }

  public Game create(NewGameDto newGameDto) {
    Game game = buildGame(newGameDto);
    return gameRepository.save(game);
  }

  Game buildGame(NewGameDto newGameDto) {
    Game game = new Game();
    game.setDate(newGameDto.getDate());

    Location location = locationRepository.findById(newGameDto.getLocation().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Location", "id",
            newGameDto.getLocation().getId()));

    GamePhase gamePhase = gamePhaseRepository.findById(newGameDto.getPhase().getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Phase", "id", newGameDto.getPhase().getId()));

    Nation hostNation = nationRepository.findById(newGameDto.getHost().getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Nation", "id", newGameDto.getHost().getId()));

    Nation guestNation = nationRepository.findById(newGameDto.getGuest().getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Nation", "id", newGameDto.getGuest().getId()));

    game.setLocation(location);
    game.setPhase(gamePhase);
    game.setHost(hostNation);
    game.setGuest(guestNation);
    return game;
  }

  public void delete(Long gameId) {
    this.gameRepository.deleteById(gameId);
  }

  public Game enterScore(Long gameId, ScoreDto scoreDto) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));

    game.setHostScore(scoreDto.getHostScore());
    game.setGuestScore(scoreDto.getGuestScore());

    game = gameRepository.save(game);

    tipService.calculatePointsOfTipsByGame(game);

    return game;
  }

}
