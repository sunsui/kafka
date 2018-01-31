package producer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
//WriteDataToKafka 
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TripProducer {
    public static void main(String[] args) throws IOException {
         Properties props = new Properties();
         props.put("bootstrap.servers", "192.168.33.101:9092");
         //The key.serializer and value.serializer instruct how to turn the key and value objects the user provides with their ProducerRecord into bytes.
         props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
         props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

         File file=new File("/home/hadoop/test/201407tripdata.txt");
         InputStreamReader read = new InputStreamReader(
                 new FileInputStream(file));
         BufferedReader bufferedReader = new BufferedReader(read);
         String lineTxt;
         while((lineTxt = bufferedReader.readLine()) != null){
        	 //创建kafka的生产者类
             Producer<String, String> producer = new KafkaProducer<String, String>(props);
             producer.send(new ProducerRecord<String, String>("tripdata1",null,lineTxt));
             producer.close();
         }
    }
}