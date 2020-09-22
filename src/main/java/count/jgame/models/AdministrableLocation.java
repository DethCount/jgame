package count.jgame.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import count.jgame.serialization.EntityIdResolver;

@Entity
@Table(name = "administrable_location")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = AdministrableLocation.class,
	resolver = EntityIdResolver.class
)
public class AdministrableLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(length = 255)
	@Length(max = 255)
	String name;
	
	@ManyToOne
	@JoinColumn(name = "id_administration_location_type", referencedColumnName = "id")
	AdministrableLocationType type;

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

	public AdministrableLocationType getType() {
		return type;
	}

	public void setType(AdministrableLocationType type) {
		this.type = type;
	}
}
