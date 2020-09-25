package count.jgame.models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

@MappedSuperclass
@DiscriminatorColumn(name = "request_type")
public abstract class ProductionRequest extends AbstractEntity
	implements ProductionRequestInterface
{	
	@ManyToOne
	@JoinColumn(name = "id_administrable_location", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	AdministrableLocation administrableLocation;

	public AdministrableLocation getAdministrableLocation() {
		return administrableLocation;
	}

	public void setAdministrableLocation(AdministrableLocation administrableLocation) {
		this.administrableLocation = administrableLocation;
	}

	public ProductionRequest() {
		super();
	}

	public ProductionRequest(AdministrableLocation administrableLocation) {
		super();
		this.administrableLocation = administrableLocation;
	}
}
