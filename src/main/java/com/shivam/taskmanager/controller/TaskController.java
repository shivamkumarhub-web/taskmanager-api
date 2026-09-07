package com.shivam.taskmanager.controller;

import com.shivam.taskmanager.dto.TaskRequest;
import com.shivam.taskmanager.dto.TaskResponse;
import com.shivam.taskmanager.enums.TaskStatus;
import com.shivam.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for task CRUD operations.
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks for the authenticated user (paginated)")
    public ResponseEntity<Page<TaskResponse>> getAllTasks(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasksByUser(auth.getName(), pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(taskService.getTaskById(id, auth.getName()));
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request, Authentication auth) {
        return ResponseEntity.ok(taskService.createTask(request, auth.getName()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request, Authentication auth) {
        return ResponseEntity.ok(taskService.updateTask(id, request, auth.getName()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication auth) {
        taskService.deleteTask(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filter tasks by status")
    public ResponseEntity<Page<TaskResponse>> getTasksByStatus(@PathVariable TaskStatus status, Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByStatus(auth.getName(), status, pageable));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics (task counts by status)")
    public ResponseEntity<Map<String, Long>> getStats(Authentication auth) {
        Map<String, Long> stats = new HashMap<>();
        for (TaskStatus status : TaskStatus.values()) {
            stats.put(status.name(), taskService.countByStatus(auth.getName(), status));
        }
        return ResponseEntity.ok(stats);
    }
}