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
public class TipService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TipRepository tipRepository;

    private TipBuilder tipBuilder;

    public TipService() {
        this.tipBuilder = new TipBuilder();
    }

    public TipService(TipBuilder tipBuilder, GameRepository gameRepository, TipRepository tipRepository) {
        this.tipBuilder = tipBuilder;
        this.gameRepository = gameRepository;
        this.tipRepository = tipRepository;
    }

    public Tip enterTip(Long gameId, User user, ScoreDto scoreDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));

        if (game.getDate().before(new Date())) {
            throw new ToLateException();
        }

        Tip tip = tipRepository.findByUserIdAndGameId(user.getId(), gameId);
        if (tip == null) {
            tip = tipBuilder.withUser(user).withGame(game).build();
        }

        tip.setHostScore(scoreDto.getHostScore());
        tip.setGuestScore(scoreDto.getGuestScore());
        tip = tipRepository.save(tip);

        return tipRepository.findByUserIdAndGameId(user.getId(), gameId);
    }

    public void calculatePointsOfTipsByGame(Game game) {
        List<Tip> tips = tipRepository.findByGameId(game.getId());
        for (Iterator<Tip> i = tips.iterator(); i.hasNext(); ) {
            Tip tip = i.next();
            tipRepository.save(calculatePointsOfTip(tip, game));
        }
    }

    Tip calculatePointsOfTip(Tip tip, Game game) {
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

        return tip;
    }
}
