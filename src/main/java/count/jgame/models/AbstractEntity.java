package count.jgame.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import count.jgame.serialization.EntityIdResolver;

@MappedSuperclass
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "@id",
	resolver = EntityIdResolver.class
)
public abstract class AbstractEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@JsonGetter("@id")
	public String getJsonId() {
		return this.getId().toString() + "@" + this.getClass().getSimpleName();
	}

	@JsonSetter("@id")
	public void setJsonId(String id) {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
