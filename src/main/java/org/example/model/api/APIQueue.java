package org.example.model.api;

import org.example.model.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * APIQueue
 * Singleton class that handles the API queue
 */
public class APIQueue {

    public static void getData(List<ApiDataRequest> parameters, APIDataListener listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<ArrayList<ApiDataResult>> future = executor.submit(() -> {
            ArrayList<ApiDataResult> results = new ArrayList<>();
            for (ApiDataRequest parameter : parameters) {
                results.add(APIOperator.getData(parameter));
            }
            return results;
        });

        int timeout = 10;

        new Thread(() -> {
            try {
                ArrayList<ApiDataResult> result = future.get(timeout, TimeUnit.SECONDS);
                listener.newApiDataAvailable(result);

            } catch (TimeoutException e) {
                System.out.println("API call took longer than " + timeout + " seconds.");
                future.cancel(true); //
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }).start();

    }




}
