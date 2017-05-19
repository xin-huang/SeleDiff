package xin.bio.popgen.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public final class TimeMeasurement {

	public static long getCpuTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ? 
				bean.getCurrentThreadCpuTime() : 0L;
	}
	
	public static long getUserTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ?
				bean.getCurrentThreadUserTime() : 0L;
	}
	
	public static long getSystemTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ?
				(bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime()) : 0L;
	}
	
}
