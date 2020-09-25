package count.jgame.serialization;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import count.jgame.repositories.ResearchRepository;

public class ResearchKeyDeserializer extends KeyDeserializer
{
	static ResearchRepository repository;
	
	
	public ResearchKeyDeserializer() {
		super();
	}


	@SuppressWarnings("static-access")
	@Autowired
	public ResearchKeyDeserializer(ResearchRepository repository) {
		super();
		this.repository = repository;
	}


	@Override
	public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		return repository.findOneByName(key).orElse(null);
	}

}
