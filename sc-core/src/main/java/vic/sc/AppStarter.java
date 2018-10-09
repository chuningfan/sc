package vic.sc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import vic.sc.annotation.EnableAOP;
import vic.sc.annotation.EnableWebComponent;

@SpringBootApplication
@EnableWebComponent
@EnableAOP
public class AppStarter {
	
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder();
		builder.sources(AppStarter.class).run(args);
	}
	
}
