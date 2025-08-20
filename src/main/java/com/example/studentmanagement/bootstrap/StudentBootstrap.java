package com.example.studentmanagement.bootstrap;

import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentBootstrap implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(StudentBootstrap.class);

    private final StudentRepository studentRepository;

    public StudentBootstrap(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            var s1 = new Student("Alice", "Johnson", "alice@example.com");
            var s2 = new Student("Bob", "Williams", "bob@example.com");
            studentRepository.saveAll(List.of(s1, s2));
            log.info("Inserted {} students into MySQL.", studentRepository.count());
        } else {
            log.info("Bootstrap skipped — {} students already present.", studentRepository.count());
        }
    }
}
