package count.jgame.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import count.jgame.serialization.EntityIdResolver;
import count.jgame.serialization.ResearchKeyDeserializer;
import count.jgame.serialization.ResearchKeySerializer;

@Entity
@Table(name = "ship_type")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "@id",
	scope = ShipType.class,
	resolver = EntityIdResolver.class
)
public class ShipType extends AbstractEntity
{
	@Column(length = 255)
	@Length(min = 1, max = 255)
	@NotBlank
	String name;
	
	@ElementCollection
	@CollectionTable(
		name = "ship_type_deps_research_level",
		joinColumns = {
			@JoinColumn(name="id_ship_type", referencedColumnName = "id")
		}
	)
	@MapKeyJoinColumn(name = "id_research", referencedColumnName = "id")
	@Column(name = "level")
	@JsonDeserialize(keyUsing = ResearchKeyDeserializer.class)
	@JsonSerialize(keyUsing = ResearchKeySerializer.class)
	Map<Research,Integer> dependsOnResearchLevel = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Research, Integer> getDependsOnResearchLevel() {
		return dependsOnResearchLevel;
	}

	public void setDependsOnResearchLevel(Map<Research, Integer> dependsOnResearchLevel) {
		this.dependsOnResearchLevel = dependsOnResearchLevel;
	}

	public ShipType() {
		super();
	}

	public ShipType(Long id, @Length(min = 1, max = 255) @NotBlank String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "ShipType [" + hashCode() + ", id=" + id + ", name=" + name + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        
		if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
		return this.getId() == ((ShipType) obj).getId();
	}
	
	@Override
	public int hashCode() {
		return this.getId() == null ? 0 : this.getId().hashCode();
	}
}
