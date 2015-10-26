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

/**
 * Simple key value storage.
 * 
 * @author jfaleiro
 *
 * @param <K>
 *            key type
 * @param <V>
 *            value type
 */
public interface KeyValue<K, V> {

	/**
	 * Insert key value.
	 * 
	 * @param key
	 *            key of entry
	 * @param value
	 *            value of entry
	 * @return the previous value of K
	 */
	V put(K key, V value);

	/**
	 * Insert key value, expiring at time milis from now.
	 * 
	 * 
	 * @param key
	 *            key of entry
	 * @param value
	 *            value of entry
	 * @param milis
	 *            expiration in the future
	 * @return the previous value of K
	 */
	V put(K key, V value, long milis);

	/**
	 * Get value of key.
	 * 
	 * @param key
	 *            the key
	 * @return the value
	 */
	V get(K key);

	/**
	 * Remove key.
	 * 
	 * @param key
	 *            the key
	 * @return previous value.
	 */
	V remove(K key);

	/**
	 * Clear all values and keys.
	 */
	void clear();

}
