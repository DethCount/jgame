package count.jgame.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "ship_request_observer")
@DiscriminatorValue("ship")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = ShipRequestObserver.class
)
public class ShipRequestObserver 
	extends ProductionRequestObserver 
{	
	@Column(name = "nb_done")
	Integer nbDone = 0;
	
	@Column(name = "unit_lead_time")
	Double unitLeadTime = 0.0; // temps de production d'une unité en s
	
	@OneToOne(cascade = CascadeType.ALL)
	ShipRequest request;

	public Integer getNbDone() {
		return nbDone;
	}

	public void setNbDone(Integer nbDone) {
		this.nbDone = nbDone;
	}

	public Double getUnitLeadTime() {
		return unitLeadTime;
	}

	public void setUnitLeadTime(Double unitLeadTime) {
		this.unitLeadTime = unitLeadTime;
	}
	
	public ShipRequest getRequest() {
		return request;
	}

	public void setRequest(ShipRequest request) {
		this.request = request;
		this.game = this.request.getGame();
	}

	public ShipRequestObserver() {
		super();
	}
	
	public ShipRequestObserver(ShipRequest request, Double unitLeadTime) {
		super();
		
		this.request = request;
		this.game = this.request.getGame();
		this.unitLeadTime = unitLeadTime;
	}

	@Override
	public String toString() {
		return String.format(
			"ShipRequestObserver [id= %d, request=%s, startedAt=%tc, finishedAt=%tc, nbDone=%d, unitLeadTime=%f]", 
			this.getId(),
			this.request == null ? null : this.request.toString(),
			startedAt,
			finishedAt,
			nbDone,
			unitLeadTime
		);
	}
}
