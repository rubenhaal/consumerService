package com.charge.autorization.internalas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
@Entity
public class CallBackResponseEntity {

    @Id
    @GeneratedValue
    private Long id;
    private UUID stationId;
    private String driverToken;
    private String status;
}
