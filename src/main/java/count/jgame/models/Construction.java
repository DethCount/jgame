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
@Table(name = "building")
public class Construction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Enumerated(EnumType.STRING)
	ConstructionType type;
	
	@Column
	Integer level = 0;
	
	@ManyToOne
	@JoinColumn(name = "id_game", referencedColumnName = "id")
	Game game;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "construction_storage", 
		joinColumns = {@JoinColumn(name = "id_construction", referencedColumnName = "id")}
	)
	@MapKeyColumn(name = "item")
	@Column(name = "nb")
	Map<String,Double> storage = new HashMap<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public ConstructionType getType() {
		return type;
	}
	
	public void setType(ConstructionType type) {
		this.type = type;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Map<String, Double> getStorage() {
		return storage;
	}

	public void setStorage(Map<String, Double> storage) {
		this.storage = storage;
	}

	public Construction(ConstructionType type, Integer level) {
		super();
		this.type = type;
		this.level = level;
	}
}
