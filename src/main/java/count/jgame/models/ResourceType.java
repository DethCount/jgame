package count.jgame.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import count.jgame.serialization.EntityIdResolver;

@Entity
@Table(name = "resource_type")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "@id",
	scope = ResourceType.class,
	resolver = EntityIdResolver.class
)
public class ResourceType extends AbstractEntity
{
	@Column(length = 255)
	@Length(min = 1, max = 255)
	@NotBlank
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ResourceType() {
		super();
	}

	public ResourceType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
