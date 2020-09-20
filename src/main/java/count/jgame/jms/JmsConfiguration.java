package count.jgame.jms;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfiguration {
	ConstructionsConfiguration constructions;
	ShipyardConfiguration ships;
	
	public ConstructionsConfiguration getConstructions() {
		return constructions;
	}
	
	public void setConstructions(ConstructionsConfiguration constructions) {
		this.constructions = constructions;
	}
	
	public ShipyardConfiguration getShips() {
		return ships;
	}
	
	public void setShips(ShipyardConfiguration ships) {
		this.ships = ships;
	}
}