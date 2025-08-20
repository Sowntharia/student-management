package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import com.example.studentmanagement.util.StudentConverter;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

import static com.example.studentmanagement.util.StudentConverter.*;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentController {

    
    private StudentService studentService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    // Create student
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        Student student = convertToEntity(studentDTO);
        Student saved = studentService.saveStudent(student);
        return ResponseEntity.ok(convertToDTO(saved));
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents()
                .stream()
                .map(StudentConverter::convertToDTO)
                .toList();
        return ResponseEntity.ok(students);
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with ID " + id + " not found."));
        return ResponseEntity.ok(convertToDTO(student));
    }

    // Update student
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        Student student = convertToEntity(studentDTO);
        Student updated = studentService.updateStudent(id, student);
        if (updated == null) {
            throw new StudentNotFoundException("Student with ID " + id + " not found.");
        }
        return ResponseEntity.ok(convertToDTO(updated));
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // Find students by last name
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<StudentDTO>> getByLastName(@PathVariable String lastName) {
        List<StudentDTO> result = studentService.getStudentsByLastName(lastName)
                .stream()
                .map(StudentConverter::convertToDTO)
                .toList();
        return ResponseEntity.ok(result);
    }
}
