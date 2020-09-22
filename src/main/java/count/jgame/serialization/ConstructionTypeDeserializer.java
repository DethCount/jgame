package count.jgame.serialization;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import count.jgame.models.ConstructionType;
import count.jgame.repositories.ConstructionTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConstructionTypeDeserializer extends JsonDeserializer<ConstructionType>
{
	private static ConstructionTypeRepository repository;
	
	public ConstructionTypeDeserializer() {
		// SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@SuppressWarnings("static-access")
	@Autowired
	public ConstructionTypeDeserializer(ConstructionTypeRepository repository)	{
		this.repository = repository;
	}

	@Override
	public ConstructionType deserialize(JsonParser p, DeserializationContext ctxt) 
		throws IOException, JsonProcessingException 
	{
		String name = p.getValueAsString();
		log.debug("deserializing ConstructionType from {}", name);
		if (null == name) {
			return null;
		}
		
		return repository.findOneByName(name).get();
	}
}
