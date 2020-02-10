package com.springboot.consume.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springboot.consume.rest.data.Employee;

@RestController
@SpringBootApplication
@RequestMapping("/restApi")
public class SpringbootConsumeRestExampleApplication implements CommandLineRunner{

	private RestTemplate restTemplate;
	
	/*@Value("${endPoint}")*/
	public String endPoint;
	
	public SpringbootConsumeRestExampleApplication() {
		this.restTemplate = new RestTemplate();
	}
	
	@Autowired
	private MyConfig config;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootConsumeRestExampleApplication.class, args);
	}
	
	
	// Reading endPoint from YAML file 
	public void run(String...strings) {
		endPoint = config.endPoint;
		System.out.println("endPoint is ----> "+endPoint);
	}
	
	// Start: Created a Rest Service http://localhost:8080/restApi/getEmployeeDetails
	
	@GetMapping("/getEmployeeDetails")
	public Employee getEmployeeDetails() {
		
		Employee employee = createEmployee();
		
		return employee;
	}
	
	private Employee createEmployee() {
		
		Employee employee = new Employee();
		
		employee.setId(1);
		employee.setFirstName("Jyotsna");
		employee.setLastName("Kotakinda");
		
		return employee;
	}
	@PostMapping("/getEmployeeDetails")
	public Employee getEmployeeDetails(@RequestBody Employee employeeObj) {
		return employeeObj;
	}
	
	// End: Created a Rest Service http://localhost:8080/restApi/getEmployeeDetails


	// Start: Consuming a Rest Service using Rest Template
	
	@GetMapping("/getForObjectOperation")
	public Employee getForObjectConsume() {	
		System.out.println("Before method call: "+endPoint+"   name: "+config.getName());
		Employee employee = restTemplate.getForObject(endPoint, Employee.class);
		return employee;
	}
	
	@GetMapping("/getForEnityOperation")
	public Employee getForEntityConsume() {		
		ResponseEntity<Employee> responseEntity = restTemplate.getForEntity(endPoint, Employee.class);
		Employee employee = responseEntity.getBody();
		return employee;
	}
	
	@PostMapping("/postForObjectOperation")
	public Employee postForObjectConsume(@RequestBody Employee inputRequest) {
		
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("username", "password"));
		// when app needs to use basic auth to use secured rest Service, then we use basic auth		
		Employee employee = restTemplate.postForObject(endPoint,inputRequest, 
				Employee.class);
		return employee;
	}
	
	@PostMapping("/postForEntityOperation")
	public Employee postForEntityConsume() {		
		ResponseEntity<Employee> responseEntity = restTemplate.postForEntity(endPoint,createEmployee(), 
				Employee.class);
		Employee employee = responseEntity.getBody();
		return employee;
	}
	
	// End: Consuming a Rest Service using Rest Template

	
}
