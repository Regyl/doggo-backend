package ru.doggo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.doggo.model.Post;
import ru.doggo.model.User;
import ru.doggo.repository.PostRepository;
import ru.doggo.repository.UserRepository;

@SpringBootApplication
public class DoggoBackendApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(DoggoBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(PostRepository posts, UserRepository users) {
        return args -> {
            posts.save(new Post("Title1", "Description1", true, "Content1"));
            posts.save(new Post("Title2", "Description2", true, "Content2"));
            posts.save(new Post("Title3", "Description3", true, "Content3"));
            posts.save(new Post("Title4", "Description4", true, "Content4"));

            users.save(new User("user", passwordEncoder.encode("password"), "READ,ROLE_USER"));
            users.save(new User("admin", passwordEncoder.encode("password"), "READ,ROLE_USER,ROLE_ADMIN"));
        };
    }

}
