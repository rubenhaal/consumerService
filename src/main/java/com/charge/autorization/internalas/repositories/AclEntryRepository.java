package com.charge.autorization.internalas.repositories;

import com.charge.autorization.internalas.model.AclEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {
    boolean existsByStationIdAndDriverId(UUID stationId, String driverId);
    Optional<AclEntry> findByStationIdAndDriverId(UUID stationId, String driverId);
}
