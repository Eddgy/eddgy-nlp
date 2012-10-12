package com.eddgy.nlp

trait DistanceFunction extends ((Point, Point) => Double)

object DistanceFunction {
  val cosine = CosineDistance
  val manhattan = ManhattanDistance
  val euclidean = EuclideanDistance
}

object CosineDistance extends DistanceFunction {
  def apply(x: Point, y: Point) = 1 - x.dotProduct(y) / (x.norm * y.norm)
}

object ManhattanDistance extends DistanceFunction {
  def apply(x: Point, y: Point) = (x - y).abs.sum
}

object EuclideanDistance extends DistanceFunction {
  def apply(x: Point, y: Point) = (x - y).norm
}
