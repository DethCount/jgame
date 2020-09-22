package count.jgame.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import count.jgame.models.ConstructionType;

public class ConstructionTypeSerializer extends JsonSerializer<ConstructionType>
{

	@Override
	public void serialize(ConstructionType value, JsonGenerator gen, SerializerProvider serializers)
		throws IOException 
	{
		gen.writeString(value.getName());	
	}

}
