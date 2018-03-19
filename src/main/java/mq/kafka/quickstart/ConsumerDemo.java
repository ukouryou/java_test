package mq.kafka.quickstart;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by liequ on 18/1/17.
 */
public class ConsumerDemo {


    private KafkaConsumer<Integer, String> consumer;

    private final String topic;

    public ConsumerDemo(String topic) {
        this.topic = topic;

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.33.20:9092,192.168.33.21:9092,192.168.33.22:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoConsumer2");
//        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "client1");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");//latest,earliest
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);

    }

    public void receiveMsg() {
        consumer.subscribe(Collections.singletonList(this.topic));

            System.out.println("start consume!");
            ConsumerRecords<Integer, String> records = consumer.poll(1000);
            int count = records.count();
            System.out.println("get  records count :"  + count);
            for (ConsumerRecord<Integer, String> record : records)
                System.out.printf("topic = %s, offset = %d, key = %s, value = %s%n", record.topic(), record.offset(), record.key(), record.value());

        consumer.commitAsync();
//        consumer.partitionsFor()
//        consumer.wakeup();throw exception ,we need process exceptions
    }



    public static void main(String[] args) {

        ConsumerDemo consumerDemo = new ConsumerDemo("test");
        consumerDemo.receiveMsg();

    }
}
