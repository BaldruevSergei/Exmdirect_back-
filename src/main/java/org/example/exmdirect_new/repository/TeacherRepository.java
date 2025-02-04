package org.example.exmdirect_new.repository;


import org.example.exmdirect_new.entity.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends AbstractUserRepository<Teacher> {

    // Найти учителя по предмету
    List<Teacher> findBySubject(String subject);

    // Найти учителя, который является классным руководителем указанного класса
    Optional<Teacher> findByClassMentor(String classMentor);

    // Найти учителей, ведущих определенный предмет (игнорируя регистр)
    List<Teacher> findBySubjectIgnoreCase(String subject);

    // Найти всех учителей, преподающих в указанном классе (по ID класса)
    @Query("""
        SELECT DISTINCT t FROM Teacher t
        JOIN t.classSubjects cs
        WHERE cs.schoolClass.id = :classId
    """)
    List<Teacher> findTeachersByClassId(@Param("classId") Long classId);
}
