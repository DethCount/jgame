package count.jgame.serialization;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import count.jgame.models.ShipType;
import count.jgame.repositories.ShipTypeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShipTypeKeyDeserializer extends KeyDeserializer
{
	private static ShipTypeRepository repository;
	
	public ShipTypeKeyDeserializer() {
	}

	@SuppressWarnings("static-access")
	@Autowired
	public ShipTypeKeyDeserializer(ShipTypeRepository repository) {
		this.repository = repository;
	}

	@Override
	public Object deserializeKey(String key, DeserializationContext ctxt) 
		throws IOException 
	{
		log.debug("{} : deserializing {}", this.getClass().getSimpleName(), key);
		System.err.println("ShipTypeKeyDeserializer : " + key);
		if (null == key) {
			return null;
		}
		
		ShipType shipType = repository.findOneByName(key).get();
		
		System.err.println(shipType.toString());
		
		return shipType;
	}
}
