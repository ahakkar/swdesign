package org.example.model.api;

import org.example.model.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * APIQueue
 * Singleton class that handles the API queue
 * 
 * @author ???
 */
public class APIQueue {

    public static void getData(List<ApiDataRequest> parameters, APIDataListener listener) {
        Future<ArrayList<ApiDataResult>> future = getFuture(parameters);

        int timeout = 10;

        new Thread(() -> {
            try {
                ArrayList<ApiDataResult> result = future.get(timeout, TimeUnit.SECONDS);
                listener.newApiDataAvailable(result, null);

            } catch (Exception e) {
                future.cancel(true);
                listener.newApiDataAvailable(null, e);
            }

        }).start();

    }

    private static Future<ArrayList<ApiDataResult>> getFuture(List<ApiDataRequest> parameters) {
        Future<ArrayList<ApiDataResult>> future;
        try (ExecutorServiceWrapper executorWrapper = new ExecutorServiceWrapper()) {
            ExecutorService executor = executorWrapper.getExecutor();

            future = executor.submit(() -> {
                ArrayList<ApiDataResult> results = new ArrayList<>();
                for (ApiDataRequest parameter : parameters) {
                    APIOperator op = new APIOperator();
                    results.add(op.getData(parameter));
                }
                return results;
            });
        }
        return future;
    }

    private static class ExecutorServiceWrapper implements AutoCloseable {
        private final ExecutorService executor;

        public ExecutorServiceWrapper() {
            this.executor = Executors.newSingleThreadExecutor();
        }

        public ExecutorService getExecutor() {
            return executor;
        }

        @Override
        public void close() {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }




}
