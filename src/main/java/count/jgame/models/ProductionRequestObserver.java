package count.jgame.models;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
@DiscriminatorColumn(name = "type")
abstract public class ProductionRequestObserver{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date startedAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date finishedAt;
	
	@Enumerated(EnumType.STRING)
	ProductionRequestStatus status = ProductionRequestStatus.Waiting;

	@ManyToOne
	@JoinColumn(name = "id_game", referencedColumnName = "id")
	Game game;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}
	
	public ProductionRequestStatus getStatus() {
		return status;
	}

	public void setStatus(ProductionRequestStatus status) {
		this.status = status;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void start() {
		this.startedAt = new Date();
		this.status = ProductionRequestStatus.Running;
	}
	
	public void finish() {
		this.finishedAt = new Date();
		this.status = ProductionRequestStatus.Finished;
	}
	
	public void fail() {
		this.finishedAt = new Date();
		this.status = ProductionRequestStatus.Failed;
	}
	
	public void cancel() {
		this.finishedAt = new Date();
		this.status = ProductionRequestStatus.Canceled;
	}
}
