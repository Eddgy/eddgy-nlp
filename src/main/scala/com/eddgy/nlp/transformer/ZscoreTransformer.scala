package com.eddgy.nlp.transformer

import com.eddgy.nlp._

/**
 * http://en.wikipedia.org/wiki/Standard_score
 */

class ZscoreTransformer(points: IndexedSeq[Point]) extends PointTransformer {

  private def square = (x: Double) => x * x

  val tpoints = points.map(_.coord).transpose

  val means = tpoints.map(values => values.sum / values.length)

  val standardDeviations = tpoints.zip(means).map {
    case (values, mean) =>
      val squaredDifferences = values.map(v => square(v - mean))
      if (squaredDifferences == 0.0) 1.0
      else math.sqrt(squaredDifferences.sum)
  }

  def apply(points: IndexedSeq[Point]): IndexedSeq[Point] = {
    points.map { point =>
      val transformed = point.coord.zip(means.zip(standardDeviations)).map {
        case (x, (mean, sdev)) => (x - mean) / sdev
      }
      Point(transformed)
    }
  }

}
