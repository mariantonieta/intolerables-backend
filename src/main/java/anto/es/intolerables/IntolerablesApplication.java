package anto.es.intolerables;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages = "anto.es.intolerables.entities")
@EnableJpaRepositories(basePackages = "anto.es.intolerables.repositories")
@SpringBootApplication
public class IntolerablesApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntolerablesApplication.class, args);
    }

}
