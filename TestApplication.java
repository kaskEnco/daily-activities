package Solr.SolrSp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class TestApplication extends SpringBootServletInitializer{
	
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
 
    private static Class<TestApplication> applicationClass = TestApplication.class;
	
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}
}
