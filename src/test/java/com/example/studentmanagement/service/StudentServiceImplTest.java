package com.example.studentmanagement.service;

import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student();
        student.setId(1L);
        student.setFirstName("Alice");
        student.setLastName("Johnson");
        student.setEmail("alice@example.com");
    }

    @Test
    void testSaveStudent() {
        when(studentRepository.save(student)).thenReturn(student);
        Student saved = studentService.saveStudent(student);
        assertEquals("Alice", saved.getFirstName());
        verify(studentRepository).save(student);
    }

    @Test
    void testGetAllStudents() {
        List<Student> list = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(list);
        List<Student> result = studentService.getAllStudents();
        assertEquals(1, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void testGetStudentById_found() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Optional<Student> result = studentService.getStudentById(1L);
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
        verify(studentRepository).findById(1L);
    }

    @Test
    void testGetStudentById_notFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById(99L));
        verify(studentRepository).findById(99L);
    }

    @Test
    void testUpdateStudent_found() {
        Student updated = new Student();
        updated.setFirstName("Updated");
        updated.setLastName("User");
        updated.setEmail("updated@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.updateStudent(1L, updated);

        assertEquals("Updated", result.getFirstName());
        assertEquals("User", result.getLastName());         
        assertEquals("updated@example.com", result.getEmail()); 
        
        verify(studentRepository).save(any(Student.class));
    }


    @Test
    void testUpdateStudent_notFound() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        Student result = studentService.updateStudent(2L, student);
        assertNull(result);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentRepository).deleteById(1L);
        studentService.deleteStudent(1L);
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void testGetStudentsByLastName() {
        List<Student> list = Arrays.asList(student);
        when(studentRepository.findByLastName("Johnson")).thenReturn(list);
        List<Student> result = studentService.getStudentsByLastName("Johnson");
        assertEquals(1, result.size());
        assertEquals("Johnson", result.get(0).getLastName());
        verify(studentRepository).findByLastName("Johnson");
    }
}
