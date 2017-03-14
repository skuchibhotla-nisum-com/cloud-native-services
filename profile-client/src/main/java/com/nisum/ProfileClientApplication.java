package com.nisum;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@EnableFeignClients
@EnableZuulProxy
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
public class ProfileClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileClientApplication.class, args);
	}
}


//How can i make it more than just a proxy????

@RestController
@RequestMapping("/users")
class ProfileApiGatewayController {

	private final UsersReader usersReader;

	@Autowired
	public ProfileApiGatewayController(UsersReader usersReader) {
		this.usersReader = usersReader;
	}

	private Collection<String> fallbackForNames() {
		return new ArrayList<>();
	}

	@HystrixCommand(fallbackMethod = "fallbackForNames")
	@GetMapping("/names")
	public Collection<String> readFirstNames() {
		return usersReader.readNames().getContent().stream().map(User::getName).collect(Collectors.toList());
	}
}

@FeignClient("profile-service")
interface UsersReader {

	@RequestMapping(method = RequestMethod.GET, value="/users")
	Resources<User> readNames();
}

class User {

	private String name;

	public User() {
	}

	public User(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
