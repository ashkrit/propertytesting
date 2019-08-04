package cache


trait KeyValueSystem[K, V] {

  def put(key: K, value: V): Boolean

  def get(key: K): Option[V]

  def size(): Int

  def remove(key: K): Boolean

}
