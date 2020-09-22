package count.jgame.serialization;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import count.jgame.models.ShipType;
import count.jgame.repositories.ShipTypeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShipTypeDeserializer extends JsonDeserializer<ShipType>
{
	private static ShipTypeRepository repository;
	
	
	public ShipTypeDeserializer() 
	{
		super();
	}

	@SuppressWarnings("static-access")
	@Autowired
	public ShipTypeDeserializer(ShipTypeRepository repository) 
	{
		this.repository = repository;
	}

	@Override
	public ShipType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String name = p.getValueAsString();
		ShipType shipType = repository.findOneByName(name).get();
		
		log.debug("{} deserialized {} to {}", this.getClass().getSimpleName(), name, shipType == null ? null : shipType.toString());
		
		return shipType;
	}

}
