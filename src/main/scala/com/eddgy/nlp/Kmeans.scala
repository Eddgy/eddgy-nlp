package com.eddgy.nlp

import org.apache.commons.logging.LogFactory
import org.apache.log4j.Logger
import org.apache.log4j.Level

class Kmeans(
  points: IndexedSeq[Point],
  distance: DistanceFunction,
  minChangeInDispersion: Double = 0.0001,
  maxIterations: Int = 100) {

  private[this] val numDimensions = points.head.numDimensions
  private[this] val origin = Point(IndexedSeq.fill(numDimensions)(0.0))
  private[this] val random = new util.Random(compat.Platform.currentTime)

  def run(k: Int, restarts: Int = 25): (Double, IndexedSeq[Point]) =
    ((1 to restarts).map { _ =>
      moveCentroids(chooseRandomCentroids(k))
    }).minBy(_._1)

  def moveCentroids(centroids: IndexedSeq[Point]): (Double, IndexedSeq[Point]) = {

    // Inner recursive function for computing next centroids
    def inner(centroids: IndexedSeq[Point],
      lastDispersion: Double,
      iteration: Int): (Double, IndexedSeq[Point]) = {

      val (dispersion, memberships) = computeClusterMemberships(centroids)
      val updatedCentroids = computeCentroids(memberships)

      val dispersionChange = lastDispersion - dispersion

      if (iteration > maxIterations || dispersionChange < minChangeInDispersion)
        (lastDispersion, centroids)
      else
        inner(updatedCentroids, dispersion, iteration + 1)
    }

    inner(centroids, Double.PositiveInfinity, 1)
  }

  def computeClusterMemberships(centroids: IndexedSeq[Point]) = {
    val (squaredDistances, memberships) = points.map { point =>
      val distances = centroids.map(distance(_, point))
      val shortestDistance = distances.min
      val closestCentroid = distances.indexWhere(shortestDistance==)
      (shortestDistance * shortestDistance, closestCentroid)
    }.unzip
    (squaredDistances.sum, memberships)
  }

  private[this] def computeCentroids(memberships: IndexedSeq[Int]) =
    memberships.zip(points)
      .groupBy(_._1)
      .map({ case (k, vs) => (k, vs.map(_._2).reduce(_ ++ _) / vs.length.toDouble) })
      .toList
      .sortBy(_._1)
      .map(_._2)
      .toIndexedSeq

  private[this] def chooseRandomCentroids(k: Int) = {
    random.shuffle(points).take(k)
  }

}
