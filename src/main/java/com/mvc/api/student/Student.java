package com.mvc.api.student;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jdk.jfr.Enabled;

@Enabled
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    @NotBlank(message = "name shouldn't be empty")
    @Size(min = 2, message = "name must be at least 2 characters")
    private String name;
    @Column(name = "email")
    @NotBlank(message = "email shouldn't be empty")
    @Email
    private String email;
    @Column(name = "age")
    @NotNull
    private Integer age;

    public Student() {
    }

    public Student(Integer id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Student(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
