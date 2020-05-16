/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author Kshitij
 */
public interface Hasher<K> 
{
	/**
	 * Hashes a generic key to a positive signed 32-bit integer.
	 * 
	 * @param key Generic key of any type to hash.
	 * @return A 32-bit integer.
	 */
	public int hash(K key);
}
