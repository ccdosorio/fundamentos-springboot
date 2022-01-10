package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;

	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;

	private UserPojo userPojo;

	private UserRepository userRepository;

	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository) {
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// ejemplosAnteriores();
		saveUserInDatabase();
		getInformationJpqlFromUser();
	}

	private void getInformationJpqlFromUser() {
		LOGGER.info("usuario con el método: "
				+ userRepository.findByUserEmail("ccdosorio@gmail.com")
				.orElseThrow( () -> new RuntimeException("No se encontró el usuario")));

		userRepository.findAndSort("Chris", Sort.by("id").ascending())
				.stream()
				.forEach(user -> LOGGER.info("User with method con sort: " + user));

		userRepository.findByName("Hilary")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query methods: " + user));

		LOGGER.info("Usuario con query methods 2: " + userRepository.findByEmailAndName("david@gmail.com", "David")
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

		userRepository.findByNameLike("%Ch%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario like: " + user));

		userRepository.findByNameOrEmail(null, "ccdosorio@gmail.com")
				.stream()
				.forEach(user -> LOGGER.info("Usuario OR: " + user));

		userRepository.findByBirthDayBetween(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 6, 2))
				.stream()
				.forEach(user -> LOGGER.info("Usuario rango fechas: " + user));

		userRepository.findByNameLikeOrderByIdDesc("%C%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario LIKE DESC: " + user));

		userRepository.findDistinctByNameLike("Christian")
				.stream()
				.forEach(user -> LOGGER.info("Usuario DISTINC: " + user));

		LOGGER.info("EL usuario a partir del named parameter es: " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(2021, 10, 22), "hilary@gmail.com")
				.orElseThrow(() ->
						new RuntimeException("No encontro el usuario a partir del named parameter: " )));


	}

	private void saveUserInDatabase() {
		User user1 = new User("Christian", "ccdosorio@gmail.com", LocalDate.of(2021, 01, 20));
		User user2 = new User("David", "david@gmail.com", LocalDate.of(2021, 05, 21));
		User user3 = new User("Hilary", "hilary@gmail.com", LocalDate.of(2021, 10, 22));
		User user4 = new User("Christopher", "chris@gmail.com", LocalDate.of(2021, 07, 17));
		User user5 = new User("Carlos", "carlos@gmail.com", LocalDate.of(2021, 11, 20));

		List<User> list = Arrays.asList(user1, user2, user3, user4, user5);

		list.stream().forEach(userRepository::save);


	}

	private void ejemplosAnteriores() {
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword() + "-" + userPojo.getAge());

		try {
			// error
			int value = 10 / 0;
			LOGGER.debug("Mi valor : " + value);
		} catch (Exception e) {
			LOGGER.error("Esto es un error al dividir por 0" + e.getMessage());
		}
	}
}
