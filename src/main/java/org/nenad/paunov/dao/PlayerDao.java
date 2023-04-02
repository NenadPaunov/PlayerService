package org.nenad.paunov.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nenad.paunov.exception.EntityNotFoundException;
import org.nenad.paunov.model.PlayerRecord;
import org.nenad.paunov.repository.PlayerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerDao {

	private final PlayerRepository dbRepository;

	public PlayerRecord getPlayerInfoById(Long playerId) throws EntityNotFoundException {
		Optional<PlayerRecord> playerRecord = dbRepository.findById(playerId);
		if (playerRecord.isEmpty()) {
			log.error("There is no player associated with requested id - {}", playerId);
			throw new EntityNotFoundException();
		}
		return playerRecord.get();
	}

	public Optional<PlayerRecord> getPlayerInfoByName(String name) {
		return dbRepository.findByName(name);
	}

	public PlayerRecord savePlayer(PlayerRecord playerRecord) {
		try {
			return (dbRepository.save(playerRecord));
		} catch (DataIntegrityViolationException e) {
			log.error("Player with name {} - already exists.",
					playerRecord.getName(), e);
			throw e;
		} catch (Exception e) {
			log.error("PlayerRecord {} - Unexpected exception while saving player",
					playerRecord.getName(), e);
			throw e;
		}
	}

	public void deletePlayer(Long playerId) {
		try {
			dbRepository.deleteById(playerId);
		} catch (Exception e) {
			log.error("PlayerRecord {} - Unexpected exception while deleting player",
					playerId, e);
			throw e;
		}
	}
}
