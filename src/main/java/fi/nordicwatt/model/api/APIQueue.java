package fi.nordicwatt.model.api;

import java.util.ArrayList;
import java.util.concurrent.*;

import fi.nordicwatt.Constants;
import fi.nordicwatt.model.data.*;
import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.model.datamodel.ResponseBundle;

/**
 * APIQueue
 * Singleton class that handles the API queue
 * 
 * @author ???
 */
public class APIQueue {

    private static APIQueue instance;
    private final  ArrayList<APIDataListener> listeners;

    private APIQueue() {
        this.listeners = new ArrayList<>();
    }

    public static APIQueue getInstance() {
        if (instance == null) {
            synchronized (APIQueue.class) {
                if (instance == null) {
                    instance = new APIQueue();
                }
            }
        }
        return instance;
    }

    public void getData(RequestBundle requests)
    {
        Future<ResponseBundle> future = getFuture(requests);
        new Thread(() -> {
            try {
                ResponseBundle responses = future.get(Constants.API_TIMEOUT, TimeUnit.SECONDS);
                System.out.println(responses.toString());
                notifyDataRequestSuccess(responses);

            } catch (Exception e) {
                future.cancel(true);
                // TODO log exception
                notifyDataRequestFailure(requests, e);
            }

        }).start();

    }

    private Future<ResponseBundle> getFuture(RequestBundle requests) {
        Future<ResponseBundle> future;
        try (ExecutorServiceWrapper executorWrapper = new ExecutorServiceWrapper()) {
            ExecutorService executor = executorWrapper.getExecutor();

            future = executor.submit(() -> {
               ResponseBundle responses = new ResponseBundle(requests.getId());
               for (DataRequest request : requests.getItems()) {
                    APIOperator op = new APIOperator();                    
                    responses.addItem(op.getData(request)); 
                }
                return responses;
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


    /**
     * Notify listeners that data request was successful, and provide the responses
     * @param responses Bundle of responses containing data for each request
     */
    public void notifyDataRequestSuccess(ResponseBundle responses) {
        for (APIDataListener listener : listeners) {
            listener.APIDataRequestSuccess(responses);
        }
    }


    /**
     * Notify listeners that data request failed
     * @param requests Bundle of requests containing their individual Status
     * @param e        Exception that caused the failure
     */
    public void notifyDataRequestFailure(RequestBundle requests, Exception e) {
        for (APIDataListener listener : listeners) {
            listener.APIDataRequestFailure(requests, e);
        }
    }

    /**
     * Use to register a class as a listener
     * @param listener
     */
    public void registerListener(APIDataListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }


    /**
     * Remove a listener
     * @param listener
     */
    public void removeListener(APIDataListener listener) {
        listeners.remove(listener);
    }
}
