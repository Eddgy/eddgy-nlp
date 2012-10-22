package com.eddgy.nlp

import data.FederalistPapers._

object Demo {

  val k = 4
  val numIterations = 100

  def withoutAxle(): Unit = {
    import com.eddgy.nlp.transformer.PcaTransformer
    val df = EuclideanDistance
    val points = articles.map(article => Point(fullFeatures(article)))
    val pointTransformer = new PcaTransformer(points, 0.95)
    val transformedPoints = pointTransformer(points)
    val km = new Kmeans(transformedPoints, df, 0.0001, numIterations)
    val (x, centroids) = km.run(k) // second argument is 'restarts'?
    val (y, predictedClusterIds) = km.computeClusterMemberships(centroids)
    val confusionMatrix = ClusterConfusionMatrix(articles.map(_.author), k, predictedClusterIds)
  }

  def withAxle(): Unit = {

    import axle.matrix.JblasMatrixFactory._
    import axle.ml.KMeans
    import axle.ml.DistanceFunction._

    // TODO: use 0.0001
    // TODO: numFeatures should be inferred

    val distance = EuclideanDistanceFunction
    val featureExtractor = (article: FederalistArticle) => fullFeatures(article)
    val labelExtractor = (article: FederalistArticle) => article.author
    val constructor = (features: Seq[Double]) => null.asInstanceOf[FederalistArticle] // TODO

    val classifier = KMeans(articles, numFullFeatures, featureExtractor, constructor, distance, k, numIterations)
    val confusionMatrix = classifier.confusionMatrix(articles, labelExtractor)
  }

}
