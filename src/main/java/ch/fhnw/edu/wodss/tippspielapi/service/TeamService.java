package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewTeamDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamMateDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserTeamDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.exception.TeamQuotaIsAchievedException;
import ch.fhnw.edu.wodss.tippspielapi.exception.UniqueTeamNameException;
import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamMate;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamInvitationRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamMateRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamRepository;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.RollbackException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMateRepository teamMateRepository;

    @Autowired
    private TeamInvitationRepository teamInvitationRepository;

    public List<UserTeamDto> getTeamsByUserId(Long userId) {
        return teamRepository.findAllTeamsByUserId(userId);
    }

    public TeamDto findById(Long teamID) {
        Team team = teamRepository.findById(teamID)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamID));
        return new TeamDto(team);
    }

    public List<TeamMateDto> findAllTeamMates(Long teamID) {
        TeamDto teamDto = findById(teamID);
        return teamRepository.findAllTeamMatesByTeamId(teamID);
    }

    public void checkIfUserIsInMoreThan4Teams(User user) {
        int amount = teamMateRepository.countByUser(user);
        if (amount > 4) {
            throw new TeamQuotaIsAchievedException();
        }
    }

    public TeamDto create(NewTeamDto newTeamDto, User user) {
        checkIfUserIsInMoreThan4Teams(user);
        Team team = new Team(newTeamDto);

        try {
            team = teamRepository.save(team);
        } catch(DataIntegrityViolationException e) {
            throw new UniqueTeamNameException();
        }

        TeamMate teamMate = new TeamMate();
        teamMate.setTeam(team);
        teamMate.setUser(user);
        teamMate.setOwner(true);
        teamMateRepository.save(teamMate);

        return new TeamDto(team);
    }

    public TeamDto update(Long teamID, NewTeamDto newTeamDto, User user) {
        Team team = teamRepository.findById(teamID)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamID));

        TeamMate teamMate = teamMateRepository.findByUserAndTeam(user, team)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMate", "user", user.getUsername()));

        if (!teamMate.isOwner()) {
            throw new ch.fhnw.edu.wodss.tippspielapi.exception.NotAllowedException();
        }

        team.setName(newTeamDto.getName());
        team = teamRepository.save(team);
        return new TeamDto(team);

    }

    public void deleteFromTeam(Long userID, Long teamID, User currentUser) {
        Team team = teamRepository.findById(teamID)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamID));

        TeamMate currentTeamMate = teamMateRepository.findByUserAndTeam(currentUser, team)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMate", "user", currentUser.getUsername()));

        if (!currentTeamMate.isOwner() && !currentUser.getId().equals(userID)) {
            throw new ch.fhnw.edu.wodss.tippspielapi.exception.NotAllowedException();
        }

        teamMateRepository.deleteByUserIdAndTeam(userID, team);

        List<TeamMate> teamMates = teamMateRepository.findByTeam(team);
        if (teamMates.isEmpty()) {
            teamInvitationRepository.deleteByTeam(team);
            teamRepository.delete(team);
        } else if (currentUser.getId().equals(userID) && currentTeamMate.isOwner()) {
            TeamMate newOwner = teamMates.get(0);
            newOwner.setOwner(true);
            teamMateRepository.save(newOwner);
        }
    }
}
