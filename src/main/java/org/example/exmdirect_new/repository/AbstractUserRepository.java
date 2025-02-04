package org.example.exmdirect_new.repository;


import org.example.exmdirect_new.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean // Указывает, что этот репозиторий абстрактный и не будет зарегистрирован как бин
public interface AbstractUserRepository<T extends User> extends JpaRepository<T, Long> {

    List<T> findByFirstName(String firstName);

    List<T> findByLastName(String lastName);

    Optional<T> findByLogin(String login);

    List<T> findByFirstNameAndLastName(String firstName, String lastName);
}
