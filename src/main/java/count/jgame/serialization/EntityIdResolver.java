package count.jgame.serialization;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fta on 20.12.15.
 */
public class EntityIdResolver implements ObjectIdResolver
{
    static private EntityManager entityManager;
    
    public EntityIdResolver() {
		super();
	}

	@SuppressWarnings("static-access")
	@Autowired
    public EntityIdResolver(final EntityManager mainEntityManager)
	{
        this.entityManager = mainEntityManager;
    }

    @Override
    public void bindItem(final ObjectIdGenerator.IdKey id, final Object pojo) {}

    @SuppressWarnings("static-access")
	@Override
    public Object resolveId(final ObjectIdGenerator.IdKey id) {

        return this.entityManager.find(id.scope, id.key);
    }

    @Override
    public ObjectIdResolver newForDeserialization(final Object context) {
        return this;
    }

    @Override
    public boolean canUseFor(final ObjectIdResolver resolverType) {
        return false;
    }

}