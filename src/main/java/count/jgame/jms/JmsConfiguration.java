package count.jgame.jms;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfiguration {
	ConstructionsConfiguration constructions;
	ShipyardConfiguration shipyard;
	ResearchesConfiguration researches;
	
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

	public ResearchesConfiguration getResearches() {
		return researches;
	}

	public void setResearches(ResearchesConfiguration researches) {
		this.researches = researches;
	}
}