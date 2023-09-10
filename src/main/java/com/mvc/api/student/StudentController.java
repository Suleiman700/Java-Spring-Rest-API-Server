package com.mvc.api.student;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @PostMapping(path = "/create")
    public void createNewStudent(@RequestBody @Valid Student student) throws IllegalAccessException {

        studentService.addNewStudent(student);
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteStudent(@PathVariable Integer id) throws IllegalAccessException {
        studentService.deleteStudent(id);
    }

    @PutMapping(path = "/update/{id}")
    public void updateStudent(@PathVariable Integer id, @RequestBody Student student) throws IllegalAccessException {
        studentService.updateStudent(id, student);
    }


    @GetMapping("/find/id/{id}")
    public Optional<Student> findStudentById(@PathVariable Integer id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/find/name/{name}")
    public Optional<Student> findStudentByName(@PathVariable String name) {
        return studentService.getStudentByName(name);
    }
}
