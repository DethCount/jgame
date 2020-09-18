package count.jgame.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "construction_request")
@DiscriminatorValue("construction")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = ConstructionRequest.class
)
public class ConstructionRequest extends ProductionRequest
{
	@Enumerated(EnumType.STRING)
	ConstructionType type;
	
	@Column
	Integer level;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "request")
	@Where(clause = "status = 'Running'")
	@JsonIgnore
	List<ConstructionRequestObserver> observers;
	
	public ConstructionType getType() {
		return type;
	}
	
	public void setType(ConstructionType type) {
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
	
	public ConstructionRequest() {
		super();
	}

	public ConstructionRequest(Game game, ConstructionType type, Integer level) {
		super();
		
		this.game = game;
		this.type = type;
		this.level = level;
	}

	@Override
	public String toString() {
		return "ConstructionRequest [type=" + type + ", level=" + level + "]";
	}
}
