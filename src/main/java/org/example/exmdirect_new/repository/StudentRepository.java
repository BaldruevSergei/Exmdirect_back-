package org.example.exmdirect_new.repository;


import org.example.exmdirect_new.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends AbstractUserRepository<Student> {

    // Поиск студентов по классу
    List<Student> findBySchoolClass_Id(Long classId);

    // Поиск студентов, которых обучает конкретный учитель
    @Query("""
        SELECT DISTINCT s FROM Student s
        JOIN s.schoolClass sc
        JOIN ClassSubject cs ON cs.schoolClass = sc
        WHERE cs.teacher.id = :teacherId
    """)
    List<Student> findStudentsByTeacherId(@Param("teacherId") Long teacherId);
}
