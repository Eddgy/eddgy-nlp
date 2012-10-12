package com.eddgy.nlp

/**
 * A simple representation of a point in some n-dimensional space.
 *
 * @param coord  A sequence of Doubles that define this point's coordinates
 *               in some space.
 */
case class Point(val coord: IndexedSeq[Double]) {
  import scala.math.sqrt

  // Zip the coordinates of this Point with those of another.
  def zip(that: Point) = this.coord.zip(that.coord)

  // Create a new Point formed by pairwise addition of the coordinates of this
  // Point with those of another.
  def ++(that: Point) = Point(this.zip(that).map { case (a, b) => a + b })

  // Create a new Point formed by pairwise subtraction of the coordinates of this
  // Point with those of another.
  def -(that: Point) = Point(this.zip(that).map { case (a, b) => a - b })

  // Create a new point that divides every value in this Point by a common
  // divisor.
  def /(divisor: Double) = Point(coord.map(_ / divisor))

  // Compute the dot product between this Point and another.
  def dotProduct(that: Point) = this.zip(that).map { case (x, y) => x * y }.sum

  // Create a new point formed by taking the absolute value of every element 
  // of this Point.
  lazy val abs = Point(coord.map(_.abs))

  // Compute the vector norm of this Point.
  lazy val norm = sqrt(this.dotProduct(this))

  // The number of elements in this Point.
  lazy val numDimensions = coord.length

  // The sum of all the values in this Point.
  lazy val sum = coord.sum

  // A terse String representation for the coordinates of this Point.
  override def toString = "[" + coord.mkString(",") + "]"
}
