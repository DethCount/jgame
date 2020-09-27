package count.jgame.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "research_request_observer")
public class ResearchRequestObserver extends ProductionRequestObserver
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
	@JoinColumn(name = "id_request")
	ResearchRequest request;

	public Double getUnitLeadTime() {
		return unitLeadTime;
	}

	public void setUnitLeadTime(Double unitLeadTime) {
		this.unitLeadTime = unitLeadTime;
	}

	public ResearchRequest getRequest() {
		return request;
	}

	public void setRequest(ResearchRequest request) {
		this.request = request;
	}

	public ResearchRequestObserver() {
		super();
	}

	public ResearchRequestObserver(ResearchRequest request, Double unitLeadTime) {
		super();
		this.unitLeadTime = unitLeadTime;
		this.request = request;
	}

	@Override
	public String toString() {
		return String.format(
			"ResearchRequestObserver [id= %d, request=%s, startedAt=%tc, finishedAt=%tc, unitLeadTime=%f]", 
			this.getId(),
			this.request == null ? null : this.request.toString(),
			startedAt,
			finishedAt,
			unitLeadTime
		);
	}
	
}
