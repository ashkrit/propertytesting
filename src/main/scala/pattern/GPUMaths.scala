package pattern

object GPUMaths {

  def sum(values: Int*): Long = {
    values.par.sum
  }

  def sort(values: Seq[Int]): Seq[Int] = {
    values.sorted
  }
}

