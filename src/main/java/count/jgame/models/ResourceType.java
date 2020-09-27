package count.jgame.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "resource_type")
public class ResourceType extends AbstractNamedEntity
{
	@Column
	@JsonIgnore
	Boolean visible;
	
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public ResourceType() {
		super();
	}

	public ResourceType(Long id, String name, Boolean visible) {
		super();
		this.id = id;
		this.name = name;
		this.visible = visible;
	}
	
	public ResourceType(Long id, String name) {
		this(id, name, true);
	}
}
