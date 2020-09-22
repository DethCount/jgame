package count.jgame.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import count.jgame.serialization.EntityIdResolver;

@Entity
@Table(name = "construction_type")

@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = ConstructionType.class,
	resolver = EntityIdResolver.class
)
public class ConstructionType
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(length = 255)
	@Length(min = 1, max = 255)
	@NotBlank
	String name;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "constructionType")
	@OrderBy("level ASC")
	@JsonIgnore
	List<ConstructionTypeLevel> levels = new ArrayList<>();

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

	public List<ConstructionTypeLevel> getLevels() {
		return levels;
	}

	public void setLevels(List<ConstructionTypeLevel> levels) {
		this.levels = levels;
	}

	public ConstructionType() {
		super();
	}

	public ConstructionType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "ConstructionType [id=" + id + ", name=" + name + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        
		if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
		return this.getId() == ((ConstructionType) obj).getId();
	}
	
	@Override
	public int hashCode() {
		return this.getId() == null ? 0 : this.getId().hashCode();
	}
}
