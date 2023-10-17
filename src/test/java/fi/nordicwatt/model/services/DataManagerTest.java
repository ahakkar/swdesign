package fi.nordicwatt.model.services;

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

        queries.add(new DataRequest(DataType.CONSUMPTION, LocalDateTime.now().minusDays(1), LocalDateTime.now(), "FI"));
        queries.add(new DataRequest(DataType.CONSUMPTION, LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Helsinki"));
        DataManagerListener listener = new DataManagerListener() {
            @Override
            public void onDataReadyForChart(List<DataResult> data, Exception e) {
                if (data != null){
                    results.addAll(data);
                }

                if (e != null){
                    exceptions.add(e);
                }

                latch.countDown();
            }
        };
        dm.registerListener(listener);
        try {
            dm.getData(queries);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Assuming that getData has been called but data is not ready yet..
        assertEquals(0, results.size());
        assertEquals(0, exceptions.size());

        // Wait for data to be ready
        boolean success = latch.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals(2, results.size());
        assertEquals(0, exceptions.size());
        for (DataResult result : results){
            assertTrue(result.getData() instanceof EnergyModel);
        }
        assertTrue(success);


    }

}
