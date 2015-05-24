package ru.kinkl.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kinkl.model.Authority;

import java.util.List;

public interface AuthorityDao extends JpaRepository<Authority, Long> {
    List<Authority> findAll();
}
