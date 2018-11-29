# flink-demo

[flink 中文文档](http://flink.iteblog.com/index.html)

[flink kafka 集成](https://www.cnblogs.com/huxi2b/p/7219792.html)

[flink 消费 kafka消息 输出到redis](https://www.cnblogs.com/jiashengmei/p/9084057.html)

[Flume+Kafka+Flink+Redis构建大数据实时处理系统：实时统计网站PV、UV展示](https://yq.aliyun.com/articles/635327)


> demo 1

```
sunjx.demo.kafka.KafkaDemo

--input-topic 'test' --output-topic 'test_out' --bootstrap.servers 'localhost:9092' --zookeeper.connect 'localhost:2181' --group.id 'test'
```
