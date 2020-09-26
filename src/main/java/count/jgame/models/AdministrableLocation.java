package count.jgame.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import count.jgame.serialization.ConstructionTypeKeyDeserializer;
import count.jgame.serialization.ConstructionTypeKeySerializer;
import count.jgame.serialization.ResearchKeyDeserializer;
import count.jgame.serialization.ResearchKeySerializer;
import count.jgame.serialization.ShipTypeKeyDeserializer;
import count.jgame.serialization.ShipTypeKeySerializer;

@Entity
@Table(
	name = "administrable_location",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"id_game", "slug"})
	}
)
public class AdministrableLocation extends AbstractEntity
{	
	@Column(length = 32)
	@Length(max = 32)
	String name;
	
	@Column(length = 32)
	@Length(min = 1, max = 32)
	String slug;
	
	@Column(length = 511)
	@Length(min = 1, max = 511)
	String path;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "id_parent")
	@JsonIdentityReference
	AdministrableLocation parent;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_resource_update")
	Date lastResourceUpdate;
	
	@ManyToOne
	@JoinColumn(name = "id_administration_location_type", referencedColumnName = "id")
	AdministrableLocationType type;
	
	@ManyToOne
	@JoinColumn(name = "id_game", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	Game game;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "administrable_location_ships", 
		joinColumns = {
			@JoinColumn(name = "id_administrable_location", referencedColumnName = "id")
		}
	)
	@MapKeyJoinColumn(name = "id_ship_type", referencedColumnName = "id")
	@Column(name = "nb")
	@OrderBy("id_ship_type ASC")
	@JsonDeserialize(keyUsing = ShipTypeKeyDeserializer.class)
	@JsonSerialize(keyUsing = ShipTypeKeySerializer.class)
	Map<ShipType,Integer> ships = new HashMap<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "administrable_location_constructions", 
		joinColumns = {
			@JoinColumn(name = "id_administrable_location", referencedColumnName = "id")
		}
	)
	@MapKeyJoinColumn(name = "id_construction_type", referencedColumnName = "id")
	@Column(name = "level")
	@OrderBy("id_construction_type ASC")
	@JsonDeserialize(keyUsing = ConstructionTypeKeyDeserializer.class)
	@JsonSerialize(keyUsing = ConstructionTypeKeySerializer.class)
	Map<ConstructionType,Integer> constructions = new HashMap<>();
	
	@ElementCollection
	@CollectionTable(
		name = "administrable_location_researches", 
		joinColumns = {
			@JoinColumn(name = "id_administrable_location", referencedColumnName = "id")
		}
	)
	@MapKeyJoinColumn(name = "id_research", referencedColumnName = "id")
	@Column(name = "level")
	@OrderBy("id_research ASC")
	@JsonDeserialize(keyUsing = ResearchKeyDeserializer.class)
	@JsonSerialize(keyUsing = ResearchKeySerializer.class)
	Map<Research,Integer> researches = new HashMap<>();
	
	@OneToMany(mappedBy = "administrableLocation")
	@Where(clause = "status IN ('Running', 'Waiting')")
	@OrderBy("id ASC")
	List<ShipRequestObserver> shipProduction = new ArrayList<>();
	
	@OneToMany(mappedBy = "administrableLocation")
	@Where(clause = "status IN ('Running', 'Waiting')")
	@OrderBy("id ASC")
	List<ConstructionRequestObserver> constructionProduction = new ArrayList<>();
	
	@OneToMany(mappedBy = "administrableLocation")
	@Where(clause = "status IN ('Running', 'Waiting')")
	@OrderBy("id ASC")
	List<ResearchRequestObserver> researchProduction = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public AdministrableLocation getParent() {
		return parent;
	}

	public void setParent(AdministrableLocation parent) {
		this.parent = parent;
	}

	public AdministrableLocationType getType() {
		return type;
	}

	public void setType(AdministrableLocationType type) {
		this.type = type;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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

	public Map<Research, Integer> getResearches() {
		return researches;
	}

	public void setResearches(Map<Research, Integer> researches) {
		this.researches = researches;
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

	public List<ResearchRequestObserver> getResearchProduction() {
		return researchProduction;
	}

	public void setResearchProduction(List<ResearchRequestObserver> researchProduction) {
		this.researchProduction = researchProduction;
	}

	public AdministrableLocation()
	{
		super();
	}

	public AdministrableLocation(String name)
	{
		super();
		this.name = name;
	}

	public AdministrableLocation(Long id, String name, AdministrableLocationType type, Game game) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.game = game;
	}

	@Override
	public String toString() {
		return "AdministrableLocation [id=" + id + ", name=" + name + ", slug=" + slug + ", lastResourceUpdate="
				+ lastResourceUpdate + ", type=" + type + "]";
	}
}
