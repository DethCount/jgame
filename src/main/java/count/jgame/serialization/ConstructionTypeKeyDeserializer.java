package count.jgame.serialization;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import count.jgame.models.ConstructionType;
import count.jgame.repositories.ConstructionTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConstructionTypeKeyDeserializer extends KeyDeserializer
{
	private static ConstructionTypeRepository repository;
	
	public ConstructionTypeKeyDeserializer() {
	}
	
	@SuppressWarnings("static-access")
	@Autowired
	public ConstructionTypeKeyDeserializer(ConstructionTypeRepository repository)	{
		this.repository = repository;
	}

	@Override
	public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		log.debug("{} : deserializing {}", this.getClass().getSimpleName(), key);
		if (null == key) {
			return null;
		}
		
		ConstructionType constructionType = repository.findOneByName(key).get();
		
		return constructionType;
	}
}
