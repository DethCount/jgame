package count.jgame.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
	use=JsonTypeInfo.Id.MINIMAL_CLASS, 
	include=JsonTypeInfo.As.PROPERTY,
	property="_type"
)
public interface ProductionRequestInterface {
	Game getGame();
	
	void setGame(Game game);
}
