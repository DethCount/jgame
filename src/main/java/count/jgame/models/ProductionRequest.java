package count.jgame.models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

@MappedSuperclass
@DiscriminatorColumn(name = "request_type")
public abstract class ProductionRequest implements ProductionRequestInterface {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_game", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	Game game;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
