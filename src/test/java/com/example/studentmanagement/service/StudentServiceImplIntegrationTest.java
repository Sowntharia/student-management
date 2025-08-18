package com.example.studentmanagement.service;

import com.example.studentmanagement.container.BaseTestContainer;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("testcontainers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentServiceImplIntegrationTest extends BaseTestContainer {

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;

    @BeforeEach
    void setup() {
        testStudent = new Student("Alice", "Smith", "alice@example.com");
        studentRepository.deleteAll(); 
    }

    @Test
    void testSaveStudent() {
        Student saved = studentService.saveStudent(testStudent);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(testStudent.getEmail());
    }

    @Test
    void testGetAllStudents() {
        studentService.saveStudent(testStudent);

        List<Student> students = studentService.getAllStudents();

        assertThat(students).hasSize(1);
        assertThat(students.get(0).getEmail()).isEqualTo(testStudent.getEmail());
    }

    @Test
    void testGetStudentById() {
        Student saved = studentService.saveStudent(testStudent);
        Optional<Student> found = studentService.getStudentById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(testStudent.getEmail());
    }

    @Test
    void testUpdateStudent() {
        Student saved = studentService.saveStudent(testStudent);
        Student updated = new Student("Bob", "Johnson", "bob@example.com");

        Student result = studentService.updateStudent(saved.getId(), updated);

        assertThat(result.getFirstName()).isEqualTo("Bob");
        assertThat(result.getLastName()).isEqualTo("Johnson");
        assertThat(result.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void testDeleteStudent() {
        Student saved = studentService.saveStudent(testStudent);
        studentService.deleteStudent(saved.getId());

        assertThat(studentRepository.findById(saved.getId())).isEmpty();

    }

    @Test
    void testFindByLastName() {
        studentService.saveStudent(testStudent);
        List<Student> found = studentService.getStudentsByLastName("Smith");

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getFirstName()).isEqualTo("Alice");
    }
}
