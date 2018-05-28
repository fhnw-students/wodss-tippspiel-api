package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewTeamDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.NotAllowedException;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.exception.TeamQuotaIsAchievedException;
import ch.fhnw.edu.wodss.tippspielapi.model.Team;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(JMockit.class)
public class TeamServiceTest {

    @Tested
    TeamService teamService;

    @Injectable
    TeamRepository teamRepository;

    @Injectable
    TeamMateRepository teamMateRepository;

    @Injectable
    TeamInvitationRepository teamInvitationRepository;

    @Test(expected = ResourceNotFoundException.class)
    public void testFindTeamById_shouldThrowAResourceNotFoundExceptionBecauseOfUnknownTeamId() {

        new Expectations() {{
            teamRepository.findById(1L);
            result = new ResourceNotFoundException("Team", "id", 1L);
        }};

        teamService.findById(1L);
    }

    @Test
    public void testFindTeamById_shouldReturnATeamDtoWithCorrectTeamId() {
        Team team = new Team();
        team.setId(1L);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);
        }};

        TeamDto teamDto = teamService.findById(team.getId());
        Assert.assertEquals(team.getId(), teamDto.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateTeam_shouldThrowAResourceNotFoundExceptionBecauseOfUnknownTeamId() {
        Team team = new Team();
        team.setId(1L);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = new ResourceNotFoundException("Team", "id", team.getId());
        }};

        teamService.update(team.getId(), new NewTeamDto(), new User());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateTeam_shouldThrowAResourceNotFoundExceptionBecauseTheCurrentUserIsNotInTheTeam() {
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        user.setUsername("user1");

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user, team);
            result = new ResourceNotFoundException("TeamMate", "user", user.getUsername());
        }};

        teamService.update(team.getId(), new NewTeamDto(), user);
    }

    @Test(expected = NotAllowedException.class)
    public void testUpdateTeam_shouldThrowANotAllowedExceptionBecauseTheCurrentUserIsNotOwner() {
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(false);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user, team);
            result = Optional.of(teamMate);
        }};

        teamService.update(team.getId(), new NewTeamDto(), user);
    }

    @Test
    public void testUpdateTeam_shouldReturnATeamDtoWithTheUpdatedName() {
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(true);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user, team);
            result = Optional.of(teamMate);

            teamRepository.save(team);
            result = team;
        }};

        NewTeamDto newTeamDto = new NewTeamDto();
        newTeamDto.setName("newTeam");

        TeamDto teamDto = teamService.update(team.getId(), newTeamDto, user);

        Assert.assertEquals(newTeamDto.getName(), teamDto.getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteFromTeam_shouldThrowAResourceNotFoundExceptionBecauseOfUnknownTeamId() {
        Team team = new Team();
        team.setId(1L);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = new ResourceNotFoundException("Team", "id", team.getId());
        }};

        teamService.deleteFromTeam(new User().getId(), team.getId(), new User());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteFromTeam_shouldThrowAResourceNotFoundExceptionBecauseTheCurrentUserIsNotInTheTeam() {
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        user.setUsername("user1");

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user, team);
            result = new ResourceNotFoundException("TeamMate", "user", user.getUsername());
        }};

        teamService.deleteFromTeam(user.getId(), team.getId(), user);
    }

    @Test(expected = NotAllowedException.class)
    public void testDeleteFromTeam_shouldThrowANotAllowedExceptionBecauseTheCurrentUserIsNotOwner() {
        Team team = new Team();
        team.setId(1L);

        User deletingUser = new User();
        deletingUser.setId(1L);
        TeamMate deletingTeamMate = new TeamMate();
        deletingTeamMate.setUser(deletingUser);
        deletingTeamMate.setTeam(team);
        deletingTeamMate.setOwner(false);

        User user = new User();
        user.setId(2L);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(deletingUser, team);
            result = Optional.of(deletingTeamMate);
        }};
        teamService.deleteFromTeam(user.getId(), team.getId(), deletingUser);
    }

    @Test(expected = NotAllowedException.class)
    public void testDeleteFromTeam_shouldThrowANotAllowedExceptionBecauseUserToDeleteIsNotCurrentUser() {
        Team team = new Team();
        team.setId(1L);
        User currentUser = new User();
        currentUser.setId(1L);
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(false);
        teamMate.setId(2L);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(teamMate);
        }};

        teamService.deleteFromTeam(teamMate.getId(), team.getId(), currentUser);
    }

    @Test
    public void testDeleteFromTeam_deleteActionOfGroupOwner_teamDeletedBecauseTeamSizeIs1() {
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        user.setId(1L);
        TeamMate owner = new TeamMate();
        owner.setUser(user);
        owner.setOwner(true);
        owner.setTeam(team);

        List<TeamMate> teamMates = new ArrayList<>();
        teamMates.add(owner);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user, team);
            result = Optional.of(owner);
        }};

        teamService.deleteFromTeam(user.getId(), team.getId(), user);

        new Verifications() {{
            teamInvitationRepository.deleteByTeam(team);
            times = 1;

            teamRepository.delete(team);
            times = 1;
        }};
    }

    @Test
    public void testDeleteFromTeam_deleteActionByCurrentUser_teamDeletedBecauseTeamSizeIs1() {
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        user.setId(1L);
        TeamMate owner = new TeamMate();
        owner.setUser(user);
        owner.setOwner(false);
        owner.setTeam(team);

        List<TeamMate> teamMates = new ArrayList<TeamMate>();
        teamMates.add(owner);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user, team);
            result = Optional.of(owner);
        }};

        teamService.deleteFromTeam(user.getId(), team.getId(), user);

        new Verifications() {{
            teamInvitationRepository.deleteByTeam(team);
            times = 1;

            teamRepository.delete(team);
            times = 1;
        }};
    }

    @Test
    public void testDeleteFromTeam_deleteAction_userDeletedByOwner() {
        Team team = new Team();
        team.setId(1L);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("Test1");
        TeamMate owner = new TeamMate();
        owner.setUser(currentUser);
        owner.setOwner(true);

        User deletingUser = new User();
        deletingUser.setId(2L);
        TeamMate deletingTeamMate = new TeamMate();
        deletingTeamMate.setUser(deletingUser);

        List<TeamMate> teamMates = new ArrayList<TeamMate>();
        teamMates.add(owner);
        teamMates.add(deletingTeamMate);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(owner);

            teamMateRepository.findByTeam(team);
            result = teamMates;
        }};

        teamService.deleteFromTeam(deletingTeamMate.getUser().getId(), team.getId(), currentUser);

        new Verifications() {{
            teamMateRepository.findByTeam(team);
            times = 1;

            teamInvitationRepository.deleteByTeam(team);
            times = 0;

            teamRepository.delete(team);
            times = 0;

            teamMateRepository.findAll();
            times = 0;
        }};

    }

    @Test
    public void testDeleteFromTeam_deleteAction_userDeletedByHimself() {
        Team team = new Team();
        team.setId(1L);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("Test1");
        TeamMate deletingTeamMate = new TeamMate();
        deletingTeamMate.setUser(currentUser);
        deletingTeamMate.setOwner(false);

        User additionalUser = new User();
        additionalUser.setId(2L);
        TeamMate owner = new TeamMate();
        owner.setUser(additionalUser);
        owner.setOwner(true);

        List<TeamMate> teamMates = new ArrayList<TeamMate>();
        teamMates.add(owner);
        teamMates.add(deletingTeamMate);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(deletingTeamMate);

            teamMateRepository.findByTeam(team);
            result = teamMates;
        }};

        teamService.deleteFromTeam(deletingTeamMate.getUser().getId(), team.getId(), currentUser);

        new Verifications() {{
            teamMateRepository.findByTeam(team);
            times = 1;

            teamRepository.delete(team);
            times = 0;

            teamMateRepository.findAll();
            times = 0;
        }};

    }

    @Test
    public void testDeleteFromTeam_deleteAction_ownerDeletedByHimself() {
        Long userId = 1L;
        Long teamId = 1L;
        Team team = new Team();
        TeamMate currentTeamMate = new TeamMate();
        User currentUser = new User();

        team.setId(teamId);
        currentUser.setId(userId);
        currentTeamMate.setOwner(true);

        TeamMate newOwner = new TeamMate();
        newOwner.setId(1L);
        List<TeamMate> teamMates = new ArrayList<>();
        teamMates.add(newOwner);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(currentUser, team);
            result = Optional.of(currentTeamMate);

            teamMateRepository.findByTeam(team);
            result = teamMates;
        }};

        teamService.deleteFromTeam(userId, teamId, currentUser);

        new Verifications() {{
            teamMateRepository.deleteByUserIdAndTeam(userId, team);
            times = 1;

            teamMateRepository.save(newOwner);
            times = 1;
        }};

    }

    @Test(expected = TeamQuotaIsAchievedException.class)
    public void testCheckIfUserIsInMoreThan4Teams_ShouldFailDueToReachedTeamQuota() {
        User user = new User();

        new Expectations() {{
            teamMateRepository.countByUser(user);
            result = 5;
        }};

        teamService.checkIfUserIsInMoreThan4Teams(user);
    }

    @Test()
    public void testCheckIfUserIsInMoreThan4Teams_ShouldNotThrowAnException() {
        User user = new User();

        new Expectations() {{
            teamMateRepository.countByUser(user);
            result = 4;
        }};

        teamService.checkIfUserIsInMoreThan4Teams(user);
    }

}
