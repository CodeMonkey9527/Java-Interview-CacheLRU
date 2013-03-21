package frank.interview;

import static org.junit.Assert.*;

import org.junit.Test;

import frank.interview.Cache;

public class CacheLRUTest {

	@Test
	public void test() {
		Cache<String, Integer> cache = new CacheLRU<String, Integer>(3);
		cache.put("A", 1);
		cache.put("B", 2);
		cache.dump();
		cache.put("C", 3);
		cache.dump();
		cache.put("D", 4);
		cache.dump();
		assertNull(cache.get("A"));
		assertEquals(new Integer(2), cache.get("B"));
		cache.dump();
		cache.put("D", 1);
		cache.dump();
		assertEquals(new Integer(3), cache.get("C"));
		cache.dump();
		assertEquals(new Integer(1), cache.get("D"));
		cache.dump();
	}

}
