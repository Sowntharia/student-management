package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import com.example.studentmanagement.util.StudentConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PageController {

    private final StudentService studentService;

    public PageController(StudentService studentService) {
        this.studentService = studentService;
    }

    // LIST
    @GetMapping("/students")
    public String list(Model model) {
        List<StudentDTO> students = studentService.getAllStudents()
                .stream().map(StudentConverter::convertToDTO).toList();
        model.addAttribute("students", students);
        return "students"; // renders templates/students.html
    }

    // CREATE (form)
    @GetMapping("/students/new")
    public String newForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        model.addAttribute("mode", "create");
        return "student-form"; // renders templates/student-form.html
    }

    // CREATE (submit)
    @PostMapping("/students")
    public String create(@ModelAttribute("student") StudentDTO dto) {
        Student entity = StudentConverter.convertToEntity(dto);
        studentService.saveStudent(entity);
        return "redirect:/students";
    }

    // EDIT (form)
    @GetMapping("/students/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Student entity = studentService.getStudentById(id).orElseThrow();
        model.addAttribute("student", StudentConverter.convertToDTO(entity));
        model.addAttribute("mode", "edit");
        return "student-form";
    }

    // EDIT (submit)
    @PostMapping("/students/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("student") StudentDTO dto) {
        studentService.updateStudent(id, StudentConverter.convertToEntity(dto));
        return "redirect:/students";
    }

    // DELETE
    @PostMapping("/students/{id}/delete")
    public String delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
