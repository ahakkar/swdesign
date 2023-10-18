package fi.nordicwatt.model.services;

import fi.nordicwatt.model.data.WeatherModel;
import org.junit.jupiter.api.Test;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.data.DataResult;
import fi.nordicwatt.model.data.EnergyModel;
import fi.nordicwatt.model.services.DataManager;
import fi.nordicwatt.model.services.DataManagerListener;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.EnvironmentVariables;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        List<DataRequest> queries = new ArrayList<>();
        final List<DataResult> results = new ArrayList<>();
        final List<Exception> exceptions = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        Thread currentThread = Thread.currentThread();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        final Thread[] callbackThread = new Thread[2];

        System.out.println("TEST STARTING ON THREAD " + Thread.currentThread().toString());
        queries.add(new DataRequest(DataType.TEMPERATURE, LocalDateTime.parse("2023-08-01 00:00:00", formatter), LocalDateTime.parse("2023-08-02 00:00:00", formatter), "tampere"));
        //queries.add(new DataRequest(DataType.CONSUMPTION, LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Helsinki"));
        DataManagerListener listener = new DataManagerListener() {
            @Override
            public void onDataReadyForChart(List<DataResult> data, Exception e) {
                callbackThread[0] = Thread.currentThread();
                if (data != null){
                    results.addAll(data);
                }

                if (e != null){
                    exceptions.add(e);
                }
                latch.countDown();

            }
        };
        assertTrue(dm.registerListener(listener));
        dm.getData(queries);

        // Assuming that getData has been called but data is not ready yet..
        assertEquals(0, results.size());
        assertEquals(0, exceptions.size());

        // Wait for data to be ready
        boolean success = latch.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(dm.removeListener(listener));


        // Assuming callback was run on a different thread
        assertNotEquals(callbackThread[0], Thread.currentThread());

        assertEquals(1, results.size());
        assertEquals(0, exceptions.size());
        for (DataResult result : results){
            assertTrue(result.getData() instanceof WeatherModel);
        }
        assertTrue(success);

        // Assuming the callback thread eventually terminates
        boolean threadTerminated = false;
        long startTime = System.currentTimeMillis();

        while(!threadTerminated && System.currentTimeMillis() - startTime < 5000){
            if (callbackThread[0].getState() == Thread.State.TERMINATED){
                threadTerminated = true;
            }
        }

        assertTrue(threadTerminated);

        List<DataRequest> queries2 = new ArrayList<>();
        final List<DataResult> results2 = new ArrayList<>();
        final List<Exception> exceptions2 = new ArrayList<>();
        CountDownLatch latch2 = new CountDownLatch(1);

        queries2.add(new DataRequest(DataType.CONSUMPTION, LocalDateTime.parse("2023-08-01 00:00:00", formatter), LocalDateTime.parse("2023-08-02 00:00:00", formatter), "helsinki"));
        DataManagerListener listener2= new DataManagerListener() {
            @Override
            public void onDataReadyForChart(List<DataResult> data, Exception e) {
                callbackThread[1] = Thread.currentThread();
                if (data != null){
                    results2.addAll(data);
                }

                if (e != null){
                    exceptions2.add(e);
                }
                latch2.countDown();

            }
        };

        assertTrue(dm.registerListener(listener2));
        dm.getData(queries2);

        assertEquals(0, results2.size());
        assertEquals(0, exceptions2.size());

        boolean success2 = latch2.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(dm.removeListener(listener2));

        assertEquals(1, results2.size());
        assertEquals(0, exceptions2.size());
        for (DataResult result : results2){
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

    }

}
