package com.eddgy.nlp

class ClusterConfusionMatrix(
  goldLabels: IndexedSeq[String],
  predictedOutcomes: IndexedSeq[String],
  counts: IndexedSeq[IndexedSeq[Int]]) {

  lazy val stringRep = {
    val lengthOfRow = counts(0).mkString.length + counts(0).length * 7

    val tableString = counts.zip(goldLabels).map {
      case (goldLine, goldLabel) => (goldLine.mkString("\t") + "\t|\t" + goldLine.sum + "\t[" + goldLabel + "]")
    }.mkString("\n")

    ("-" * 80 + "\n" + "Confusion matrix.\n" + "Columns give predicted counts. Rows give gold counts.\n" +
      "-" * 80 + "\n" + tableString + "\n" + "-" * lengthOfRow + "\n" +
      counts.transpose.map(_.sum).mkString("\t") + "\n" +
      predictedOutcomes.map("[" + _ + "]").mkString("\t") + "\n")
  }

  override def toString = stringRep
}

object ClusterConfusionMatrix {

  def apply(goldClusterIds: IndexedSeq[String], numPredictedClusters: Int, predictedClusterIndices: IndexedSeq[Int]) = {

    val goldLabels = goldClusterIds.toSet.toIndexedSeq
    val goldIndices = goldLabels.zipWithIndex.toMap
    val numGoldClusters = goldIndices.size
    val counts = Array.fill(numGoldClusters, numPredictedClusters)(0)
    goldClusterIds.zip(predictedClusterIndices).map { case (goldId, predIndex) => counts(goldIndices(goldId))(predIndex) += 1 }
    new ClusterConfusionMatrix(goldLabels, (0 until numPredictedClusters).map(_.toString), counts.map(_.toIndexedSeq).toIndexedSeq)
  }

}
