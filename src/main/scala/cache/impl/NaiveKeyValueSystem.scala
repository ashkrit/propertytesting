package cache.impl

import cache.KeyValueSystem

import scala.collection.mutable._

class NaiveKeyValueSystem[K, V] extends KeyValueSystem[K, V] {

  val entry = new HashMap[K, V]()

  override def put(key: K, value: V): Boolean = {
    entry.put(key, value)
    true
  }

  override def get(key: K): Option[V] = {
    entry.get(key)
  }

  override def remove(key: K): Boolean = {
    val currentValue = entry.remove(key)
    currentValue match {
      case Some(_) => true
      case None => false
    }
  }

  override def size(): Int = entry.size
}
