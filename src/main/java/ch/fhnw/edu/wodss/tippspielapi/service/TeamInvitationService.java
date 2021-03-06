package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.PageDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamInvitationDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.NotAllowedException;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamInvitation;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamMate;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamInvitationRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamMateRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamInvitationService {

    @Autowired
    private TeamInvitationRepository teamInvitationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMateRepository teamMateRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TeamService teamService;

    public TeamInvitationDto create(long teamId, String email, User currentUser, Locale locale) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));

        TeamMate teamMate = teamMateRepository.findByUserAndTeam(currentUser,team)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMate", "user", currentUser.getUsername()));

        if (!teamMate.isOwner() || email.equals(currentUser.getEmail())) {
            throw new NotAllowedException();
        }

        TeamInvitation teamInvitation = teamInvitationRepository.findByTeamAndEmail(team, email);
        if (teamInvitation == null) {
            TeamInvitation newTeamInvitation = new TeamInvitation();
            newTeamInvitation.setEmail(email);
            newTeamInvitation.setTeam(team);
            teamInvitation = teamInvitationRepository.save(newTeamInvitation);
        }
        emailService.sendInvitationEmailTo(email, team, locale);
        return new TeamInvitationDto(teamInvitation);

    }

    public PageDto<TeamInvitationDto> getMyInvitations(User currentUser, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TeamInvitation> pageTeamInvitation = teamInvitationRepository.findByEmail(currentUser.getEmail(), pageRequest);
        Page<TeamInvitationDto> pageTeamInvitationDto = pageTeamInvitation.map(TeamInvitationDto::new);
        return new PageDto<>(pageTeamInvitationDto);
    }

    public void delete(long teamInvitationId, User currentUser) {
        TeamInvitation teamInvitation = teamInvitationRepository.findById(teamInvitationId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamInvitation", "id", teamInvitationId));

        Team team = teamInvitation.getTeam();

        TeamMate teamMate = teamMateRepository.findByUserAndTeam(currentUser, team)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMate", "user", currentUser.getUsername()));

        if (!currentUser.getEmail().equals(teamInvitation.getEmail()) && !teamMate.isOwner()) {
            throw new NotAllowedException();
        }

        teamInvitationRepository.delete(teamInvitation);
    }

    public void accept(long teamInvitationId, User currentUser) {
        teamService.checkIfUserIsInMoreThan4Teams(currentUser);

        TeamInvitation teamInvitation = teamInvitationRepository.findById(teamInvitationId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamInvitation", "id", teamInvitationId));

        Team team = teamInvitation.getTeam();

        if (!currentUser.getEmail().equals(teamInvitation.getEmail()) || team == null) {
            throw new NotAllowedException();
        }

        TeamMate teamMate = new TeamMate();
        teamMate.setTeam(team);
        teamMate.setUser(currentUser);
        teamMate.setOwner(false);
        teamMateRepository.save(teamMate);

        teamInvitationRepository.delete(teamInvitation);
    }
}
