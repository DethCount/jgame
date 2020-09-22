package count.jgame.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import count.jgame.serialization.EntityIdResolver;

@Entity
@Table(name = "administrable_location_type")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = AdministrableLocationType.class,
	resolver = EntityIdResolver.class
)
public class AdministrableLocationType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(length = 255)
	@Length(min = 1, max = 255)
	@NotBlank
	String name;
	
	@ManyToMany
	@JoinTable(
		name = "location_resources",
		joinColumns = @JoinColumn(name = "id_administrable_location_type", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "id_resource_type", referencedColumnName = "id")
	)
	List<ResourceType> resources = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
		name = "location_constructions",
		joinColumns = @JoinColumn(name = "id_administrable_location_type", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "id_construction_type", referencedColumnName = "id")
	)
	List<ConstructionType> constructions = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ResourceType> getResources() {
		return resources;
	}

	public void setResources(List<ResourceType> resources) {
		this.resources = resources;
	}

	public List<ConstructionType> getConstructions() {
		return constructions;
	}

	public void setConstructions(List<ConstructionType> constructions) {
		this.constructions = constructions;
	}

	public AdministrableLocationType() {
		super();
	}

	public AdministrableLocationType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "AdministrableLocationType [id=" + id + ", name=" + name + "]";
	}
}
