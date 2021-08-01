import static org.junit.Assert.*;

import org.junit.Test;

public class SimilaritiesTest {

	@Test
	public void testCompareTo() {
		Similarities s1 = new Similarities("apple", "banana");
		Similarities s2 = new Similarities("coconut", "duck");
		
		s1.setCount(0);
		s2.setCount(0);
		assertEquals(String.valueOf(s1.compareTo(s2)), "-2");
		assertEquals(String.valueOf(s2.compareTo(s1)), "2");
		assertEquals(String.valueOf(s1.compareTo(s1)), "0");
		
		s1.setCount(0);
		s2.setCount(1);
		assertEquals(String.valueOf(s1.compareTo(s2)), "1");
		assertEquals(String.valueOf(s2.compareTo(s1)), "-1");
		assertEquals(String.valueOf(s2.compareTo(s2)), "0");
	}

}