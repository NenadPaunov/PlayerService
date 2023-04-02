package org.nenad.paunov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nenad.paunov.dao.PlayerDao;
import org.nenad.paunov.dto.PlayerRequest;
import org.nenad.paunov.dto.PlayerResponse;
import org.nenad.paunov.exception.EntityNotFoundException;
import org.nenad.paunov.exception.ExternalServiceException;
import org.nenad.paunov.model.PlayerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {

	private final PlayerDao playerDao;
	private final RestTemplate restTemplate;

	@Value("${game_service_host}")
	private String gameServiceHost;

	public PlayerResponse getPlayerInfo(Long playerId) throws EntityNotFoundException {
		return new PlayerResponse(playerDao.getPlayerInfoById(playerId));
	}

	public PlayerResponse getPlayerInfo(String name) {
		return playerDao.getPlayerInfoByName(name).map(PlayerResponse::new).orElseGet(PlayerResponse::new);
	}

	public PlayerResponse registerPlayer(PlayerRequest playerRequest) throws EntityNotFoundException {
		PlayerRecord playerRecord;
		if (playerRequest.getName() != null) {
			Optional<PlayerRecord> optionalPlayerRecord = playerDao.getPlayerInfoByName(playerRequest.getName());

			if (optionalPlayerRecord.isPresent()) {
				playerRecord = optionalPlayerRecord.get();
				if (playerRecord.getGameIds() != null) {
					playerRecord.getGameIds().add(playerRequest.getGameId());
				} else {
					playerRecord.setGameIds(Collections.singletonList(playerRequest.getGameId()));
				}
			} else {
				playerRecord = new PlayerRecord(playerRequest.getName(), playerRequest.getGameId() != null ? Collections.singletonList(playerRequest.getGameId()) : null);
			}
		} else {
			playerRecord = removeGameId(playerRequest.getPlayerId(), playerRequest.getGameId());
		}
		PlayerRecord savedPlayerRecord = playerDao.savePlayer(playerRecord);
		return new PlayerResponse(savedPlayerRecord);
	}

	private PlayerRecord removeGameId(Long playerId, Long gameIdToRemove) throws EntityNotFoundException {
		PlayerRecord playerRecord = playerDao.getPlayerInfoById(playerId);
		playerRecord.getGameIds().remove(gameIdToRemove);
		return playerRecord;
	}

	@Transactional
	public void deletePlayer(Long playerId) {
		deleteAllGames(playerId);
		playerDao.deletePlayer(playerId);
	}

	private URI getDeleteAllGamesEndpoint(Long id) {
		return UriComponentsBuilder.fromUriString(gameServiceHost + "/gameservice/player/")
				.path(String.valueOf(id))
				.build()
				.toUri();
	}

	public void deleteAllGames(Long playerId) {
		URI uri = getDeleteAllGamesEndpoint(playerId);
		log.debug("Delete all games endpoint: {} ", uri);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<PlayerRequest> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Void> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.DELETE, requestEntity, Void.class);

		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			log.error("Unable to get gameIds from Player Service. Status: {} Body: {}", responseEntity.getStatusCode(), responseEntity.getBody());
			throw new ExternalServiceException("Unable to get gameIds from Player Service.");
		}

	}


}
