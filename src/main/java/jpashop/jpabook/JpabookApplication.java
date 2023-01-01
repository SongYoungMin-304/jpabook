package jpashop.jpabook;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpabookApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpabookApplication.class, args);
	}

	/*
	 * 무한 로딩 방지로 지연 로딩 조회 api 시 지연 로딩 호출 안함
	 */
	@Bean
	Hibernate5Module hibernate5Module(){
		return new Hibernate5Module();
	}


}
