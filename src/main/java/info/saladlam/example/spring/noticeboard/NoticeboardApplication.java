package info.saladlam.example.spring.noticeboard;

import com.github.dozermapper.springboot.autoconfigure.DozerAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
// don't support loading AutoConfiguration by /META-INF/spring.factories file
@Import(DozerAutoConfiguration.class)
public class NoticeboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoticeboardApplication.class, args);
	}

}
