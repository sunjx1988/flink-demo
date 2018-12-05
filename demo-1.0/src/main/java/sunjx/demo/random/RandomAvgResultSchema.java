package sunjx.demo.random;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

/**
 * @Auther: sunjx
 * @Date: 2018/12/5 0005 14:35
 * @Description:
 */
public class RandomAvgResultSchema  implements DeserializationSchema<String>, SerializationSchema<String> {
    @Override
    public String deserialize(byte[] bytes) throws IOException {
        return new String(bytes);
    }

    @Override
    public boolean isEndOfStream(String str) {
        return false;
    }

    @Override
    public byte[] serialize(String str) {
        return str.getBytes();
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return TypeInformation.of(String.class);
    }
}
