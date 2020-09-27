package count.jgame.jms;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ResearchesConfiguration {
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
