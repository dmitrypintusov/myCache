package by.pintusov.myCache;

import by.pintusov.myCache.api.ICache;
import by.pintusov.myCache.strategy.CacheHolder;
import by.pintusov.myCache.strategy.CacheStrategies;
import org.junit.*;

/**
 * My cache one level memory test
 * @author pintusov
 */
public class MyCacheOneLevelMemoryTest {

	private ICache cache;
	private CacheStrategies cacheStrategies;
	private CacheHolder cacheHolder;

	private int expectedSize;
	private int actualSize;
	private final String expectedKey = "key1";
	private final String expectedValue = "value1";


	@Before
	public void setUp () throws Exception {
		cacheStrategies = new CacheStrategies();
		cacheHolder = cacheStrategies.ONE_LEVEL_MEMORY;
		cache = cacheHolder.getCache();
	}

	@Test
	public void testPut () throws Exception {
		expectedSize = 2;
		createCache();
		cache.put("key2", "value2");
		actualSize = cache.size();
		Assert.assertEquals("Cache put () test:", expectedSize, actualSize);
	}

	@Test
	public void testExtract () throws Exception {
		createCache();
		String actualValue = (String) cache.extract(expectedKey);
		Assert.assertEquals("Cache extract () test:", expectedValue, actualValue);
	}

	@Test
	public void testRemove () throws Exception {
		expectedSize = 0;
		createCache();
		cache.remove(expectedKey);
		actualSize = cache.size();
		Assert.assertEquals("Cache remove () test:", expectedSize, actualSize);
	}

	@Test
	public void testSize () throws Exception {
		expectedSize = 1;
		createCache();
		actualSize = cache.size();
		Assert.assertEquals("Cache size () test:", expectedSize, actualSize);
	}

	private void createCache () throws Exception {
		cache.put(expectedKey, expectedValue);
	}
}
