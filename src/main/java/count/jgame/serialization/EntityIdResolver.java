package count.jgame.serialization;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fta on 20.12.15.
 */
@Slf4j
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
    public Object resolveId(final ObjectIdGenerator.IdKey id)
    {
    	log.debug("json resolving entity {} in scope {}", id.key, id.scope);
    	
    	Matcher m = Pattern.compile("^(\\d+)@(\\w+)$").matcher(id.key.toString());
    	
    	if (m.find()) {
        	Long longId = Long.valueOf(m.group(1));
    		String className = "count.jgame.models." + m.group(2);
    		
        	log.debug("parsed id : {} {} of {}", className, longId, m.group(0));
        	
            try {
    			return this.entityManager.find(Class.forName(className), longId);
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		}
    	}
        
        return null;
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