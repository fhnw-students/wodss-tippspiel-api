package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewGameDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.ScoreDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.exception.ToLateException;
import ch.fhnw.edu.wodss.tippspielapi.model.*;
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
    private GameRepository gameRepository;

    @Autowired
    private TipRepository tipRepository;

    @Autowired
    private GamePhaseRepository gamePhaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private NationRepository nationRepository;

    public List<TippedGame> getGamesByUserId(Long userId) {
        List<TippedGame> games = gameRepository.findAllTippedGamesByUserId(userId);
        return games;
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

    public Game enterScore(Long gameId, ScoreDto scoreDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));

        if (game.getDate().before(new Date())) {
            throw new ToLateException();
        }

        game.setHostScore(scoreDto.getHostScore());
        game.setGuestScore(scoreDto.getGuestScore());

        game = gameRepository.save(game);

        this.calculatePointsOfTipsByGame(game);

        return game;
    }

    private void calculatePointsOfTipsByGame(Game game) {
        List<Tip> tips = this.tipRepository.findByGameId(game.getId());
        for (Iterator<Tip> i = tips.iterator(); i.hasNext();) {
            Tip tip = i.next();

            // 1. Correct host score
            tip.setTippedHostScoreCorrectly(tip.getHostScore() == game.getHostScore());

            // 2. Correct guest score
            tip.setTippedGuestScoreCorrectly(tip.getGuestScore() == game.getGuestScore());

            // 3. Correct winner
            tip.setTippedWinnerCorrectly(
                    (game.getHostScore() > game.getGuestScore() && tip.getHostScore() > tip.getGuestScore()) ||
                    (game.getHostScore() < game.getGuestScore() && tip.getHostScore() < tip.getGuestScore()) ||
                    (game.getHostScore() == game.getGuestScore() && tip.getHostScore() == tip.getGuestScore())
            );

            // 4. Correct winner & balance
            tip.setTippedBalanceAndWinnerCorrectly(
                    tip.isTippedWinnerCorrectly() && (game.getHostScore() - game.getGuestScore() == tip.getHostScore() - tip.getGuestScore())
            );

            // 5. Sum points for the given tip
            tip.setPoints(
                    (tip.isTippedHostScoreCorrectly() ? 2 : 0) +
                    (tip.isTippedGuestScoreCorrectly() ? 2 : 0) +
                    (tip.isTippedWinnerCorrectly() ? 10 : 0) +
                    (tip.isTippedBalanceAndWinnerCorrectly() ? 6 : 0)
            );

            // Save altered tip
            tipRepository.save(tip);
        }
    }
}
