package trip

import kafka.serializer.StringDecoder
import java.util.HashMap
import java.util.Calendar
import org.apache.kafka.clients.producer.{ProducerConfig, KafkaProducer, ProducerRecord}
import org.apache.kafka._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf

object NumByGender {
  def main(args: Array[String]) {
        val sparkConf = new SparkConf().setMaster("local[2]").setAppName("NumByGender")
        val scc = new StreamingContext(sparkConf, Duration(30000))
      //  scc.checkpoint(".") // 因为使用到了updateStateByKey,所以必须要设置checkpoint
        val topics = Set("tripdata1") //我们需要消费的kafka数据的topic
        val kafkaParam = Map(
                "metadata.broker.list" -> "192.168.33.101:9092" // kafka的broker list地址
            )
        val stream= createStream(scc, kafkaParam, topics)
        val tmp = stream.map(_._2)      // 取出value
                  .map(rec => rec.split(","))
                  .map(rec => (rec(14),1))
                  .reduceByKey(_+_)
                  .transform(rec => rec.sortByKey(ascending = false))
                  
          
        tmp.print() // 打印前10个数据
        tmp.saveAsTextFiles("/home/hadoop/test/NumByGender")   
        scc.start() // 真正启动程序
        scc.awaitTermination() //阻塞等待
    }

    val updateFunc = (currentValues: Seq[Int], preValue: Option[Int]) => {
        val curr = currentValues.sum
        val pre = preValue.getOrElse(0)
        Some(curr + pre)
    }
    /**
     * 创建一个从kafka获取数据的流.
     * @param scc           spark streaming上下文
     * @param kafkaParam    kafka相关配置
     * @param topics        需要消费的topic集合
     * @return
     */
    def createStream(scc: StreamingContext, kafkaParam: Map[String, String], topics: Set[String]) = {
        KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](scc, kafkaParam, topics)
    }
    
}