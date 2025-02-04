package org.example.exmdirect_new.repository;


import org.example.exmdirect_new.entity.ClassSubject;
import org.example.exmdirect_new.entity.SchoolClass;
import org.example.exmdirect_new.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long> {

    // Найти все предметы, которые ведутся в определённом классе
    List<ClassSubject> findBySchoolClass(SchoolClass schoolClass);

    // Найти все предметы, которые преподаёт конкретный учитель
    List<ClassSubject> findByTeacher(Teacher teacher);

    // Найти предмет по его названию
    List<ClassSubject> findBySubject(String subject);

    // Найти предметы, которые ведёт конкретный учитель в указанном классе
    List<ClassSubject> findByTeacherAndSchoolClass(Teacher teacher, SchoolClass schoolClass);
}
