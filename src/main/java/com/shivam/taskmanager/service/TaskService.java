package com.shivam.taskmanager.service;

import com.shivam.taskmanager.dto.TaskRequest;
import com.shivam.taskmanager.dto.TaskResponse;
import com.shivam.taskmanager.entity.Task;
import com.shivam.taskmanager.entity.User;
import com.shivam.taskmanager.enums.TaskStatus;
import com.shivam.taskmanager.exception.ResourceNotFoundException;
import com.shivam.taskmanager.repository.TaskRepository;
import com.shivam.taskmanager.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for task CRUD operations.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setPriority(request.getPriority() != null ? request.getPriority() : com.shivam.taskmanager.enums.TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setUser(user);
        taskRepository.save(task);

        return toResponse(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getAllTasksByUser(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return taskRepository.findByUserId(user.getId(), pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        if (!task.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        return toResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        if (!task.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        taskRepository.save(task);
        return toResponse(task);
    }

    @Transactional
    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        if (!task.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasksByStatus(String username, TaskStatus status, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return taskRepository.findByUserIdAndStatus(user.getId(), status, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public long countByStatus(String username, TaskStatus status) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return taskRepository.countByUserIdAndStatus(user.getId(), status);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setUsername(task.getUser().getUsername());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        return response;
    }
}