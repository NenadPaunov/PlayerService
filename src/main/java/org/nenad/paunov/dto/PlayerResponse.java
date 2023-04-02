package org.nenad.paunov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.nenad.paunov.model.PlayerRecord;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerResponse {
	private Long playerId;
	private String name;
	private List<Long> gameId;

	public PlayerResponse(PlayerRecord playerRecord) {
		setPlayerId(playerRecord.getId());
		setName(playerRecord.getName());
		setGameId(playerRecord.getGameIds());
	}
}
