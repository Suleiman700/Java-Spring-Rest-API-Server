package com.mvc.api.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // SELECT * FROM students WHERE name = ?
//    @Query("SELECT s FROM Student s WHERE s.name = ?1")
    Student findByName(String name);
    Student findByEmail(String email);
}