package de.bonial.transportmanager;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import de.bonial.transportmanager.handlers.CarHandler;
import de.bonial.transportmanager.handlers.FlightHandler;
import de.bonial.transportmanager.handlers.Handler;
import de.bonial.transportmanager.handlers.TrainHandler;
import de.bonial.transportmanager.service.PassengerService;
import de.bonial.transportmanager.service.PassengerServiceImpl;
import de.bonial.transportmanager.util.PropertyUtil;

public class AppModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(PassengerService.class).to(PassengerServiceImpl.class);
        Multibinder<Handler> multiBinder = Multibinder.newSetBinder(binder(), Handler.class);
        multiBinder.addBinding().to(CarHandler.class);
        multiBinder.addBinding().to(FlightHandler.class);
        multiBinder.addBinding().to(TrainHandler.class);
    }

    @Provides
    @Singleton
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials("foobar", "foobar");
        AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(PropertyUtil.prop.getProperty("aws_service_end_point"),
                Regions.DEFAULT_REGION.getName());
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(provider).build();
    }

}
