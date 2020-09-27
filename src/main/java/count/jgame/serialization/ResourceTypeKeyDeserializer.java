package count.jgame.serialization;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import count.jgame.repositories.ResourceTypeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResourceTypeKeyDeserializer extends KeyDeserializer
{
	@SuppressWarnings("unused")
	@Autowired
	private ResourceTypeRepository repository;
	
	public ResourceTypeKeyDeserializer() {
	}

	@Override
	public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		log.debug("{} : deserializing {}", this.getClass().getSimpleName(), key);
		if (null == key) {
			return null;
		}
		
		return repository.findOneByNameAndVisible(key, true).orElse(null);
	}
}
