package com.charge.autorization.internalas.service;

import com.charge.autorization.internalas.model.AclEntry;
import com.charge.autorization.internalas.model.CallbackResponse;
import com.charge.autorization.internalas.model.MessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ProcessService {
    private AclService aclService;
    private final WebClient webClient;
    private CallbackResponseService callbackResponseService;
    public void processMessage(MessageDto message ){
        Optional<AclEntry> aclEntry = aclService.findAclByDriverIdAndStationId(message.getStationId(), message.getDriverId());

        log.info("Access granted. Making callback to {}", message.getCallback());


        var callbackResponse = CallbackResponse.builder()
                .stationId(message.getStationId())
                .driverToken(aclEntry.isPresent() ? aclEntry.get().getToken() : "")
                .status(isAllowed(aclEntry.isPresent()))
                .build();

        callbackResponseService.saveCallBackLog(callbackResponse);
        try {
            URI uri = URI.create(message.getCallback());

            webClient.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(callbackResponse)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> {
                        log.warn("Client error: {}", response.statusCode());
                        return response.createException();
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, response -> {
                        log.error("Server error: {}", response.statusCode());
                        return response.createException();
                    })
                    .toBodilessEntity()
                    .doOnSuccess(response -> log.info("Callback success: {}", response.getStatusCode()))
                    .doOnError(error -> log.error("Callback failed: {}", error.getMessage()))
                    .retry(3)
                    .subscribe();
        }catch (IllegalArgumentException  ex){
            log.error("failed to connect : {} ", message.getCallback());
        }
    }
    private String isAllowed(boolean allowed){
        return allowed? "allowed":"not allowed";
    }
}
