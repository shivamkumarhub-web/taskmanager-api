package com.shivam.taskmanager.service;

import com.shivam.taskmanager.dto.TaskRequest;
import com.shivam.taskmanager.dto.TaskResponse;
import com.shivam.taskmanager.entity.Task;
import com.shivam.taskmanager.entity.User;
import com.shivam.taskmanager.enums.TaskPriority;
import com.shivam.taskmanager.enums.TaskStatus;
import com.shivam.taskmanager.exception.ResourceNotFoundException;
import com.shivam.taskmanager.repository.TaskRepository;
import com.shivam.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaskService using Mockito mocks.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private TaskService taskService;

    @Test
    void createTask_shouldSaveAndReturnResponse() {
        User user = new User("testuser", "test@example.com", "encodedpass");
        user.setId(1L);

        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setDescription("Description");
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.HIGH);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        TaskResponse response = taskService.createTask(request, "testuser");

        assertEquals("New Task", response.getTitle());
        assertEquals("TODO", response.getStatus().name());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTaskById_notFound_shouldThrowException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                taskService.getTaskById(99L, "testuser"));
    }

    @Test
    void deleteTask_shouldCallRepositoryDelete() {
        User user = new User("testuser", "test@example.com", "encodedpass");
        user.setId(1L);

        Task task = new Task("Task to delete", "Desc", TaskStatus.TODO, TaskPriority.LOW);
        task.setId(1L);
        task.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, "testuser");

        verify(taskRepository, times(1)).delete(task);
    }
}