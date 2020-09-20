package count.jgame.jms;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfiguration {
	ConstructionsConfiguration constructions;
	ShipyardConfiguration shipyard;
	
	public ConstructionsConfiguration getConstructions() {
		return constructions;
	}
	
	public void setConstructions(ConstructionsConfiguration constructions) {
		this.constructions = constructions;
	}
	
	public ShipyardConfiguration getShipyard() {
		return shipyard;
	}
	
	public void setShipyard(ShipyardConfiguration shipyard) {
		this.shipyard = shipyard;
	}
}