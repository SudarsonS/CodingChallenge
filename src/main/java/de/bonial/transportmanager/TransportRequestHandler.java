package de.bonial.transportmanager;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.bonial.transportmanager.model.Mode;
import de.bonial.transportmanager.model.Request;
import de.bonial.transportmanager.service.PassengerService;
import de.bonial.transportmanager.util.PropertyUtil;

import java.util.Map;
import java.util.Properties;

public class TransportRequestHandler implements RequestHandler<S3Event, Object> {

    Injector injector = Guice.createInjector(new AppModule());

    ObjectMapper mapper = new ObjectMapper();

    private PassengerService service = injector.getInstance(PassengerService.class);
    private AmazonS3 client = injector.getInstance(AmazonS3.class);

    public Object handleRequest(S3Event s3event, Context context) {

        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        S3EventNotification.S3Entity s3 = s3event.getRecords().get(0).getS3();
        Request request = getRequest(s3);
        Map<Mode, Integer> result = service.compute(request);
        try {
            client.putObject(PropertyUtil.prop.getProperty("bucket_name"), PropertyUtil.prop.getProperty("result_key"), mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    private Request getRequest(S3EventNotification.S3Entity s3) {
        String bucketName = s3.getBucket().getName();
        String key = s3.getObject().getKey();
        try {
            S3Object s3Object = client.getObject(bucketName, key);
            return mapper.readValue(s3Object.getObjectContent(), Request.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setService(PassengerService service) {
        this.service = service;
    }

    public void setClient(AmazonS3 client) {
        this.client = client;
    }
}
