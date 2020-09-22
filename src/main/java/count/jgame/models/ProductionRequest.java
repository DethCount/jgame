package count.jgame.models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

@MappedSuperclass
@DiscriminatorColumn(name = "request_type")
public abstract class ProductionRequest implements ProductionRequestInterface {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_administrable_location", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	AdministrableLocation administrableLocation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
