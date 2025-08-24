package com.example.studentmanagement.repository;

import com.example.studentmanagement.container.BaseTestContainer;
import com.example.studentmanagement.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class StudentRepositoryIntegrationTest extends BaseTestContainer {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testSaveStudent() {
        Student student = new Student("John", "Doe", "john.doe@example.com");
        Student saved = studentRepository.save(student);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void testFindById() {
        Student student = new Student("Jane", "Smith", "jane.smith@example.com");
        Student saved = studentRepository.save(student);
        Optional<Student> found = studentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    void testFindByLastName() {
        Student student = new Student("Alan", "Turing", "alan.turing@example.com");
        studentRepository.save(student);

        var result = studentRepository.findByLastName("Turing");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alan");
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student("Ada", "Lovelace", "ada.lovelace@example.com");
        Student saved = studentRepository.save(student);
        studentRepository.deleteById(saved.getId());

        Optional<Student> result = studentRepository.findById(saved.getId());
        assertThat(result).isNotPresent();
    }
}
