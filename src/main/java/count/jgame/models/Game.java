package count.jgame.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "game")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = Game.class
)
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column
	@Length(min = 1, max = 255)
	@NotBlank
	String player;
	
	@Column
	Long score = 0l;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "game_ships", 
		joinColumns = {@JoinColumn(name = "id_game", referencedColumnName = "id")}
	)
	@MapKeyColumn(name = "type")
	@Column(name = "nb")
	@OrderBy("type ASC")
	Map<ShipType,Integer> ships = new HashMap<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "game_constructions", 
		joinColumns = {@JoinColumn(name = "id_game", referencedColumnName = "id")}
	)
	@MapKeyColumn(name = "type")
	@Column(name = "level")
	@OrderBy("type ASC")
	Map<ConstructionType,Integer> constructions = new HashMap<>();
	
	@OneToMany(mappedBy = "game")
	@Where(clause = "status IN ('Running', 'Waiting')")
	@OrderBy("id ASC")
	List<ShipRequestObserver> shipProduction;
	
	@OneToMany(mappedBy = "game")
	@Where(clause = "status IN ('Running', 'Waiting')")
	@OrderBy("id ASC")
	List<ConstructionRequestObserver> constructionProduction;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Map<ShipType, Integer> getShips() {
		return ships;
	}

	public void setShips(Map<ShipType, Integer> ships) {
		this.ships = ships;
	}

	public Map<ConstructionType, Integer> getConstructions() {
		return constructions;
	}

	public void setConstructions(Map<ConstructionType, Integer> constructions) {
		this.constructions = constructions;
	}

	public List<ShipRequestObserver> getShipProduction() {
		return shipProduction;
	}

	public void setShipProduction(List<ShipRequestObserver> shipProduction) {
		this.shipProduction = shipProduction;
	}

	public List<ConstructionRequestObserver> getConstructionProduction() {
		return constructionProduction;
	}

	public void setConstructionProduction(List<ConstructionRequestObserver> constructionProduction) {
		this.constructionProduction = constructionProduction;
	}
	
	public Game()
	{
		for (ShipType t : ShipType.values()) {
			if (!this.getShips().containsKey(t)) {
				this.getShips().put(t, 0);
			}
		}

		for (ConstructionType t : ConstructionType.values()) {
			if (!this.getConstructions().containsKey(t)) {
				this.getConstructions().put(t, 0);
			}
		}
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", player=" + player + ", score=" + score + "]";
	}
}
