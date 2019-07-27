package state

class CounterSystem(private var n: Int = 0) {

  def increment(): Int = {
    if (n < Int.MaxValue) {
      n = n + 1
      n
    } else {
      throw new RuntimeException("Overflow")
    }
  }

  def get: Int = n

}
