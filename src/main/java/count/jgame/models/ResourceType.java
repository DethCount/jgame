package count.jgame.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "resource_type")
public class ResourceType extends AbstractNamedEntity
{	
	public ResourceType() {
		super();
	}

	public ResourceType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
