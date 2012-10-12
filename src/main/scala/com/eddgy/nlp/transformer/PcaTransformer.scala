package com.eddgy.nlp.transformer

import com.eddgy.nlp._

import Jama.Matrix
import pca_transform.PCA
import com.eddgy.nlp.Point
import scala.Array.fallbackCanBuildFrom

class PcaTransformer(points: IndexedSeq[Point], cutoff: Double) extends PointTransformer {

  val scaler = new ZscoreTransformer(points)

  val pca = new PCA(new Matrix(scaler(points).map(_.coord.toArray).toArray))

  val eigVals = (0 until pca.getOutputDimsNo).map(pca.getEigenvalue(_))
  val eigValsSq = eigVals.map(x => x * x)
  val propVariance = eigValsSq.map(_ / eigValsSq.sum)
  val numComponents = propVariance.scan(0.0)(_ + _).indexWhere(cutoff<)

  def apply(points: IndexedSeq[Point]): IndexedSeq[Point] =
    pca.transform(new Matrix(scaler(points).map(_.coord.toArray).toArray),
      PCA.TransformationType.ROTATION)
      .getArray.map { tc => Point(tc.take(numComponents).toIndexedSeq) }

}
