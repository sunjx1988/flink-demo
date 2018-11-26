package sunjx.demo.kafka;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 * @Auther: sunjx
 * @Date: 2018/11/26 0026 14:43
 * @Description:
 */
public class MemoryUsageExtrator {
    private static OperatingSystemMXBean mxBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    /**
     * Get current free memory size in bytes
     * @return  free RAM size
     */
    public static long currentFreeMemorySizeInBytes() {
        return mxBean.getFreePhysicalMemorySize();
    }
}
