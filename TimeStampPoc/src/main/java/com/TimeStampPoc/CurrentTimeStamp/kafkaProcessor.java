package com.TimeStampPoc.CurrentTimeStamp;

import com.TimeStampPoc.CurrentTimeStamp.Stream.Schema.Schema.TimeStamp;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

@Component
public class kafkaProcessor {


    @Autowired
    MongoTemplate mongoTemplate;





    private static final Logger log = LoggerFactory.getLogger("kafkaProducer.class");
    private HashMap<String, Object> props = new HashMap<>();



    public kafkaProcessor(HashMap<String, Object> props) {
        this.props = props;

    }

    private Map<String, Object> prop1;



    

    StreamsBuilder builder = new StreamsBuilder();

    private static final String TOPIC = "input1Topic";
    private static final String TOPIC1 = "outputTopic";
    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
    MongoDatabase database = mongoClient.getDatabase("mydb");

    MongoCollection<Document> collection = database.getCollection("TimeStamp");

    int c=0;
    @Scheduled(fixedRate = 1)
    public void logCurrentTime() {


        var event = eventInitializer();
        KStream<String, TimeStamp> streams = builder.stream(TOPIC);

        ProducerRecord<String, TimeStamp> producerRecord = new ProducerRecord<>("input1Topic", "key", event);
        c+=1;

        log.info("Values:" + producerRecord);
        log.info("Count:"+c);
        streams.mapValues(value -> {
            return producerRecord.value();
        }).to(TOPIC1);
      /*  KStream<String, TimeStamp> streams1 = builder.stream(TOPIC1);
        GlobalKTable<String, Object> globalKTable =builder.globalTable(TOPIC1, Consumed.with(Topology.AutoOffsetReset.LATEST));
*/
        ConsumerRecord<String, Object> record = new ConsumerRecord<>(TOPIC1,0,0,"key",producerRecord.value());

        String key = record.key();
        String value = record.value().toString();
        DBObject document=new BasicDBObject();
        document.put("topic",record.topic());
        document.put("key",record.key());
        document.put("value",record.value().toString());
         mongoTemplate.insert(document,"TimeStamp");


            }


    public TimeStamp eventInitializer() {
        var event=new TimeStamp();
        event.setCurrentTime(ZonedDateTime.now().toString());
        List<CharSequence> list = new ArrayList<>();
        // add 5 element in ArrayList
        list.add("Amazon");
        list.add("Flipkart");
        list.add("Myntra");
        list.add("Ajio");
        list.add("Meesho");
        Random rand = new Random();
        event.setMerchantName(list.get(rand.nextInt(list.size())));
        List<Long> list1 = new ArrayList<>();// add 5 element in ArrayList
        list1.add(1450L);
        list1.add(1451L);
        list1.add(1452L);
        list1.add(1453L);
        list1.add(1454L);
        event.setTransactionAmt(list1.get(rand.nextInt(list.size())));
        List<CharSequence> list2 = new ArrayList<>();
        list2.add("US");
        list2.add("ALL");
        list2.add("AFN");
        list2.add("ARS");
        list2.add("BSD");
        event.setCurrency(list2.get(rand.nextInt(list.size())));
        event.setTransactionId(UUID.randomUUID().toString());

        return event;



    }
        }



