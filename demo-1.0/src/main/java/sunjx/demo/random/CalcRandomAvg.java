package sunjx.demo.random;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Properties;

import static org.apache.flink.streaming.api.TimeCharacteristic.EventTime;

/**
 * @Auther: sunjx
 * @Date: 2018/12/5 0005 11:11
 * @Description:
 */
@Slf4j
public class CalcRandomAvg {

    public static void main(String[] args) throws Exception{

        String input_topic = "random";
        String output_topic = "random_out";
        String flink_server = "sunjx:9092";
        String zk_server = "sunjx:2181";
        String group_id = "test";
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);
        Properties properties = parameterTool.getProperties();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        if(args == null || args.length == 0){
            properties.put("input-topic", input_topic);
            properties.put("output-topic", output_topic);
            properties.put("bootstrap.servers", flink_server);
            properties.put("zookeeper.connect", zk_server);
            properties.put("group.id", group_id);
        }

        env.setStreamTimeCharacteristic(EventTime);
        env.enableCheckpointing(5000);
        env.getConfig().setGlobalJobParameters(parameterTool);
        env.getConfig().disableSysoutLogging();

        DataStream<RandomEvent> randomStream =
                env.addSource(new FlinkKafkaConsumer010<>(
                StringUtils.isEmpty(parameterTool.get("input-topic")) ? input_topic : parameterTool.getRequired("input-topic"),
                new RandomEventSchema(),
                properties))
                .assignTimestampsAndWatermarks(new CustomWatermarkExtractor());

        DataStream<String> avgStream = randomStream.timeWindowAll(Time.seconds(5)).apply(new AvgWindowFunction());
        avgStream.addSink(new FlinkKafkaProducer010<>(
                StringUtils.isEmpty(parameterTool.get("output-topic")) ? output_topic : parameterTool.getRequired("output-topic"),
                new RandomAvgResultSchema(),
                properties));

        env.execute("calc random avg");
    }

    private static class CustomWatermarkExtractor implements AssignerWithPeriodicWatermarks<RandomEvent> {

        private static final long serialVersionUID = -742759155861320823L;

        private long currentTimestamp = Long.MIN_VALUE;

        @Override
        public long extractTimestamp(RandomEvent event, long previousElementTimestamp) {
            this.currentTimestamp = event.getTimestamp();
            return event.getTimestamp();
        }

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentTimestamp == Long.MIN_VALUE ? Long.MIN_VALUE : currentTimestamp - 1);
        }
    }

    private static class AvgWindowFunction implements AllWindowFunction<RandomEvent, String, TimeWindow> {

        @Override
        public void apply(TimeWindow timeWindow, Iterable<RandomEvent> iterable, Collector<String> collector) throws Exception {
            int sum = 0;
            int count = 0;

            StringBuilder sb = new StringBuilder();
            Iterator<RandomEvent> iterator = iterable.iterator();
            while (iterator.hasNext()){
                RandomEvent number = iterator.next();
                count ++;
                sum += Integer.valueOf(number.getRandom());
                sb.append(number.getRandom()).append(",");
            }

            sb.append(sum / count);
            collector.collect(sb.toString());
        }
    }
}
