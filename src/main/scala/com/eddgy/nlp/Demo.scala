package com.eddgy.nlp

import com.eddgy.nlp.transformer.PcaTransformer
import data.FederalistPapers._
import axle.matrix.JblasMatrixFactory._
import axle.ml.KMeans._

object Demo {

  val k = 4
  val numIterations = 100

  def withoutAxle(): Unit = {
    val df = EuclideanDistance // or CosineDistance or ManhattanDistance
    val points = articles.map(article => Point(fullFeatures(article))) // or simpleFeatures
    val pointTransformer = new PcaTransformer(points, 0.95) // or IdentityTransformer or ZscoreTransformer(points)
    val transformedPoints = pointTransformer(points)
    val km = new Kmeans(transformedPoints, df, 0.0001, numIterations)
    val (x, centroids) = km.run(k) // second argument is 'restarts'?
    val (y, predictedClusterIds) = km.computeClusterMemberships(centroids)
    val confusionMatrix = ClusterConfusionMatrix(articles.map(_.author), k, predictedClusterIds)
  }

  def withAxle(): Unit = {

    // TODO: use df
    // TODO: use 0.0001
    // TODO: numFeatures should be inferred
    // TODO: use kmeans internal pca transformation
    // val pointTransformer = new PcaTransformer(points, 0.95) // or IdentityTransformer or ZscoreTransformer(points)
    // val transformedPoints = pointTransformer(points)
    // val data = transformedPoints.map(_.coord)

    val classifier = cluster(articles, numFullFeatures,
      (article: FederalistArticle) => fullFeatures(article),
      (features: Seq[Double]) => null.asInstanceOf[FederalistArticle], // TODO
      k, numIterations)

    val confusionMatrix = classifier.confusionMatrix(articles, (article: FederalistArticle) => article.author)

  }

}