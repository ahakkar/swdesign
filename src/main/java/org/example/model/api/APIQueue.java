package org.example.model.api;

import org.example.model.data.*;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * APIQueue
 * Singleton class that handles the API queue
 */
public class APIQueue {

    private static APIQueue instance;
    private final LinkedBlockingDeque<ApiDataResult> dequeue;

    private final ArrayList<APIDataListener> listeners;


    private APIQueue() {
        dequeue = new LinkedBlockingDeque<>();
        listeners = new ArrayList<>();
    }

    public static APIQueue getInstance() {

        synchronized (APIQueue.class){
            if (instance == null) {
                instance = new APIQueue();
            }
            return instance;
        }
    }

    public void addListener(APIDataListener listener) {
        listeners.add(listener);
    }

    public boolean removeListener(APIDataListener listener) {
        return listeners.remove(listener);
    }

    public ApiDataResult getApiDataResult() {
        return dequeue.poll();
    }

    public void newDataRequired(ApiDataRequest parameters) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<ApiDataResult> future = executor.submit(() -> APIOperator.getData(parameters));
        executor.shutdown();
        int timeout = 10;

        new Thread(() -> {
            try {

                ApiDataResult result = future.get(timeout, TimeUnit.SECONDS);
                dequeue.add(result);
                notifyListeners();
            } catch (TimeoutException e) {
                System.out.println("API call took longer than " + timeout + " seconds.");
                future.cancel(true); //
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }).start();

    }

    private void notifyListeners() {
        for (APIDataListener listener : listeners) {
            listener.newApiDataAvailable();
        }
    }



}
