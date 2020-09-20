package count.jgame.jms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(
	prefix = "shipyard", 
	ignoreUnknownFields = true, 
	ignoreInvalidFields = true
)
public class ShipyardConfiguration {
	Integer minDelay;
	Integer retryDelay;
	Integer failedDelay;
	
	public Integer getMinDelay() {
		return minDelay;
	}
	public void setMinDelay(Integer minDelay) {
		this.minDelay = minDelay;
	}
	public Integer getRetryDelay() {
		return retryDelay;
	}
	public void setRetryDelay(Integer retryDelay) {
		this.retryDelay = retryDelay;
	}
	public Integer getFailedDelay() {
		return failedDelay;
	}
	public void setFailedDelay(Integer failedDelay) {
		this.failedDelay = failedDelay;
	}
}
