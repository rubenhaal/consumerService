package com.charge.autorization.internalas.service;

import com.charge.autorization.internalas.model.AclEntry;
import com.charge.autorization.internalas.repositories.AclEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AclService {
    private AclEntryRepository repository;

    public boolean checkAclEntryForUserAndStationId(UUID stationId, String driverID){
        return repository.existsByStationIdAndDriverId(stationId, driverID);
    }
    public Optional<AclEntry> findAclByDriverIdAndStationId(UUID stationId, String driverID){
        return repository.findByStationIdAndDriverId(stationId,driverID);
    }
}
