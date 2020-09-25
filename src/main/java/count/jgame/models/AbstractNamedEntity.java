package count.jgame.models;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

@MappedSuperclass
public class AbstractNamedEntity extends AbstractEntity
{
	@Column(length = 255)
	@Length(min = 1, max = 255)
	@NotBlank
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
