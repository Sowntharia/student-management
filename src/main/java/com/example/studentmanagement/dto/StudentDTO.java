package com.example.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentDTO {

    private Long id;
    
    @NotBlank(message = "Student Name shouldn't be empty")
    private String firstName;
    @NotBlank(message = "Student Last Name shouldn't be empty")
    private String lastName;
    @Email
    @NotBlank(message = "Student emailid shouldn't be empty")
    private String email;

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
