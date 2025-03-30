package com.charge.autorization.internalas.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class CallbackResponse {
    private UUID stationId;
    private String driverToken;
    private String status;
}
