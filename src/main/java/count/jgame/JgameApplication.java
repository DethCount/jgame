package count.jgame;

import javax.jms.Session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,MongoAutoConfiguration.class})
public class JgameApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(JgameApplication.class, args);
	}

	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		System.out.println("content negociation config");
	    configurer
	    	.favorParameter(true)
	    	.parameterName("format")
	    	.ignoreAcceptHeader(true)
	    	.defaultContentType(MediaType.APPLICATION_JSON) 
	    	.mediaType("html", MediaType.TEXT_HTML) 
	    	.mediaType("json", MediaType.APPLICATION_JSON); 
	}
	
	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
	    
		return converter;
	}
}
