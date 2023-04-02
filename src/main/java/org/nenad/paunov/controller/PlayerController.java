package org.nenad.paunov.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nenad.paunov.dto.PlayerRequest;
import org.nenad.paunov.exception.EntityNotFoundException;
import org.nenad.paunov.service.PlayerService;
import org.nenad.paunov.vo.SwaggerConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
public class PlayerController {

	private final PlayerService playerService;

	@Operation(summary = SwaggerConstants.PLAYER_INFO)
	@GetMapping("/{id}")
	public ResponseEntity<?> getPlayerInfo(@PathVariable("id") Long id) throws EntityNotFoundException {
		log.info("Calling getPlayerInfo with id: {}", id);
		return ResponseEntity.ok().body(playerService.getPlayerInfo(id));
	}

	@Operation(summary = SwaggerConstants.GAME_IDS)
	@GetMapping("/gameIds")
	public ResponseEntity<?> getGameIds(@RequestParam String name) {
		log.info("Calling getGameIds with name: {}", name);
		return ResponseEntity.ok().body(playerService.getPlayerInfo(name));
	}

	@Operation(summary = SwaggerConstants.REGISTER_PLAYER)
	@PostMapping ( "/register" )
	public ResponseEntity<?> registerPlayer(@RequestBody @Validated  PlayerRequest playerRequest) throws EntityNotFoundException {
		if(playerRequest.getName() == null && playerRequest.getPlayerId() == null) {
			return ResponseEntity.badRequest().body("Either playerName or playerId must be sent");
		}
		log.info("Calling registerPlayer with name: {} , playerId: {} and gameId: {}", playerRequest.getName(), playerRequest.getPlayerId(), playerRequest.getGameId());
		return ResponseEntity.ok().body(playerService.registerPlayer(playerRequest));
	}

	@Operation(summary = SwaggerConstants.DELETE_PLAYER)
	@DeleteMapping ( "/{id}" )
	public ResponseEntity<?> deletePlayer(@PathVariable("id") Long id) {
		log.info("Calling deletePlayer with id: {}", id);
		playerService.deletePlayer(id);
		return ResponseEntity.ok().build();
	}
}
