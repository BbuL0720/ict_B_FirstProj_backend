package kr.co.ictb.ictb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@PropertySource(value = {"classpath:/properties/config.properties"}, encoding = "UTF-8")
public class IctbApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(IctbApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(IctbApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer crosConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {

				System.out.println("Cros Allow Origin 실행!");
				registry.addMapping("/**")
						.allowedOrigins("http://192.168.0.142:3001","http://localhost:3001")
						.allowedHeaders("*")
						.allowCredentials(true)
						.allowedMethods("*").maxAge(3600);
			}
		};
	}
}
