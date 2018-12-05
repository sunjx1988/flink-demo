package sunjx.demo.random;

import lombok.Data;

/**
 * @Auther: sunjx
 * @Date: 2018/12/5 0005 11:20
 * @Description:
 */
@Data
public class RandomEvent {

    private String random;

    private long timestamp;

    @Override
    public String toString() {
        return random +"," + timestamp;
    }

    public RandomEvent(String random, long timestamp) {
        this.random = random;
        this.timestamp = timestamp;
    }

    public static RandomEvent fromString(String str){

        String[] split = str.split(",");
        return new RandomEvent(split[0], Long.valueOf(split[1]));
    }
}
