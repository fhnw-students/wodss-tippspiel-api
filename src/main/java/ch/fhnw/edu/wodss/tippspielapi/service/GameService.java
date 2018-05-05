package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.GameDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewGameDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.ScoreDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TipDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.NotAllowedException;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.model.*;
import ch.fhnw.edu.wodss.tippspielapi.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TipRepository tipRepository;

    @Autowired
    private GamePhaseRepository gamePhaseRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private NationRepository nationRepository;

    public List<TippedGame> getGamesByUserId(Long userId) {
        List<TippedGame> games = gameRepository.findAllTippedGamesByUserId(userId);
        return games;
    }

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Game create(NewGameDto newGameDto) {
        Game game = new Game();
        game.setDate(newGameDto.getDate());

        Location location = locationRepository.findById(newGameDto.getLocation().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", newGameDto.getLocation().getId()));

        GamePhase gamePhase = gamePhaseRepository.findById(newGameDto.getPhase().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Phase", "id", newGameDto.getPhase().getId()));

        Nation hostNation = nationRepository.findById(newGameDto.getHost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nation", "id", newGameDto.getHost().getId()));

        Nation guestNation = nationRepository.findById(newGameDto.getGuest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nation", "id", newGameDto.getGuest().getId()));

        game.setLocation(location);
        game.setPhase(gamePhase);
        game.setHost(hostNation);
        game.setGuest(guestNation);

        return gameRepository.save(game);
    }

    public Game enterScore(Long gameId, ScoreDto scoreDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));

        game.setHostScore(scoreDto.getHostScore());
        game.setGuestScore(scoreDto.getGuestScore());

        return gameRepository.save(game);
    }

    public Tip enterTip(Long gameId, User user, ScoreDto scoreDto) {
        Tip tip = tipRepository.findByUserIdAndGameId(user.getId(), gameId);

        if(tip == null){
            tip = new Tip();
            tip.setUser(user);
            tip.setHostScore(scoreDto.getHostScore());
            tip.setGuestScore(scoreDto.getGuestScore());
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));

            tip.setGame(game);
            tip = tipRepository.save(tip);
        } else {
            tip.setHostScore(scoreDto.getHostScore());
            tip.setGuestScore(scoreDto.getGuestScore());
            tip = tipRepository.save(tip);
        }

        return tipRepository.findByUserIdAndGameId(user.getId(), gameId);
    }

    public void delete(Long gameId) {
        this.gameRepository.deleteById(gameId);
    }
}
