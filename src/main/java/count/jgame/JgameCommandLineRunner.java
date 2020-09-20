package count.jgame;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JgameCommandLineRunner implements CommandLineRunner {
	@Autowired ConnectionFactory connectionFactory;
	
	@Autowired DefaultJmsListenerContainerFactoryConfigurer configurer;
	
    @Override
    public void run(String...args) throws Exception {
    	clearOldScheduledMessages();
    }

	@Bean
    public void clearOldScheduledMessages() throws JMSException
	{
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        configurer.configure(factory, connectionFactory);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination[] destinations = new Destination[] {
    		session.createQueue("${jgame.jms.shipyard.destination:Shipyard}"),
    		session.createQueue("${jgame.jms.shipyard.failedDestination:Shipyard_FAILED}"),
    		session.createQueue("${jgame.jms.constructions.destination:Constructions}"),
    		session.createQueue("${jgame.jms.constructions.destination:Constructions_FAILED}")
        };
        
        Destination management = session.createTopic(ScheduledMessage.AMQ_SCHEDULER_MANAGEMENT_DESTINATION);

        AtomicInteger nbRemovedMessages = new AtomicInteger(0);
        
        for (Destination destination : destinations) {
            // Create the Consumer to receive the scheduled message
        	MessageConsumer consumer = session.createConsumer(destination);
        	
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    nbRemovedMessages.incrementAndGet();
                }
            });
        }
        
        // Send the remove request
        MessageProducer producer = session.createProducer(management);
        Message request = session.createMessage();
        request.setStringProperty(ScheduledMessage.AMQ_SCHEDULER_ACTION, ScheduledMessage.AMQ_SCHEDULER_ACTION_REMOVEALL);
        producer.send(request);

        log.info("Removed {} scheduled JMS messages", nbRemovedMessages);
    }
}