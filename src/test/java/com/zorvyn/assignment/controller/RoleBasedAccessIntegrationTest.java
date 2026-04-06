package com.zorvyn.assignment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.zorvyn.assignment.service.TransactionRecordService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
public class RoleBasedAccessIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // ============================================
    // 1. GET /api/transactions (General Viewing)
    // ADMIN, ANALYST, VIEWER -> OK
    // ============================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanGetAllTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCanGetAllTransactionsWithoutDates() throws Exception {
        mockMvc.perform(get("/api/transactions").param("period", "MONTHLY"))
                .andExpect(status().isOk());
    }

    // ============================================
    // 2. GET /api/transactions (Dates Restriction)
    // strict VIEWER with startDate -> 403 Forbidden
    // ============================================
    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCannotAccessWithExplicitDates() throws Exception {
        mockMvc.perform(get("/api/transactions")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-12-31"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void analystCanAccessWithExplicitDates() throws Exception {
        mockMvc.perform(get("/api/transactions")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-12-31"))
                .andExpect(status().isOk());
    }

    // ============================================
    // 3. POST /api/transactions (Creation)
    // ADMIN -> 200/201, ANALYST/VIEWER -> 403
    // ============================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanCreateTransaction() throws Exception {
        String payload = "{\"amount\": 100, \"date\": \"2024-05-01\", \"type\": \"EXPENSE\", \"category\": \"FOOD\", \"deleted\": false}";
        mockMvc.perform(post("/api/transactions")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk()); // Returns OK per controller logic
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void analystCannotCreateTransaction() throws Exception {
        mockMvc.perform(post("/api/transactions")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCannotCreateTransaction() throws Exception {
        mockMvc.perform(post("/api/transactions")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
    }

    // ============================================
    // 4. GET /api/transactions/deleted (Admin Only)
    // ============================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanViewDeletedTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions/deleted"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCannotViewDeletedTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions/deleted"))
                .andExpect(status().isForbidden());
    }

    // ============================================
    // 5. POST /api/transactions/summary (Analytics)
    // ADMIN/ANALYST -> OK, VIEWER -> 403
    // ============================================
    @Test
    @WithMockUser(roles = "ANALYST")
    void analystCanAccessSummary() throws Exception {
        mockMvc.perform(post("/api/transactions/summary")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"period\": \"MONTHLY\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCannotAccessSummary() throws Exception {
        mockMvc.perform(post("/api/transactions/summary")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"period\": \"MONTHLY\"}"))
                .andExpect(status().isForbidden());
    }

    // ============================================
    // 6. GET /users (User Management)
    // ADMIN -> OK, Others -> 403
    // ============================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanManageUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void analystCannotManageUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCannotManageUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }
}
