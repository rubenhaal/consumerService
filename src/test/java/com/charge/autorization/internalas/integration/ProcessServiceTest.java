package com.charge.autorization.internalas.integration;

import com.charge.autorization.internalas.model.AclEntry;
import com.charge.autorization.internalas.model.MessageDto;
import com.charge.autorization.internalas.service.AclService;
import com.charge.autorization.internalas.service.CallbackResponseService;
import com.charge.autorization.internalas.service.ProcessService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProcessServiceTest {

    public static final String TESTID = "testid";
    public static final String URL = "http://localhost:8080";

    ProcessService processService;

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void shouldSendMessageToEndpoint_whenMessageReceivedIsOk(){


        try (MockWebServer mockWebServer = new MockWebServer()) {
            mockWebServer.start();

            String callbackUrl = mockWebServer.url("/api/session/received").toString();


            AclService aclService = mock(AclService.class);
            CallbackResponseService callbackResponseService = mock(CallbackResponseService.class);
            WebClient webClient = WebClient.builder().build();

            ProcessService processService = new ProcessService(
                    aclService,
                    webClient,
                    callbackResponseService
            );

            UUID stationId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
            String driverId = "driverX";

            MessageDto message = new MessageDto();
            message.setStationId(stationId);
            message.setDriverId(driverId);
            message.setCallback("http://localhost:"+mockWebServer.getPort()+"/api/session/received");

            AclEntry aclEntry = new AclEntry();
            aclEntry.setToken("tokenid");
            aclEntry.setId(2L);
            aclEntry.setStationId(stationId);
            aclEntry.setDriverId(driverId);

            when(aclService.findAclByDriverIdAndStationId(stationId, driverId))
                    .thenReturn(Optional.of(aclEntry));

            mockWebServer.enqueue(new MockResponse().setResponseCode(200));

            // Act
            processService.processMessage(message);

            // Assert
            RecordedRequest request = mockWebServer.takeRequest(2, TimeUnit.SECONDS);
            assertNotNull(request);
            assertEquals("POST", request.getMethod());
            assertEquals("/api/session/received", request.getPath());

            String body = request.getBody().readUtf8();
            assertTrue(body.contains("\"driverToken\":\"tokenid\""));
            assertTrue(body.contains("\"status\":\"allowed\""));
            assertTrue(body.contains(stationId.toString()));

            verify(callbackResponseService).saveCallBackLog(argThat(cb ->
                    cb.getStationId().equals(stationId) &&
                            cb.getDriverToken().equals("tokenid") &&
                            cb.getStatus().equals("allowed")
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void shouldFailSendMessageToEndpoint_whenCallBackUrlIsNotOk(){


        try (MockWebServer mockWebServer = new MockWebServer()) {
            mockWebServer.start();

            String callbackUrl = mockWebServer.url("/api/session/received").toString();


            AclService aclService = mock(AclService.class);
            CallbackResponseService callbackResponseService = mock(CallbackResponseService.class);
            WebClient webClient = WebClient.builder().build();

            ProcessService processService = new ProcessService(
                    aclService,
                    webClient,
                    callbackResponseService
            );

            UUID stationId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
            String driverId = "driverX";

            MessageDto message = new MessageDto();
            message.setStationId(stationId);
            message.setDriverId(driverId);
            message.setCallback("http://loclahost:"+mockWebServer.getPort()+"/api/session/received");

            AclEntry aclEntry = new AclEntry();
            aclEntry.setToken("tokenid");
            aclEntry.setId(2L);
            aclEntry.setStationId(stationId);
            aclEntry.setDriverId(driverId);

            when(aclService.findAclByDriverIdAndStationId(stationId, driverId))
                    .thenReturn(Optional.of(aclEntry));

            mockWebServer.enqueue(new MockResponse().setResponseCode(200));

            // Act
            processService.processMessage(message);

            // Assert
            RecordedRequest request = mockWebServer.takeRequest(2, TimeUnit.SECONDS);
            assertNull(request);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}