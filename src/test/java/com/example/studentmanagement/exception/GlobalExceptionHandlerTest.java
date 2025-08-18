package com.example.studentmanagement.exception;

import com.example.studentmanagement.controller.StudentController;
import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings({ "unused" })
@WebMvcTest(controllers = StudentController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void testResourceNotFoundException() throws Exception {
        Mockito.when(studentService.getStudentById(anyLong()))
                .thenThrow(new StudentNotFoundException("Student not found with id 999"));

        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found with id 999"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testValidationException() throws Exception {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName(""); // invalid
        studentDTO.setLastName("ValidLast");
        studentDTO.setEmail("valid@example.com");

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Student Name shouldn't be empty"));
    }

    @Test
    void testGenericException() throws Exception {
        
        Mockito.when(studentService.getStudentById(anyLong()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }
}
