package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TipDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserGameDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserGameTipDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.GameRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TipRepository tipRepository;

    public List<UserGameDto> getGamesByUserId(Long userId) {
        List<UserGameDto> games = gameRepository.findAllUserGames();
        return games;
    }

    public UserGameDto enterTip(Long gameId, User user, TipDto newTip) {
        Tip tip = tipRepository.findByUserIdAndGameId(user.getId(), gameId);
        if(tip == null){
            tip = new Tip();
            tip.setUser(user);
            tip.setHostScore(newTip.getHostScore());
            tip.setGuestScore(newTip.getGuestScore());
            Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
            tip.setGame(game);
            tip = tipRepository.save(tip);
        } else {
            tip.setHostScore(newTip.getHostScore());
            tip.setGuestScore(newTip.getGuestScore());
            tip = tipRepository.save(tip);
        }

        return gameRepository.findUserGameByTipId(tip.getId());
    }

}
