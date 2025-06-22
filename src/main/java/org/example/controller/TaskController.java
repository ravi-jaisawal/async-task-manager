package org.example.controller;

import org.example.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private TaskService service;


    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/all")
    ResponseEntity<?> getTask() {
        List<String> taskList = service.getListOfTask();
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PostMapping("/start")
    public ResponseEntity<String> postTask(
            @RequestParam(defaultValue = "10000") long durationMs,
            @RequestParam(defaultValue = "8000") long timeoutMs
    ) {
        String task = service.startBlockingTask(durationMs, timeoutMs);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PostMapping("/cancel/{taskId}")
    public ResponseEntity<String> cancelTask(@PathVariable String taskId) {
        boolean result = service.cancelTask(taskId);
        if (result) {
            return new ResponseEntity<>("Task cancelled successfully..." + taskId, HttpStatus.OK);
        }
        return new ResponseEntity<>("Task not found or already completed:" + taskId, HttpStatus.OK);
    }

    @PostMapping("/shutdown")
    public ResponseEntity<String> shutdownSystem() {
        service.shutdownExecutors();
        return ResponseEntity.ok("Executors shut down.");
    }
}
