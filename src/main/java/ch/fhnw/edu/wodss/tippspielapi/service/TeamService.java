package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserTeamDto;
import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<UserTeamDto> getTeamsByUserId(Long userId){
        return teamRepository.findAllTeamsByUserId(userId);
    }
}
