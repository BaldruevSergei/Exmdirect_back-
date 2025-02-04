package org.example.exmdirect_new.service;



import org.example.exmdirect_new.entity.User;
import org.example.exmdirect_new.repository.AbstractUserRepository;

import java.util.List;
import java.util.Optional;

public class AbstractUserService<T extends User> {

    protected final AbstractUserRepository<T> userRepository;

    public AbstractUserService(AbstractUserRepository<T> userRepository) {
        this.userRepository = userRepository;
    }

    public List<T> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<T> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<T> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public List<T> getUsersByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public List<T> getUsersByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    public List<T> getUsersByFullName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public T saveUser(T user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
