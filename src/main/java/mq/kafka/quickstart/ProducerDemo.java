package mq.kafka.quickstart;

import org.apache.kafka.clients.producer.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by liequ on 18/1/20.
 */
public class ProducerDemo {
    private Map<Long, Integer> keyPartitons = new HashMap<>();
    private final KafkaProducer<Integer, String> producer;
    private final String topic;

    public ProducerDemo(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.33.20:9092");
        props.put("client.id", "DemoProducer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    public void sendMsg(Integer key, String value) throws ExecutionException, InterruptedException {
        Future<RecordMetadata> future = producer.send(new ProducerRecord<>(
                topic, key, value), new DemoCallBack(System.currentTimeMillis(), key, value));
        System.out.print("send msg !");
        RecordMetadata recordMetadata = future.get();
        System.out.println(recordMetadata.toString());

    }


    class DemoCallBack implements Callback {

        private final long startTime;
        private final long key;
        private final String message;

        public DemoCallBack(long startTime, long key, String message) {
            this.startTime = startTime;
            this.key = key;
            this.message = message;
        }

        /**
         * A callback method the user can implement to provide asynchronous handling of request completion. This method will
         * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
         * non-null.
         *
         * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
         *                  occurred.
         * @param exception The exception thrown during processing of this record. Null if no error occurred.
         */
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            System.out.println("call back ");
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null) {
                System.out.println("metadata is not null");
                int partition = metadata.partition();
                if(keyPartitons.get(key) == null) {
                    keyPartitons.put(key, partition);
                } else {
                    if(keyPartitons.get(key) != partition) {
                        System.out.println("key " + key + ", before:" + keyPartitons.get(key) + ", after: " + partition);
                    }
                }
                System.out.println("消息(" + key + ", " + message + ") " +
                        "发送到分区(" + metadata.partition() + "), " +
                        "偏移量(" + metadata.offset() + ") " +
                        "耗时 " + elapsedTime + " ms");
            } else {
                exception.printStackTrace();
                System.out.println("eeeor");
            }
        }
    }


    public static void main(String[] args) {
        ProducerDemo producerDemo = new ProducerDemo("liequ-index");
        try {
            producerDemo.sendMsg(1007,"message demo 1007");
        } catch (ExecutionException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

}
