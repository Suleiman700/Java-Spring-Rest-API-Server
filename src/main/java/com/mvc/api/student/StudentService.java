package com.mvc.api.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {

        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByName(String name) {
        return Optional.ofNullable(studentRepository.findByName(name));
    }

    public void addNewStudent(Student student) throws IllegalAccessException {
        // Check if student email exist
        Optional<Student> studentByEmail = Optional.ofNullable(studentRepository.findByEmail(student.getEmail()));
        if (studentByEmail.isPresent()) {
            throw new IllegalAccessException("Email taken!");
        }
        else {
            studentRepository.save(student);
        }
    }

    @Transactional
    public void updateStudent(Integer id, Student newStudentData) throws IllegalAccessException {
        // Check if student exist
        Student studentData = studentRepository.findById(id).orElseThrow(() -> new IllegalAccessException("student with id " + id + " does not exist"));

        // Check name
        if (newStudentData.getName() != null && !newStudentData.getName().isEmpty() && !Objects.equals(studentData.getName(), newStudentData.getName())) {
            studentData.setName(newStudentData.getName());
        }

        // Check email
        if (newStudentData.getEmail() != null && !newStudentData.getEmail().isEmpty() && !Objects.equals(studentData.getEmail(), newStudentData.getEmail())) {
            Optional<Student> studentOptional = Optional.ofNullable(studentRepository.findByEmail(newStudentData.getEmail()));
            if (studentOptional.isPresent()) {
                throw new IllegalAccessException("email taken");
            }
            studentData.setEmail(newStudentData.getEmail());
        }

        // Check age
        if (newStudentData.getAge() != null && !Objects.equals(studentData.getAge(), newStudentData.getAge())) {
            studentData.setAge(newStudentData.getAge());
        }
    }

    public void deleteStudent(Integer id) throws IllegalAccessException {
        // Check if student exist
        boolean exists = studentRepository.existsById(id);
        if (!exists) {
            throw new IllegalAccessException("student with id " + id + " does not exists");
        }
        studentRepository.deleteById(id);
    }
}
