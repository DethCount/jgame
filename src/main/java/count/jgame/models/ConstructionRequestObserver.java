package count.jgame.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import count.jgame.serialization.EntityIdResolver;

@Entity
@Table(name = "construction_request_observer")
@DiscriminatorValue("construction")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = ConstructionRequestObserver.class,
	resolver = EntityIdResolver.class
)
public class ConstructionRequestObserver 
	extends ProductionRequestObserver 
{	
	@Column(name = "unit_lead_time")
	Double unitLeadTime = 0.0; // temps de production d'une unité en s
	
	@OneToOne(cascade = {
		CascadeType.ALL, 
		CascadeType.PERSIST, 
		CascadeType.DETACH, 
		CascadeType.MERGE, 
		CascadeType.REFRESH, 
		CascadeType.REMOVE
	})
	ConstructionRequest request;

	public Double getUnitLeadTime() {
		return unitLeadTime;
	}

	public void setUnitLeadTime(Double unitLeadTime) {
		this.unitLeadTime = unitLeadTime;
	}
	
	public ConstructionRequest getRequest() {
		return request;
	}

	public void setRequest(ConstructionRequest request) {
		this.request = request;
		this.game = this.request.getGame();
	}

	public ConstructionRequestObserver() {
		super();
	}
	
	public ConstructionRequestObserver(ConstructionRequest request, Double unitLeadTime) {
		super();
		
		this.request = request;
		this.game = this.request.getGame();
		this.unitLeadTime = unitLeadTime;
	}

	@Override
	public String toString() {
		return String.format(
			"ConstructionRequestObserver [id= %d, request=%s, startedAt=%tc, finishedAt=%tc, unitLeadTime=%f]", 
			this.getId(),
			this.request == null ? null : this.request.toString(),
			startedAt,
			finishedAt,
			unitLeadTime
		);
	}
}
