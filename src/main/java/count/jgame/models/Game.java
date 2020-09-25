package count.jgame.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import count.jgame.serialization.EntityIdResolver;

@Entity
@Table(name = "game")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "@id",
	scope = Game.class,
	resolver = EntityIdResolver.class
)
public class Game extends AbstractEntity
{	
	@Column
	@Length(min = 1, max = 255)
	@NotBlank
	String player;
	
	@Column
	Long score = 0l;
	
	@OneToMany(mappedBy = "game")
	@OrderBy("id ASC")
	List<AdministrableLocation> administrableLocations = new ArrayList<>();

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
	
	public List<AdministrableLocation> getAdministrableLocations() {
		return administrableLocations;
	}

	public void setAdministrableLocations(List<AdministrableLocation> administrableLocations) {
		this.administrableLocations = administrableLocations;
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
