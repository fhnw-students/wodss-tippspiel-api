package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewTeamDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamDto;
import ch.fhnw.edu.wodss.tippspielapi.exception.ResourceNotFoundException;
import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamMate;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamMateRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TeamRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Test(expected = ResourceNotFoundException.class)
    public void testFindTeamById_shouldThrowAResourceNotFoundExceptionBecauseOfUnknownTeamId(){

        new Expectations() {{
            teamRepository.findById(1L);
            result = new ResourceNotFoundException("Team", "id", 1L);
        }};

        teamService.findById(1L);
    }

    @Test
    public void testFindTeamById_shouldReturnATeamDtoWithCorrectTeamId(){
        Team team = new Team();
        team.setId(1L);

        new Expectations(){{
            teamRepository.findById(team.getId());
            result = Optional.of(team);
        }};

        TeamDto teamDto = teamService.findById(team.getId());
        Assert.assertEquals(team.getId(), teamDto.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateTeam_shouldThrowAResourceNotFoundExceptionBecauseOfUnknownTeamId(){
        Team team = new Team();
        team.setId(1L);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = new ResourceNotFoundException("Team", "id", team.getId());
        }};

        teamService.update(team.getId(),new NewTeamDto(),new User());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateTeam_shouldThrowAResourceNotFoundExceptionBecauseTheCurrentUserIsNotInTheTeam(){
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        user.setUsername("user1");

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user,team);
            result = new ResourceNotFoundException("TeamMate", "user", user.getUsername());
        }};

        teamService.update(team.getId(),new NewTeamDto(),user);
    }

    @Test(expected = ch.fhnw.edu.wodss.tippspielapi.exception.NotAllowedException.class)
    public void testUpdateTeam_shouldThrowANotAllowedExceptionBecauseTheCurrentUserIsNotOwner(){
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(false);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user,team);
            result = Optional.of(teamMate);
        }};

        teamService.update(team.getId(),new NewTeamDto(),user);
    }

    @Test
    public void testUpdateTeam_shouldReturnATeamDtoWithTheUpdatedName(){
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        TeamMate teamMate = new TeamMate();
        teamMate.setOwner(true);

        new Expectations() {{
            teamRepository.findById(team.getId());
            result = Optional.of(team);

            teamMateRepository.findByUserAndTeam(user,team);
            result = Optional.of(teamMate);

            teamRepository.save(team);
            result = team;
        }};

        NewTeamDto newTeamDto = new NewTeamDto();
        newTeamDto.setName("newTeam");

        TeamDto teamDto = teamService.update(team.getId(), newTeamDto, user);

        Assert.assertEquals(newTeamDto.getName(),teamDto.getName());
    }


}
