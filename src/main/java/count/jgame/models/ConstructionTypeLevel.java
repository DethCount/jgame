package count.jgame.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import count.jgame.serialization.ResourceTypeKeyDeserializer;
import count.jgame.serialization.ResourceTypeKeySerializer;

@Entity
@Table(name = "construction_type_level")
public class ConstructionTypeLevel extends AbstractEntity
{	
	@Column
	Integer level;
	
	@ManyToOne
	@JoinColumn(name = "id_construction_type", referencedColumnName = "id")
	ConstructionType constructionType;
	
	@ElementCollection
	@CollectionTable(
		name = "construction_type_level_productions", 
		joinColumns = {
			@JoinColumn(name = "id_construction_type_level", referencedColumnName = "id")
		}
	)
	@MapKeyJoinColumn(name = "id_resource_type", referencedColumnName = "id")
	@Column(name = "value")
	@JsonDeserialize(keyUsing = ResourceTypeKeyDeserializer.class)
	@JsonSerialize(keyUsing = ResourceTypeKeySerializer.class)
	Map<ResourceType, Double> productions = new HashMap<>();
	
	@ElementCollection
	@CollectionTable(
		name = "construction_type_level_storage", 
		joinColumns = {
			@JoinColumn(name = "id_construction_type_level", referencedColumnName = "id")
		}
	)
	@MapKeyJoinColumn(name = "id_resource_type", referencedColumnName = "id")
	@Column(name = "value")
	@JsonDeserialize(keyUsing = ResourceTypeKeyDeserializer.class)
	@JsonSerialize(keyUsing = ResourceTypeKeySerializer.class)
	Map<ResourceType, Integer> storage = new HashMap<>();

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public ConstructionType getConstructionType() {
		return constructionType;
	}

	public void setConstructionType(ConstructionType constructionType) {
		this.constructionType = constructionType;
	}

	public Map<ResourceType, Double> getProductions() {
		return productions;
	}

	public void setProductions(Map<ResourceType, Double> productions) {
		this.productions = productions;
	}

	public Map<ResourceType, Integer> getStorage() {
		return storage;
	}

	public void setStorage(Map<ResourceType, Integer> storage) {
		this.storage = storage;
	}

	public ConstructionTypeLevel() {
		super();
	}

	public ConstructionTypeLevel(Long id, Integer level, ConstructionType constructionType) {
		super();
		this.id = id;
		this.level = level;
		this.constructionType = constructionType;
	}
}
