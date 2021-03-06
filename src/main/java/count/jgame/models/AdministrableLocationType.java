package count.jgame.models;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "administrable_location_type")
public class AdministrableLocationType extends AbstractNamedEntity
{	
	@Column(name = "can_construct_buildings")
	Boolean canConstructBuildings = false;
	
	@Column(name = "can_build_ships")
	Boolean canBuildShips = false;
	
	@Column(name = "can_do_research")
	Boolean canDoResearch = false;
	
	@OneToMany(
		mappedBy = "administrableLocationType",
		cascade = CascadeType.PERSIST
	)
	// ConstructionTypes that will be transformed to this type of administrable location type
	Set<ConstructionType> constructionTypes = new LinkedHashSet<>();
	
	@ManyToMany
	@JoinTable(
		name = "administrable_location_type_resources",
		joinColumns = @JoinColumn(name = "id_administrable_location_type", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "id_resource_type", referencedColumnName = "id")
	)
	// resource types that can be stored in this administrable location type
	Set<ResourceType> resources = new LinkedHashSet<>();
	
	@ManyToMany
	@JoinTable(
		name = "administrable_location_type_constructions",
		joinColumns = @JoinColumn(name = "id_administrable_location_type", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "id_construction_type", referencedColumnName = "id")
	)
	// construction types that can be built in this administrable location type
	Set<ConstructionType> constructions = new LinkedHashSet<>();
	
	@ManyToMany
	@JoinTable(
		name = "administrable_location_type_ships",
		joinColumns = @JoinColumn(name = "id_administrable_location_type", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "id_ship_type", referencedColumnName = "id")
	)
	// ship types that can be built in this administrable location type
	Set<ShipType> ships = new LinkedHashSet<>();
	
	@ManyToMany
	@JoinTable(
		name = "administrable_location_type_researches",
		joinColumns = @JoinColumn(name = "id_administrable_location_type", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "id_research", referencedColumnName = "id")
	)
	// researches that can be done in this administrable location type
	Set<Research> researches = new LinkedHashSet<>();

	public Boolean getCanConstructBuildings() {
		return canConstructBuildings;
	}

	public void setCanConstructBuildings(Boolean canConstructBuildings) {
		this.canConstructBuildings = canConstructBuildings;
	}

	public Boolean getCanBuildShips() {
		return canBuildShips;
	}

	public void setCanBuildShips(Boolean canBuildShips) {
		this.canBuildShips = canBuildShips;
	}

	public Boolean getCanDoResearch() {
		return canDoResearch;
	}

	public void setCanDoResearch(Boolean canDoResearch) {
		this.canDoResearch = canDoResearch;
	}

	public Set<ConstructionType> getConstructionTypes() {
		return constructionTypes;
	}

	public void setConstructionTypes(Set<ConstructionType> constructionTypes) {
		this.constructionTypes = constructionTypes;
	}

	public Set<ResourceType> getResources() {
		return resources;
	}

	public void setResources(Set<ResourceType> resources) {
		this.resources = resources;
	}

	public Set<ConstructionType> getConstructions() {
		return constructions;
	}

	public void setConstructions(Set<ConstructionType> constructions) {
		this.constructions = constructions;
	}

	public Set<ShipType> getShips() {
		return ships;
	}

	public void setShips(Set<ShipType> ships) {
		this.ships = ships;
	}

	public Set<Research> getResearches() {
		return researches;
	}

	public void setResearches(Set<Research> researches) {
		this.researches = researches;
	}

	public AdministrableLocationType() {
		super();
	}

	public AdministrableLocationType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "AdministrableLocationType [id=" + id + ", name=" + name + "]";
	}
}
