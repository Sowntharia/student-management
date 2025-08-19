package com.example.studentmanagement;

import com.example.studentmanagement.container.BaseTestContainer;
import com.example.studentmanagement.controller.StudentController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentmanagementApplicationTests extends BaseTestContainer {

	@Autowired
	StudentController studentController;

	@Test
	void contextLoads() {
		
		assertNotNull(studentController);
	}
}
