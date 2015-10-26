/**
 *
 *   keyvalue - simple key value storage-with-expiration
 *
 *   Copyright (C) 2015 Jorge M. Faleiro Jr.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package keyvalue;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultKeyValueTest {

	private DefaultKeyValue<String, Integer> kv;

	@Before
	public void before() {
		kv = new DefaultKeyValue<>(1, TimeUnit.MILLISECONDS);
	}

	@After
	public void after() {
		kv.clear();
		kv.shutdown();
	}

	@Test
	public void testRemove() {
		kv.put("A", 1);
		assertEquals(Integer.valueOf(1), kv.get("A"));
		assertEquals(Integer.valueOf(1), kv.remove("A"));
		assertNull(kv.remove("A"));
	}

	@Test
	public void testPut() {
		kv.put("A", 1);
		assertEquals(Integer.valueOf(1), kv.put("A", 2));
		assertEquals(Integer.valueOf(2), kv.get("A"));
	}

	@Test
	public void testPutTimeout() throws InterruptedException {
		kv.put("A", 1, 1);
		kv.put("B", 2, 200);
		Thread.sleep(100);
		assertEquals(null, kv.get("A"));
		assertEquals(Integer.valueOf(2), kv.get("B"));
	}

	@Test
	public void testLoad() {
		for (int i = 0; i < 10_000_000; i++) {
			kv.put(String.valueOf(i), i);
		}
	}
	
	@Test
	public void testLoadTimeout() {
		for (int i = 0; i < 10_000_000; i++) {
			kv.put(String.valueOf(i), i, 10);
		}
	}

}
