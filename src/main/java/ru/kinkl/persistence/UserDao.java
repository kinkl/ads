package ru.kinkl.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kinkl.model.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
    List<User> findAll();

    User findByNameIgnoreCase(String name);
}
