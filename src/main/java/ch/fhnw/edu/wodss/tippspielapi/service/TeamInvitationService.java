package ch.fhnw.edu.wodss.tippspielapi.service;

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

    public List<TeamInvitationDto> getMyInvitations(User currentUser) {
        List<TeamInvitation> teamInvitations = teamInvitationRepository.findByEmail(currentUser.getEmail());
        return teamInvitations.stream()
                .map(TeamInvitationDto::new)
                .collect(Collectors.toList());
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
}
