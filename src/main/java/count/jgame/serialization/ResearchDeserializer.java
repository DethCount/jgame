package count.jgame.serialization;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import count.jgame.models.Research;
import count.jgame.repositories.ResearchRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResearchDeserializer extends JsonDeserializer<Research>
{
	static ResearchRepository repository;
	
	public ResearchDeserializer() {
		super();
	}

	@SuppressWarnings("static-access")
	@Autowired
	public ResearchDeserializer(ResearchRepository repository)
	{
		super();
		this.repository = repository;
	}

	@Override
	public Research deserialize(JsonParser p, DeserializationContext ctxt) 
		throws IOException, JsonProcessingException 
	{
		String name = p.getValueAsString();
		log.debug("deserializing ConstructionType from {}", name);
		
		if (null == name) {
			return null;
		}
		
		return repository.findOneByName(p.getValueAsString()).orElse(null);
	}
}
