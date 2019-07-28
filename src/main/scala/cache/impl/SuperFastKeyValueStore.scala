package cache.impl

import cache.KeyValueSystem
import org.cliffc.high_scale_lib.NonBlockingHashMap

class SuperFastKeyValueStore[K, V] extends KeyValueSystem[K, V] {

  val entry = new NonBlockingHashMap[K, V]()

  override def put(key: K, value: V): Boolean = {
    entry.put(key, value)
    true
  }

  override def get(key: K): Option[V] = {
    val value = entry.get(key)
    Option(value)
  }

  override def remove(key: K): Boolean = {
    val currentValue = Option(entry.remove(key))
    currentValue match {
      case Some(_) => true
      case None => false
    }
  }

  override def size(): Int = entry.size
}
