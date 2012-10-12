package com.eddgy.nlp

import com.eddgy.nlp.transformer.PcaTransformer

object Demo {

  import data.FederalistPapers._

  val df = EuclideanDistance // or CosineDistance or ManhattanDistance
  val k = 4
  val numIterations = 100

  val points = fullPoints(articles) // or simplePoints(articles)

  def withoutAxle(): Unit = {
    val pointTransformer = new PcaTransformer(points, 0.95) // or IdentityTransformer or ZscoreTransformer(points)
    val transformedPoints = pointTransformer(points)
    val km = new Kmeans(transformedPoints, df, 0.0001, numIterations)
    val (x, centroids) = km.run(k) // second argument is 'restarts'?
    val (y, predictedClusterIds) = km.computeClusterMemberships(centroids)
    val confusionMatrix = ClusterConfusionMatrix(articles.map(_.author), k, predictedClusterIds)
  }

  def withAxle(): Unit = {

    import axle.ml.KMeans._

    // TODO:
    val pointTransformer = new PcaTransformer(points, 0.95) // or IdentityTransformer or ZscoreTransformer(points)
    val transformedPoints = pointTransformer(points)

    val data = transformedPoints.map(_.coord)

    // TODO: use df
    // TODO: use 0.0001
    val classifier = cluster(data,
      N = data(0).length, // TODO: there should be a way to infer this
      featureExtractor = (obj: Seq[Double]) => obj,
      constructor = (features: Seq[Double]) => features,
      K = k,
      iterations = numIterations)

    val predictions = data.map(classifier.classify(_))

    // TODO:
    val confusionMatrix = ClusterConfusionMatrix(articles.map(_.author), k, predictions)

  }

}