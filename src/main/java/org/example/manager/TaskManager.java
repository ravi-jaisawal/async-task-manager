package org.example.manager;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class TaskManager {

    ExecutorService executor = Executors.newCachedThreadPool();
    ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
    ConcurrentHashMap<String, Future<?>> taskRegistry = new ConcurrentHashMap<>();

    public <T> CompletableFuture<T> submitTask(String taskId, Callable<T> task, long timeOut) {
        // result to store
        CompletableFuture<T> resultFuture = new CompletableFuture<>();
        // run the task
        Future<T> future = executor.submit(() -> {
            try {
                T result = task.call();
                resultFuture.complete(result);
                return result;
            } catch (InterruptedException e) {
                resultFuture.completeExceptionally(new CancellationException("Interrupted"));
                throw e;
            } catch (Exception e) {
                resultFuture.completeExceptionally(e);
                throw e;
            } finally {
                taskRegistry.remove(taskId);
            }
        });
        // store the task
        taskRegistry.put(taskId, future);
        // scheduler part handler
        scheduler.schedule(() -> {
            if (!resultFuture.isDone()) {
                future.cancel(true);
                resultFuture.completeExceptionally(new CancellationException("Interrupted"));
                taskRegistry.remove(taskId);
            }
        }, timeOut, TimeUnit.MILLISECONDS);

        return resultFuture;
    }

    public boolean cancelTask(String taskId) {
        Future<?> future = taskRegistry.get(taskId);
        if (future != null) {
            if (!future.isDone()) {
                future.cancel(true);
                return true;
            }
        }
        return false;
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        scheduler.shutdown();
    }


    public List<String> getListOfTask() {
        List<String> taskList = new ArrayList<>(taskRegistry.keySet());
        return taskList;
    }
}
