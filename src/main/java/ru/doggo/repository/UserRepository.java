package ru.doggo.repository;

import org.springframework.data.repository.CrudRepository;
import ru.doggo.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByRefreshToken(String refreshToken);
}
