package com.example.studentmanagement.util;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.model.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentConverterTest {

	@Test
	void testConvertToEntity_AllFields() {
	    StudentDTO dto = new StudentDTO();
	    dto.setId(1L);
	    dto.setFirstName("TestFirst");
	    dto.setLastName("TestLast");
	    dto.setEmail("test@example.com");

	    Student student = StudentConverter.convertToEntity(dto);

	    assertNotNull(student);
	    assertEquals(1L, student.getId());
	    assertEquals("TestFirst", student.getFirstName());
	    assertEquals("TestLast", student.getLastName());
	    assertEquals("test@example.com", student.getEmail());
	}

    @Test
    void testConvertToDTO() {
        Student student = new Student();
        student.setId(2L);
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student.setEmail("alice@example.com");

        StudentDTO dto = StudentConverter.convertToDTO(student);
        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Alice", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("alice@example.com", dto.getEmail());
    }

    @Test
    void testConvertToEntity_nullInput() {
        assertNull(StudentConverter.convertToEntity(null));
    }

    @Test
    void testConvertToDTO_nullInput() {
        assertNull(StudentConverter.convertToDTO(null));
    }
}
