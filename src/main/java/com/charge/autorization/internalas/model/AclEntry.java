package com.charge.autorization.internalas.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
@Entity
@Table(name = "acl_entries")
@Data
public class AclEntry {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "station_id")
    private UUID stationId;
    @Column(name = "driver_id")
    private String driverId;
    private String token;
}
