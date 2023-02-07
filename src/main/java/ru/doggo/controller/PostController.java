package ru.doggo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.doggo.model.Post;
import ru.doggo.repository.PostRepository;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postsRepo;

    public PostController(PostRepository postsRepo) {
        this.postsRepo = postsRepo;
    }

    @GetMapping("/")
    public String home(Principal principal) {
        return "Hello, " + principal.getName();
    }

    @GetMapping("/all")
    public Iterable<Post> findAll() {
        return postsRepo.findAll();
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable("id") Post post) {
        return post;
    }

}
