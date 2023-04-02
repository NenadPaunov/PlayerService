package org.nenad.paunov.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nenad.paunov.dao.PlayerDao;
import org.nenad.paunov.dto.PlayerRequest;
import org.nenad.paunov.dto.PlayerResponse;
import org.nenad.paunov.exception.EntityNotFoundException;
import org.nenad.paunov.model.PlayerRecord;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

	@InjectMocks
	private static PlayerService instance;
	private static final PlayerDao playerDao = Mockito.mock(PlayerDao.class);
	private static final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

	@BeforeAll
	public static void beforeAll() {
		instance = new PlayerService(playerDao, restTemplate);
	}

	@Test
	void getGameInfoById() throws Exception {
		Long playerId = 1L;
		PlayerRecord playerRecord = new PlayerRecord("Test name", null);
		given(playerDao.getPlayerInfoById(playerId)).willReturn(playerRecord);
		PlayerResponse expectedResponse = new PlayerResponse(playerRecord);

		PlayerResponse response = instance.getPlayerInfo(playerId);

		assertThat(response.getName()).isEqualTo(expectedResponse.getName());
	}

	@Test
	void getGameInfoByName() {
		String name = "Test name";
		PlayerRecord playerRecord = new PlayerRecord("Test name", null);
		given(playerDao.getPlayerInfoByName(name)).willReturn(Optional.of(playerRecord));
		PlayerResponse expectedResponse = new PlayerResponse(playerRecord);

		PlayerResponse response = instance.getPlayerInfo(name);

		assertThat(response.getName()).isEqualTo(expectedResponse.getName());
	}

	@Test
	void addGameToTheExistingPlayer() throws EntityNotFoundException {
		String name = "Test name";
		Long gameId = 1L;
		PlayerRequest playerRequest = new PlayerRequest();
		playerRequest.setName(name);
		playerRequest.setGameId(gameId);

		PlayerRecord playerRecord = new PlayerRecord("Test name", null);
		given(playerDao.getPlayerInfoByName(name)).willReturn(Optional.of(playerRecord));
		given(playerDao.savePlayer(any())).willReturn(playerRecord);
		PlayerResponse expectedResponse = new PlayerResponse(playerRecord);

		PlayerResponse response = instance.registerPlayer(playerRequest);

		assertThat(response.getName()).isEqualTo(expectedResponse.getName());
	}

	@Test
	void addGameToTheNewPlayer() throws EntityNotFoundException {
		String name = "Test name";
		Long gameId = 1L;
		PlayerRequest playerRequest = new PlayerRequest();
		playerRequest.setName(name);
		playerRequest.setGameId(gameId);

		PlayerRecord playerRecord = new PlayerRecord("Test name", null);
		given(playerDao.getPlayerInfoByName(name)).willReturn(Optional.empty());
		given(playerDao.savePlayer(any())).willReturn(playerRecord);
		PlayerResponse expectedResponse = new PlayerResponse(playerRecord);

		PlayerResponse response = instance.registerPlayer(playerRequest);

		assertThat(response.getName()).isEqualTo(expectedResponse.getName());
	}

	@Test
	void removeGameId() throws EntityNotFoundException {
		Long gameId = 1L;
		PlayerRequest playerRequest = new PlayerRequest();
		playerRequest.setGameId(gameId);
		List<Long> gameIds = new ArrayList<>();
		gameIds.add(1L);
		gameIds.add(2L);
		PlayerRecord playerRecord = new PlayerRecord("Test name", gameIds);
		given(playerDao.getPlayerInfoById(any())).willReturn(playerRecord);
		given(playerDao.savePlayer(any())).willReturn(playerRecord);
		PlayerResponse expectedResponse = new PlayerResponse(playerRecord);

		PlayerResponse response = instance.registerPlayer(playerRequest);

		assertThat(response.getName()).isEqualTo(expectedResponse.getName());
	}

	@Test
	void deletePlayerTest() {
		Long playerId = 1L;

		doNothing().when(instance).deleteAllGames(playerId);
		doNothing().when(playerDao).deletePlayer(playerId);

		instance.deletePlayer(playerId);

		verify(instance, times(1)).deleteAllGames(playerId);
		verify(playerDao, times(1)).deletePlayer(playerId);
	}

}