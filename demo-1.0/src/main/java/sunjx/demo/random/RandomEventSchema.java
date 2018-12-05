package sunjx.demo.random;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

/**
 * @Auther: sunjx
 * @Date: 2018/12/5 0005 11:19
 * @Description:
 */
public class RandomEventSchema implements DeserializationSchema<RandomEvent>, SerializationSchema<RandomEvent> {

    @Override
    public RandomEvent deserialize(byte[] bytes) throws IOException {
        return RandomEvent.fromString(new String(bytes));
    }

    @Override
    public boolean isEndOfStream(RandomEvent randomEvent) {
        return false;
    }

    @Override
    public byte[] serialize(RandomEvent randomEvent) {
        return randomEvent.toString().getBytes();
    }

    @Override
    public TypeInformation<RandomEvent> getProducedType() {
        return TypeInformation.of(RandomEvent.class);
    }
}
