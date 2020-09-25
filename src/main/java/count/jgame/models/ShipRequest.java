package count.jgame.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import count.jgame.serialization.ShipTypeDeserializer;
import count.jgame.serialization.ShipTypeSerializer;

@Entity
@Table(name = "ship_request")
@DiscriminatorValue("ship")
public class ShipRequest extends ProductionRequest
{	
	@ManyToOne
	@JoinColumn(name = "id_ship_type", referencedColumnName = "id")
	@JsonDeserialize(using = ShipTypeDeserializer.class)
	@JsonSerialize(using = ShipTypeSerializer.class)
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
	
	ShipRequest(AdministrableLocation administrableLocation, ShipType type, Integer nb) {
		super(administrableLocation);
		
		this.type = type;
		this.nb = nb;
	}
	
	@Override
	public String toString() {
		return String.format(
			"ShipRequest [administrableLocationId=%d, type=%s, nb=%d]",
			this.getAdministrableLocation() == null ? null : this.getAdministrableLocation().getId(),
			type,
			nb
		);
	}
}
