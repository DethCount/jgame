package count.jgame.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import count.jgame.models.Research;

public class ResearchKeySerializer extends JsonSerializer<Research>
{
	@Override
	public void serialize(Research value, JsonGenerator gen, SerializerProvider serializers) 
		throws IOException 
	{
		gen.writeFieldName(value.getName());
	}
}
