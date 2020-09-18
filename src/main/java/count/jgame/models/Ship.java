package count.jgame.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ship")
public class Ship {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Enumerated(EnumType.STRING)
	ShipType type;
	
	@ManyToOne
	@JoinColumn(name = "id_game", referencedColumnName = "id")
	Game game;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "ship_cargo", 
		joinColumns = {@JoinColumn(name = "id_ship", referencedColumnName = "id")}
	)
	@MapKeyColumn(name = "item")
	@Column(name = "nb")
	Map<String,Double> cargo = new HashMap<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ShipType getType() {
		return type;
	}

	public void setType(ShipType type) {
		this.type = type;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Map<String, Double> getCargo() {
		return cargo;
	}

	public void setCargo(Map<String, Double> cargo) {
		this.cargo = cargo;
	}

	public Ship(Game game, ShipType type) {
		super();
		
		this.game = game;
		this.type = type;
	}
}
