package de.morknet.mdsc3.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter
{
	private final static Map<String, AtomicInteger> m_Counting = new HashMap<String, AtomicInteger>();

	private Counter()
	{
	}

	public static void setValue(String container, String value)
	{
		AtomicInteger counter = m_Counting.get(container);
		
		if (counter == null)
		{
			counter = new AtomicInteger();
			m_Counting.put(container, counter);
		}
		counter.set(Integer.parseInt(value));
	}
	
	public static void addValue(String container, String value)
	{
		m_Counting.get(container).addAndGet(Integer.parseInt(value));
	}
	
	public static Integer getValue(String container)
	{
		return m_Counting.get(container).get();
	}
}
