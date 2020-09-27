package count.jgame.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import count.jgame.models.ResourceType;

public class ResourceTypeKeySerializer extends JsonSerializer<ResourceType>{

	@Override
	public void serialize(ResourceType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (value.getVisible() == false) {
			gen.writeOmittedField("hidden");
			return;
		}
		
		gen.writeFieldName(value.getName());
	}

}
