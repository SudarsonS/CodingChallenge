package de.bonial.transportmanager;

import cloud.localstack.LambdaContext;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import de.bonial.transportmanager.model.Request;
import de.bonial.transportmanager.service.PassengerService;
import de.bonial.transportmanager.service.PassengerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class TransportRequestHandlerTest {

    private TransportRequestHandler requestHandler;

    private PassengerService passengerService;

    private AmazonS3 client;

    @Before
    public void setUp() throws Exception {
        requestHandler = new TransportRequestHandler();
        passengerService = mock(PassengerServiceImpl.class);
        requestHandler.setService(passengerService);
        client = mock(AmazonS3.class);
        requestHandler.setClient(client);
        when(passengerService.compute(any())).thenReturn(ImmutableMap.of());
        S3Object s3Object = new S3Object();
        String input = "{ \"transports\": [{ \"model\": \"Boeing 777\", \"b-passenger-capacity\": 14, \"e-passengercapacity\": 300 }, { \"manufacturer\": \"BMW\", \"model\": \"M3\", \"passenger-capacity\": 4 }, { \"model\": \"ICE\", \"number-wagons\": 5, \"w-passenger-capacity\": 30 }, { \"manufacturer\": \"Mercedes-Benz\", \"model\": \"CKlasse\", \"passenger-capacity\": 4 }, { \"model\": \"Boeing 777S\", \"b-passenger-capacity\": 10, \"e-passengercapacity\": 200 }, { \"manufacturer\": \"Audi\", \"model\": \"Q3\", \"passenger-capacity\": 6 } ] }";
        s3Object.setObjectContent(new ByteArrayInputStream(input.getBytes()));
        when(client.getObject(anyString(), anyString())).thenReturn(s3Object);
    }

    @Test
    public void shouldFetchTheFileFromS3WhenTheEventIsArrived() throws IOException {
        S3Event s3Event = getS3Event();

        requestHandler.handleRequest(s3Event, new LambdaContext());

        verify(client).getObject(eq("bonial-transport"), eq("234324"));
    }

    @Test
    public void shouldComputeTheResultByInvokingPassengerService() throws IOException {
        S3Event s3Event = getS3Event();

        requestHandler.handleRequest(s3Event, new LambdaContext());

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(passengerService).compute(captor.capture());
        Request request = captor.getValue();
        assertThat(request.getTransports().size()).isEqualTo(6);
    }

    @Test
    public void shouldPutTheResultToResultBucket() throws IOException {
        S3Event s3Event = getS3Event();

        requestHandler.handleRequest(s3Event, new LambdaContext());

        verify(client).putObject("bonial-transport", "summary/1/result", "{}");
    }

    private S3Event getS3Event() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<S3EventNotification.S3EventNotificationRecord>> typeReference = new TypeReference<List<S3EventNotification.S3EventNotificationRecord>>() {
        };
        List<S3EventNotification.S3EventNotificationRecord> records = mapper.readValue("[ { \"eventVersion\": \"2.0\", \"eventTime\": \"1970-01-01T00:00:00.000Z\", \"requestParameters\": { \"sourceIPAddress\": \"127.0.0.1\" }, \"s3\": { \"configurationId\": \"testConfigRule\", \"object\": { \"eTag\": \"0123456789abcdef0123456789abcdef\", \"sequencer\": \"0A1B2C3D4E5F678901\", \"key\": \"234324\", \"size\": 1024 }, \"bucket\": { \"arn\": \"arn:aws:s3:::bonial-transport\", \"name\": \"bonial-transport\", \"ownerIdentity\": { \"principalId\": \"EXAMPLE\" } }, \"s3SchemaVersion\": \"1.0\" }, \"responseElements\": { \"x-amz-id-2\": \"EXAMPLE123/5678abcdefghijklambdaisawesome/mnopqrstuvwxyzABCDEFGH\", \"x-amz-request-id\": \"EXAMPLE123456789\" }, \"awsRegion\": \"us-east-1\", \"eventName\": \"ObjectCreated:Put\", \"userIdentity\": { \"principalId\": \"EXAMPLE\" }, \"eventSource\": \"aws:s3\" }]", typeReference);
        return new S3Event(records);
    }


}