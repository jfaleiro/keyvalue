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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultKeyValue<K, V> implements KeyValue<K, V> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DefaultKeyValue.class);

	@SuppressWarnings("unused")
	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	private final ConcurrentMap<K, V> map = new ConcurrentHashMap<>();

	// private final BlockingQueue<Date> timestamps = new
	// PriorityBlockingQueue<Date>(
	// 100, Collections.reverseOrder());

	private final BlockingQueue<Date> timestamps = new PriorityBlockingQueue<Date>(
			100, new Comparator<Date>() {
				@Override
				public int compare(Date o1, Date o2) {
					if (o1.before(o2)) {
						return -1;
					} else if (o2.before(o1)) {
						return +1;
					} else {
						return 0;
					}
				}
			});

	private final ConcurrentMap<Date, List<K>> timestampToKey = new ConcurrentHashMap<>();

	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);

	public DefaultKeyValue(long interval, TimeUnit unit) {
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				synchronized (DefaultKeyValue.this) {
					final Date now = new Date();
					Date t = timestamps.peek();
					while (t != null && t.before(now)) {
						final List<K> keys = timestampToKey.remove(t);
						if (keys != null) {
							keys.forEach(new Consumer<K>() {
								@Override
								public void accept(K key) {
									map.remove(key);
								}
							});
						}
						timestamps.poll();
						t = timestamps.peek();
					}
				}
			}

		}, 0, interval, unit);
	}

	public DefaultKeyValue() {
		this(30, TimeUnit.SECONDS);
	}

	@Override
	public V put(K key, V value) {
		return put(key, value, 0L);
	}

	@Override
	public synchronized V put(K key, V value, long milis) {
		if (milis > 0L) {
			final Date ts = new Date(System.currentTimeMillis() + milis);
			timestamps.add(ts);
			if (!timestampToKey.containsKey(ts)) {
				timestampToKey.put(ts, new ArrayList<K>());
			}
			timestampToKey.get(ts).add(key);
		}
		return map.put(key, value);
	}

	@Override
	public V get(K key) {
		return map.get(key);
	}

	@Override
	public V remove(K key) {
		return map.remove(key);
	}

	public void shutdown() {
		scheduler.shutdown();
	}

	@Override
	public void clear() {
		map.clear();
		timestamps.clear();
		timestampToKey.clear();
	}

}
