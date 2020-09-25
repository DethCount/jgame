package count.jgame.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import count.jgame.models.Research;

public class ResearchSerializer extends JsonSerializer<Research>
{
	@Override
	public void serialize(Research value, JsonGenerator gen, SerializerProvider serializers) 
		throws IOException 
	{
		gen.writeString(value.getName());
	}
}
