package com.example.studentmanagement;

import com.example.studentmanagement.container.BaseTestContainer;
import com.example.studentmanagement.controller.StudentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("testcontainers")
class StudentmanagementApplicationTests extends BaseTestContainer {

	@Autowired
	private StudentController studentController;

	@Test
	void contextLoads() {
		
		assertThat(studentController).isNotNull();
	}
}
