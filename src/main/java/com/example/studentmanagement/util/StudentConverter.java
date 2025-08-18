package com.example.studentmanagement.util;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.model.Student;

public class StudentConverter {

    private StudentConverter() {
        // Utility class prevent instantiation
    }

    public static Student convertToEntity(StudentDTO dto) {
        if (dto == null) return null;
        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        return student;
    }

    public static StudentDTO convertToDTO(Student student) {
        if (student == null) return null;
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        return dto;
    }
}
