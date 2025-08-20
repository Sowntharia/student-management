package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import com.example.studentmanagement.util.StudentConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    private static final String REDIRECT_STUDENTS   = "redirect:/students";
    private static final String VIEW_STUDENTS       = "students";
    private static final String VIEW_STUDENT_FORM   = "student-form";

    private final StudentService studentService;

    public PageController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String list(Model model) {
        model.addAttribute("students",
            studentService.getAllStudents()
                          .stream()
                          .map(StudentConverter::convertToDTO)
                          .toList()); // Java 16+ idiomatic
        return VIEW_STUDENTS;
    }

    @GetMapping("/students/new")
    public String newForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        model.addAttribute("mode", "create");
        return VIEW_STUDENT_FORM;
    }

    @PostMapping("/students")
    public String create(@ModelAttribute("student") StudentDTO dto) {
        Student entity = StudentConverter.convertToEntity(dto);
        studentService.saveStudent(entity);
        return REDIRECT_STUDENTS;
    }

    @GetMapping("/students/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Student entity = studentService.getStudentById(id).orElseThrow();
        model.addAttribute("student", StudentConverter.convertToDTO(entity));
        model.addAttribute("mode", "edit");
        return VIEW_STUDENT_FORM;
    }

    @PostMapping("/students/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("student") StudentDTO dto) {
        studentService.updateStudent(id, StudentConverter.convertToEntity(dto));
        return REDIRECT_STUDENTS;
    }

    @PostMapping("/students/{id}/delete")
    public String delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return REDIRECT_STUDENTS;
    }
}
