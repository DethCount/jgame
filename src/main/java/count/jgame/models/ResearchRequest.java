package count.jgame.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import count.jgame.serialization.ResearchDeserializer;
import count.jgame.serialization.ResearchSerializer;

@Entity
@Table(name = "research_request")
public class ResearchRequest extends ProductionRequest
{	
	@ManyToOne
	@JoinColumn(name = "id_type", referencedColumnName = "id")
	@JsonDeserialize(using = ResearchDeserializer.class)
	@JsonSerialize(using = ResearchSerializer.class)
	Research type;
	
	@Column
	Integer level;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "request")
	@JsonIgnore
	List<ConstructionRequestObserver> observers;

	public Research getType() {
		return type;
	}

	public void setType(Research type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<ConstructionRequestObserver> getObservers() {
		return observers;
	}

	public void setObservers(List<ConstructionRequestObserver> observers) {
		this.observers = observers;
	}

	public ResearchRequest() {
		super();
	}

	public ResearchRequest(AdministrableLocation administrableLocation, Research type, Integer level) {
		super(administrableLocation);
		this.type = type;
		this.level = level;
	}

	@Override
	public String toString() {
		return String.format(
			"ResearchRequest [administrableLocationId=%d, type=%s, level=%d]",
			this.getAdministrableLocation() == null ? null : this.getAdministrableLocation().getId(),
			type,
			level
		);
	}	
}
