package org.example.exmdirect_new.repository;


import org.example.exmdirect_new.entity.SchoolClass;
import org.example.exmdirect_new.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {

    // Найти класс по ID
    Optional<SchoolClass> findById(Long id);

    // Найти все классы, которые ведёт определённый классный руководитель
    List<SchoolClass> findByClassTeacher(Teacher classTeacher);
}
