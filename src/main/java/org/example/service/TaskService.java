package org.example.service;

import org.example.manager.TaskManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {

    private TaskManager taskManager;

    public TaskService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public String startBlockingTask(long durationMs, long timeoutMs) {
        String taskId = UUID.randomUUID().toString();
        CompletableFuture<String> future = this.taskManager.submitTask(taskId, () -> {
            long endTime = System.currentTimeMillis() + durationMs;
            System.out.println("System.currentTimeMillis() " + System.currentTimeMillis());
            System.out.println("System.currentTimeMillis() " + endTime);

            while (System.currentTimeMillis() < endTime) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("Task interrupted");
                }
                Thread.sleep(500);
            }
            return "Task [" + taskId + "] completed";
        }, timeoutMs);

        future.whenComplete((res, ex) -> {
            if (ex != null) {
                System.out.println("Task [" + taskId + "] failed: " + ex.getMessage());
            } else {
                System.out.println("Task [" + taskId + "] finished: " + res);
            }
        });
        return taskId;
    }

    public boolean cancelTask(String taskId) {
        return this.taskManager.cancelTask(taskId);
    }

    public List<String> getListOfTask() {
        return this.taskManager.getListOfTask();
    }

    public void shutdownExecutors() {
        this.taskManager.shutdown();
    }

}
