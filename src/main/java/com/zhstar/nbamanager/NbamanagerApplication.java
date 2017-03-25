package  com.zhstar.nbamanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class NbamanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NbamanagerApplication.class, args);
	}
}
