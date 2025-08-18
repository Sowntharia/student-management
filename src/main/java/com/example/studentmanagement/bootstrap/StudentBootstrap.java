package com.example.studentmanagement.bootstrap;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StudentBootstrap implements CommandLineRunner {

    private final StudentRepository studentRepository;

    public StudentBootstrap(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            Student s1 = new Student("Alice", "Johnson", "alice@example.com");
            Student s2 = new Student("Bob", "Williams", "bob@example.com");

            studentRepository.save(s1);
            studentRepository.save(s2);

            System.out.println("Inserted students into MySQL.");
        }
    }
}
