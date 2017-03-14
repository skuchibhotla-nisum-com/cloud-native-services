package com.nisum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.stream.Stream;

@EnableDiscoveryClient
@SpringBootApplication
public class ProfileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileServiceApplication.class, args);
	}
}


@RestController
@RefreshScope
@RequestMapping("/message")
class UserMessageController {

	private final String message;

	public UserMessageController(@Value("${message}") String message) {
		this.message = message;
	}

	@GetMapping
	public String readMessage() {
		return message;
	}
}

@Component
class SampleDataToLoadCLR implements CommandLineRunner {

	private final UserRepository userRepository;

	@Autowired
	public SampleDataToLoadCLR(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		Stream.of("Harish","Vijay","Priyanka","Pranitha","Spring","Boot").forEach( name -> userRepository.save(new
				User(name)));
		userRepository.findAll().forEach(System.out::println);
	}
}


@RepositoryRestResource
interface UserRepository extends JpaRepository<User, Long> {



}

@Entity
class User {

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	public User() { //only for JPA
	}

	public User(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
