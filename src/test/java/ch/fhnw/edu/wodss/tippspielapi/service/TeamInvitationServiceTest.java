package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamInvitationDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.NotAllowedException;
import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamInvitation;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamMate;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamInvitationRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamMateRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RunWith(JMockit.class)
public class TeamInvitationServiceTest {

    @Tested
    TeamInvitationService teamInvitationService;

    @Injectable
    private TeamInvitationRepository teamInvitationRepository;

    @Injectable
    private TeamRepository teamRepository;

    @Injectable
    private TeamMateRepository teamMateRepository;

    @Injectable
    private EmailService emailService;

    @Injectable
    private TeamService teamService;

    @Test(expected = NotAllowedException.class)
    public void testCreate_shouldFailIfTheCurrentUserIsNotTheOwner() {
        Locale locale = new Locale("DE");
        User currentUser = new User();
        Team team = new Team();
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(false);

        new Expectations() {{
            teamRepository.findById(1L);
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(teamMate);
        }};

        teamInvitationService.create(1L, "", currentUser, locale);
    }

    @Test
    public void testCreate_shouldCreateANewTeamInvitationIfThereIsNone() {
        Locale locale = new Locale("DE");
        String email = "";
        User currentUser = new User();
        Team team = new Team();
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(true);
        TeamInvitation newTeamInvitation = new TeamInvitation();
        newTeamInvitation.setEmail(email);
        newTeamInvitation.setTeam(team);
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId(1L);
        teamInvitation.setEmail(email);
        teamInvitation.setTeam(team);

        new Expectations() {{
            teamRepository.findById(1L);
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(teamMate);

            teamInvitationRepository.findByTeamAndEmail(team, email);
            result = null;

            teamInvitationRepository.save(newTeamInvitation);
            result = teamInvitation;
        }};

        teamInvitationService.create(1L, email, currentUser, locale);

        new Verifications() {{
            teamInvitationRepository.save(newTeamInvitation);
            times = 1;

            emailService.sendInvitationEmailTo(email, team, locale);
            times = 1;
        }};

    }

    @Test
    public void testCreate_shouldSendEmailEvenThereIsAInvitation() {
        Locale locale = new Locale("DE");
        String email = "";
        User currentUser = new User();
        Team team = new Team();
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(true);
        TeamInvitation newTeamInvitation = new TeamInvitation();
        newTeamInvitation.setEmail(email);
        newTeamInvitation.setTeam(team);
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId(1L);
        teamInvitation.setEmail(email);
        teamInvitation.setTeam(team);

        new Expectations() {{
            teamRepository.findById(1L);
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(teamMate);

            teamInvitationRepository.findByTeamAndEmail(team, email);
            result = teamInvitation;
        }};

        teamInvitationService.create(1L, email, currentUser, locale);

        new Verifications() {{
            teamInvitationRepository.save(newTeamInvitation);
            times = 0;

            emailService.sendInvitationEmailTo(email, team, locale);
            times = 1;
        }};
    }

    @Test
    public void testGetMyInvitations_ShouldReturnAListOfInvitations() {
        User currentUser = new User();
        currentUser.setEmail("example@email.io");
        Team team = new Team();

        TeamInvitation teamInvitationA = new TeamInvitation();
        teamInvitationA.setId(1L);
        teamInvitationA.setEmail("");
        teamInvitationA.setTeam(team);
        TeamInvitation teamInvitationB = new TeamInvitation();
        teamInvitationB.setId(2L);
        teamInvitationB.setEmail("");
        teamInvitationB.setTeam(team);
        List<TeamInvitation> teamInvitations = Arrays.asList(teamInvitationA, teamInvitationB);

        new Expectations() {{
            teamInvitationRepository.findByEmail(currentUser.getEmail());
            result = teamInvitations;
        }};

        List<TeamInvitationDto> teamInvitationDtos = teamInvitationService.getMyInvitations(currentUser);

        Assert.assertEquals(teamInvitations.size(), teamInvitationDtos.size());
    }

    @Test(expected = NotAllowedException.class)
    public void testDelete_ShouldFailIfTheCurrentUserIsNotTheOwnerAndTheEmailsAreNotEqual() {
        String email = "example@email.io";
        User currentUser = new User();
        currentUser.setEmail(email);
        Team team = new Team();
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId(1L);
        teamInvitation.setEmail("");
        teamInvitation.setTeam(team);
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(false);

        new Expectations() {{
            teamInvitationRepository.findById(1L);
            result = Optional.of(teamInvitation);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(teamMate);
        }};

        teamInvitationService.delete(teamInvitation.getId(), currentUser);

        new Verifications() {{
            teamInvitationRepository.delete(teamInvitation);
            times = 0;
        }};
    }

    @Test
    public void testDelete_ShouldDeleteIfCurrentUserIsOwner() {
        String email = "example@email.io";
        User currentUser = new User();
        currentUser.setEmail(email);
        Team team = new Team();
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId(1L);
        teamInvitation.setEmail("");
        teamInvitation.setTeam(team);
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(true);

        new Expectations() {{
            teamInvitationRepository.findById(1L);
            result = Optional.of(teamInvitation);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(teamMate);
        }};

        teamInvitationService.delete(teamInvitation.getId(), currentUser);

        new Verifications() {{
            teamInvitationRepository.delete(teamInvitation);
            times = 1;
        }};
    }

    @Test
    public void testDelete_ShouldDeleteIfEmailsAreEqual() {
        String email = "example@email.io";
        User currentUser = new User();
        currentUser.setEmail(email);
        Team team = new Team();
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId(1L);
        teamInvitation.setEmail(email);
        teamInvitation.setTeam(team);
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(false);

        new Expectations() {{
            teamInvitationRepository.findById(1L);
            result = Optional.of(teamInvitation);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(teamMate);
        }};

        teamInvitationService.delete(teamInvitation.getId(), currentUser);

        new Verifications() {{
            teamInvitationRepository.delete(teamInvitation);
            times = 1;
        }};
    }

    @Test(expected = NotAllowedException.class)
    public void testAccept_ShouldFailIfTeamIsNull() {
        String email = "example@email.io";
        User currentUser = new User();
        currentUser.setEmail(email);
        Team team = new Team();
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId(1L);
        teamInvitation.setEmail(email);

        TeamMate teamMate = new TeamMate();
        teamMate.setTeam(team);
        teamMate.setUser(currentUser);
        teamMate.setOwner(false);

        new Expectations() {{
            teamInvitationRepository.findById(1L);
            result = Optional.of(teamInvitation);
        }};

        teamInvitationService.accept(teamInvitation.getId(), currentUser);
    }

    @Test(expected = NotAllowedException.class)
    public void testAccept_ShouldFailIfInvitationIsNotFromTheCurrentUser() {
        String email = "example@email.io";
        String email2 = "example-2@email.io";
        User currentUser = new User();
        currentUser.setEmail(email2);
        Team team = new Team();
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId(1L);
        teamInvitation.setEmail(email);
        teamInvitation.setTeam(team);

        TeamMate teamMate = new TeamMate();
        teamMate.setTeam(team);
        teamMate.setUser(currentUser);
        teamMate.setOwner(false);

        new Expectations() {{
            teamInvitationRepository.findById(1L);
            result = Optional.of(teamInvitation);
        }};

        teamInvitationService.accept(teamInvitation.getId(), currentUser);
    }

    @Test
    public void testAccept_ShouldSaveTeamMateAndDeleteInvitation() {
        String email = "example@email.io";
        User currentUser = new User();
        currentUser.setEmail(email);
        Team team = new Team();
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId(1L);
        teamInvitation.setEmail(email);
        teamInvitation.setTeam(team);

        TeamMate teamMate = new TeamMate();
        teamMate.setTeam(team);
        teamMate.setUser(currentUser);
        teamMate.setOwner(false);

        new Expectations() {{
            teamInvitationRepository.findById(1L);
            result = Optional.of(teamInvitation);

            teamMateRepository.save(teamMate);

            teamInvitationRepository.delete(teamInvitation);
        }};

        teamInvitationService.accept(teamInvitation.getId(), currentUser);

        new Verifications() {{
            teamMateRepository.save(teamMate);
            times = 1;

            teamInvitationRepository.delete(teamInvitation);
            times = 1;
        }};
    }
}
