package count.jgame;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import count.jgame.jms.JmsConfiguration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(
	prefix = "jgame", 
	ignoreUnknownFields = true, 
	ignoreInvalidFields = true
)
public class ApplicationConfiguration {
	private JmsConfiguration jms;

	public JmsConfiguration getJms() {
		return jms;
	}

	public void setJms(JmsConfiguration jms) {
		this.jms = jms;
	}
}