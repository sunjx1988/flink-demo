package sunjx.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.io.File;

/**
 * @Auther: sunjx
 * @Date: 2018/11/26 0026 11:04
 * @Description:
 */
@Slf4j
public class WordCount {

    private static final String JOB_NAME = "Word Count Job";

    private static final String TEXT_FILE_PATH = "/data/flink/data.txt";

    private static final String CSV_FILE_PATH = "";

    private static final String OUT_FILE_PATH = "/data/flink/out.txt";

    public static void main(String[] args) {
        try {
            //获取运行环境
            ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();

            //读取数据
            DataSet<String> dataSet = environment.readTextFile(TEXT_FILE_PATH);
    //        DataSet<String> dataSet = environment.readCsvFile(CSV_FILE_PATH);

            //统计逻辑处理
            DataSet<Tuple2<String, Integer>> resultSet = dataSet
                    .flatMap(new Tokenizer())
                    .groupBy(0)
                    .sum(1);

            FileUtils.forceDeleteOnExit(new File(OUT_FILE_PATH));

            //输出结果
            resultSet.writeAsCsv(OUT_FILE_PATH, "\n", " ");

            //执行
            environment.execute(JOB_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {

        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // normalize and split the line
            String[] tokens = value.toLowerCase().split("\\W+");

            // emit the pairs
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<>(token, 1));
                }
            }
        }
    }
}
