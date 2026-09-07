package com.shivam.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivam.taskmanager.dto.LoginRequest;
import com.shivam.taskmanager.dto.RegisterRequest;
import com.shivam.taskmanager.dto.TaskRequest;
import com.shivam.taskmanager.enums.TaskPriority;
import com.shivam.taskmanager.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for TaskController using MockMvc with JWT auth.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class TaskControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        String uniqueUser = "taskuser" + System.currentTimeMillis();
        RegisterRequest register = new RegisterRequest();
        register.setUsername(uniqueUser);
        register.setEmail(uniqueUser + "@example.com");
        register.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        LoginRequest login = new LoginRequest();
        login.setUsername(uniqueUser);
        login.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        jwtToken = objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.HIGH);

        mockMvc.perform(post("/api/tasks")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void getTasks_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getTasks_withAuth_shouldReturnPaginatedResults() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Page Test Task");

        mockMvc.perform(post("/api/tasks")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getTaskStats_shouldReturnStatusCounts() throws Exception {
        mockMvc.perform(get("/api/tasks/stats")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.TODO").exists())
                .andExpect(jsonPath("$.DONE").exists());
    }
}