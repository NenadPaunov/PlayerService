package org.nenad.paunov.repository;

import org.nenad.paunov.model.PlayerRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerRecord, Long> {

	Optional<PlayerRecord> findByName(String name);
}
