package org.nenad.paunov.model;

import jakarta.persistence.*;

//@Entity
//@Table(name = "player_stats", schema = "player_data")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
// TODO Ovo mozda mozemo zameniti sa Redisom
public class PlayerStatsRecord extends AuditModel {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		private int numWins;

		private int numLosses;

		private double avgScore;

		private int highScore;

		@OneToOne
		@JoinColumn(name = "player_record_id")
		private PlayerRecord playerRecord;
}
