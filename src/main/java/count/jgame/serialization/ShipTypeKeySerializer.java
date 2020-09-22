package count.jgame.serialization;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import count.jgame.models.ShipType;

@Component
public class ShipTypeKeySerializer extends JsonSerializer<ShipType>{

	@Override
	public void serialize(ShipType value, JsonGenerator gen, SerializerProvider serializers) 
		throws IOException 
	{
		gen.writeFieldName(value.getName());
	}
}
