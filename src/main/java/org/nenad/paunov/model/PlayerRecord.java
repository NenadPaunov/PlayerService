package org.nenad.paunov.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRecord extends AuditModel {
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	@ElementCollection
	@CollectionTable(name = "player_games", joinColumns = @JoinColumn(name = "player_id"))
	@Column(name = "game_id")
	private List<Long> gameIds;
//	@OneToOne(mappedBy = "playerRecord", cascade = CascadeType.ALL)
//	private PlayerStatsRecord playerStats;

	public PlayerRecord(String name, List<Long> gameIds) {
		this.setName(name);
		this.setGameIds(gameIds);
	}
}
