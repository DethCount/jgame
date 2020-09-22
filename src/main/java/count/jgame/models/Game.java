package count.jgame.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import count.jgame.serialization.ConstructionTypeKeyDeserializer;
import count.jgame.serialization.ConstructionTypeKeySerializer;
import count.jgame.serialization.EntityIdResolver;
import count.jgame.serialization.ShipTypeKeyDeserializer;
import count.jgame.serialization.ShipTypeKeySerializer;

@Entity
@Table(name = "game")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = Game.class,
	resolver = EntityIdResolver.class
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
	
	@Temporal(TemporalType.TIMESTAMP)
	Date lastResourceUpdate;
	
	@ElementCollection
	@CollectionTable(
		name = "game_ships", 
		joinColumns = {@JoinColumn(name = "id_game", referencedColumnName = "id")}
	)
	@MapKeyJoinColumn(name = "id_ship_type", referencedColumnName = "id")
	@Column(name = "nb")
	@OrderBy("id_ship_type ASC")
	@JsonDeserialize(keyUsing = ShipTypeKeyDeserializer.class)
	@JsonSerialize(keyUsing = ShipTypeKeySerializer.class)
	Map<ShipType,Integer> ships = new HashMap<>();

	@ElementCollection
	@CollectionTable(
		name = "game_constructions", 
		joinColumns = {@JoinColumn(name = "id_game", referencedColumnName = "id")}
	)
	@MapKeyColumn(name = "type")
	@MapKeyJoinColumn(name = "id_construction_type", referencedColumnName = "id")
	@OrderBy("id_construction_type ASC")
	@JsonDeserialize(keyUsing = ConstructionTypeKeyDeserializer.class)
	@JsonSerialize(keyUsing = ConstructionTypeKeySerializer.class)
	Map<ConstructionType,Integer> constructions = new HashMap<>();
	
	@OneToMany(mappedBy = "game")
	@Where(clause = "status IN ('Running', 'Waiting')")
	@OrderBy("id ASC")
	List<ShipRequestObserver> shipProduction = new ArrayList<>();
	
	@OneToMany(mappedBy = "game")
	@Where(clause = "status IN ('Running', 'Waiting')")
	@OrderBy("id ASC")
	List<ConstructionRequestObserver> constructionProduction = new ArrayList<>();

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

	public Date getLastResourceUpdate() {
		return lastResourceUpdate;
	}

	public void setLastResourceUpdate(Date lastResourceUpdate) {
		this.lastResourceUpdate = lastResourceUpdate;
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
		super();
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", player=" + player + ", score=" + score + "]";
	}
}
