package fi.nordicwatt.model.service;

import org.junit.jupiter.api.Test;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.data.DataResponse;
import fi.nordicwatt.model.datamodel.EnergyModel;
import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.model.datamodel.ResponseBundle;
import fi.nordicwatt.model.datamodel.WeatherModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.EnvironmentVariables;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * @author ???
 */
public class DataManagerTest {

    @Test
    public void testGetData() throws IOException, InterruptedException {
        EnvironmentVariables.load(".env");
        DataManager dm = DataManager.getInstance();

        String responseId = UUID.randomUUID().toString();
        RequestBundle requests = new RequestBundle();
        
        final ResponseBundle responses = new ResponseBundle(responseId);
        final List<Exception> exceptions = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        Thread currentThread = Thread.currentThread();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        final Thread[] callbackThread = new Thread[3];
        System.out.println("TEST STARTING ON THREAD " + Thread.currentThread().toString());

        requests.addItem(
            new DataRequest(
                DataType.TEMPERATURE, 
                LocalDateTime.parse("2023-08-01 00:00:00", formatter),
                LocalDateTime.parse("2023-08-03 00:00:00", formatter),
                "tampere"));
        //requests.addItem(new DataRequest(DataType.CONSUMPTION, LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Helsinki"));

        DataManagerListener listener = new DataManagerListener() {
            @Override
            public void dataRequestSuccess(ResponseBundle responsesReceived) {
                callbackThread[0] = Thread.currentThread();
                responses.setItems(responsesReceived.getItems());
                latch.countDown();
            }

            @Override
            public void dataRequestFailure(RequestBundle failedRequests, Exception e) {
                callbackThread[0] = Thread.currentThread();
                exceptions.add(e);
                latch.countDown();
            }
        };

        assertTrue(dm.registerListener(listener));
        dm.getData(requests);

        // Assuming that getData has been called but data is not ready yet..
        assertEquals(0, responses.getItems().size());
        assertEquals(0, exceptions.size());

        // Wait for data to be ready
        boolean success = latch.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(dm.removeListener(listener));


        // Assuming callback was run on a different thread
        assertNotEquals(callbackThread[0], Thread.currentThread());

        assertEquals(1, responses.getItems().size());
        assertEquals(0, exceptions.size());
        for (DataResponse response : responses.getItems()){
            assertTrue(response.getData() instanceof WeatherModel);
        }
        assertTrue(success);
        assertTrue(responses.getItems().get(0).getData().getDataPoints().containsKey("2023-08-01 00:00:00"));
        assertFalse(responses.getItems().get(0).getData().getDataPoints().containsKey("2023-07-31 23:00:00"));

        // Assuming the callback thread eventually terminates
        boolean threadTerminated = false;
        long startTime = System.currentTimeMillis();

        while(!threadTerminated && System.currentTimeMillis() - startTime < 5000){
            if (callbackThread[0].getState() == Thread.State.TERMINATED){
                threadTerminated = true;
            }
        }

        assertTrue(threadTerminated);

        String responseId2 = UUID.randomUUID().toString();
        RequestBundle requests2 = new RequestBundle();
        ResponseBundle responses2 = new ResponseBundle(responseId2);

        final List<Exception> exceptions2 = new ArrayList<>();
        CountDownLatch latch2 = new CountDownLatch(1);

        requests2.addItem(
            new DataRequest(
                DataType.CONSUMPTION,
                LocalDateTime.parse("2023-08-01 00:00:00", formatter),
                LocalDateTime.parse("2023-08-02 00:00:00", formatter), 
                "helsinki"));

        DataManagerListener listener2 = new DataManagerListener() {
            @Override
            public void dataRequestSuccess(ResponseBundle responsesReceived) {
                callbackThread[1] = Thread.currentThread();
                responses2.setItems(responsesReceived.getItems());
                latch2.countDown();
            }

            @Override
            public void dataRequestFailure(RequestBundle failedRequests, Exception e) {
                callbackThread[1] = Thread.currentThread();
                exceptions2.add(e);
                latch2.countDown();
            }
        };

        assertTrue(dm.registerListener(listener2));
        dm.getData(requests2);

        assertEquals(0, responses2.getItems().size());
        assertEquals(0, exceptions2.size());

        boolean success2 = latch2.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(dm.removeListener(listener2));

        assertEquals(1, responses2.getItems().size());
        assertEquals(0, exceptions2.size());
        for (DataResponse result : responses2.getItems()){
            assertTrue(result.getData() instanceof EnergyModel);
        }
        assertTrue(success2);

        // Assuming callback was run on a different thread
        assertNotEquals(callbackThread[0], Thread.currentThread());

        // Assuming the callback thread eventually terminates

        threadTerminated = false;
        startTime = System.currentTimeMillis();

        while(!threadTerminated && System.currentTimeMillis() - startTime < 5000){
            if (callbackThread[1].getState() == Thread.State.TERMINATED){
                threadTerminated = true;
            }
        }

        assertTrue(threadTerminated);

        String responseId3 = UUID.randomUUID().toString();
        RequestBundle requests3 = new RequestBundle();
        ResponseBundle responses3 = new ResponseBundle(responseId3);

        final List<Exception> exceptions3 = new ArrayList<>();
        CountDownLatch latch3 = new CountDownLatch(1);

        requests3.addItem(
                new DataRequest(
                        DataType.TEMPERATURE,
                        LocalDateTime.parse("2023-08-01 00:00:00", formatter),
                        LocalDateTime.parse("2023-08-30 00:00:00", formatter),
                        "helsinki"));

        DataManagerListener listener3 = new DataManagerListener() {
            @Override
            public void dataRequestSuccess(ResponseBundle responsesReceived) {
                callbackThread[2] = Thread.currentThread();
                responses3.setItems(responsesReceived.getItems());
                latch3.countDown();
            }

            @Override
            public void dataRequestFailure(RequestBundle failedRequests, Exception e) {
                callbackThread[2] = Thread.currentThread();
                exceptions3.add(e);
                latch3.countDown();
            }
        };

        assertTrue(dm.registerListener(listener3));
        dm.getData(requests3);

        boolean success3 = latch3.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(success3);

        //assertEquals(0, responses3.getItems().size());
        //assertEquals(1, exceptions3.size());

    }

}
