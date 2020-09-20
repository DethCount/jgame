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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "ship_request")
@DiscriminatorValue("ship")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = ShipRequest.class
)
public class ShipRequest extends ProductionRequest
{	
	@Enumerated(EnumType.STRING)
	ShipType type;	

	@Column
	Integer nb;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "request")
	@JsonIgnore
	List<ShipRequestObserver> observers;
	
	public ShipType getType() {
		return type;
	}
	
	public void setType(ShipType type) {
		this.type = type;
	}
	
	public Integer getNb() {
		return nb;
	}
	
	public void setNb(Integer nb) {
		this.nb = nb;
	}

	public List<ShipRequestObserver> getObservers() {
		return observers;
	}

	public void setObservers(List<ShipRequestObserver> observers) {
		this.observers = observers;
	}

	ShipRequest() {
		super();
	}
	
	ShipRequest(Game game, ShipType type, Integer nb) {
		super();
		
		this.game = game;
		this.type = type;
		this.nb = nb;
	}
	
	@Override
	public String toString() {
		return String.format(
			"ShipRequest [gameId=%d, type=%s, nb=%d]",
			this.getGame() == null ? null : this.getGame().getId(),
			type,
			nb
		);
	}
}
