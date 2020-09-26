package count.jgame.models;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "game")
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
	Set<AdministrableLocation> administrableLocations = new LinkedHashSet<>();

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
	
	public Set<AdministrableLocation> getAdministrableLocations() {
		return administrableLocations;
	}

	public void setAdministrableLocations(Set<AdministrableLocation> administrableLocations) {
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
