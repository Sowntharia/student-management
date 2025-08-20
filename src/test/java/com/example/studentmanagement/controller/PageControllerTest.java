package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PageController.class)
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void list_shouldRenderStudents_withModelFromService() throws Exception {
        Student s1 = new Student(); s1.setId(1L); s1.setFirstName("Alice"); s1.setLastName("Smith"); s1.setEmail("a@x.com");
        Student s2 = new Student(); s2.setId(2L); s2.setFirstName("Bob");   s2.setLastName("Jones"); s2.setEmail("b@x.com");
        given(studentService.getAllStudents()).willReturn(List.of(s1, s2));

        mockMvc.perform(get("/students"))
               .andExpect(status().isOk())
               .andExpect(view().name("students"))
               .andExpect(model().attributeExists("students"))
               .andExpect(model().attribute("students", hasSize(2)));
    }

    @Test
    void newForm_shouldRenderStudentForm_inCreateMode() throws Exception {
        mockMvc.perform(get("/students/new"))
               .andExpect(status().isOk())
               .andExpect(view().name("student-form"))
               .andExpect(model().attributeExists("student"))
               .andExpect(model().attribute("mode", "create"));
    }

    @Test
    void create_shouldCallSave_andRedirectToList() throws Exception {
        Student saved = new Student();
        saved.setId(99L); saved.setFirstName("John"); saved.setLastName("Doe"); saved.setEmail("john@doe.com");
        given(studentService.saveStudent(any(Student.class))).willReturn(saved);

        mockMvc.perform(post("/students")
                    .param("firstName", "John")
                    .param("lastName", "Doe")
                    .param("email", "john@doe.com"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/students"));

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentService, times(1)).saveStudent(captor.capture());
        Student sent = captor.getValue();
        assertThat(sent.getId()).as("id for new create").isNull();
        assertThat(sent.getFirstName()).isEqualTo("John");
        assertThat(sent.getLastName()).isEqualTo("Doe");
        assertThat(sent.getEmail()).isEqualTo("john@doe.com");
    }

    @Test
    void editForm_shouldLoadStudent_andRenderFormInEditMode() throws Exception {
        Student existing = new Student();
        existing.setId(5L); existing.setFirstName("Eva"); existing.setLastName("Green"); existing.setEmail("eva@x.com");
        given(studentService.getStudentById(5L)).willReturn(Optional.of(existing));

        mockMvc.perform(get("/students/5/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("student-form"))
               .andExpect(model().attributeExists("student"))
               .andExpect(model().attribute("mode", "edit"))
               
               .andExpect(model().attribute("student", allOf(
                       hasProperty("firstName", equalTo("Eva")),
                       hasProperty("lastName",  equalTo("Green")),
                       hasProperty("email",     equalTo("eva@x.com"))
               )));
    }

    @Test
    void update_shouldCallUpdate_andRedirect() throws Exception {
        Student updated = new Student();
        updated.setId(7L); updated.setFirstName("Up"); updated.setLastName("Dated"); updated.setEmail("up@dated.com");
        given(studentService.updateStudent(eq(7L), any(Student.class))).willReturn(updated);

        mockMvc.perform(post("/students/7")
                    .param("firstName", "Up")
                    .param("lastName",  "Dated")
                    .param("email",     "up@dated.com"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/students"));

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentService, times(1)).updateStudent(eq(7L), captor.capture());
        Student sent = captor.getValue();
        assertThat(sent.getFirstName()).isEqualTo("Up");
        assertThat(sent.getLastName()).isEqualTo("Dated");
        assertThat(sent.getEmail()).isEqualTo("up@dated.com");
    }

    @Test
    void delete_shouldCallDelete_andRedirect() throws Exception {
        mockMvc.perform(post("/students/9/delete"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).deleteStudent(9L);
    }
}
