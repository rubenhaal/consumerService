package com.charge.autorization.internalas.service;

import com.charge.autorization.internalas.model.CallBackResponseEntity;
import com.charge.autorization.internalas.model.CallbackResponse;
import com.charge.autorization.internalas.repositories.CallBackResponseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CallbackResponseService {
    private CallBackResponseRepository repository;

    public void saveCallBackLog(CallbackResponse callbackResponse){


        repository.save(CallBackResponseEntity.builder()
                        .driverToken(callbackResponse.getDriverToken())
                        .status(callbackResponse.getStatus())
                        .stationId(callbackResponse.getStationId())
                .build());

    }
}
