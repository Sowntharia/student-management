package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
	
    Student saveStudent(Student student);
    
    List<Student> getAllStudents();
    
    Optional<Student> getStudentById(Long id);
    
    Student updateStudent(Long id, Student student);
    
    void deleteStudent(Long id);
    
    List<Student> getStudentsByLastName(String lastName);
}
