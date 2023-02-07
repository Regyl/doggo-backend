package ru.doggo.repository;

import org.springframework.data.repository.CrudRepository;
import ru.doggo.model.Post;

public interface PostRepository extends CrudRepository<Post, Long> {
}
