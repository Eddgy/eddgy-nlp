package com.eddgy.nlp.data

import com.eddgy.nlp._
import org.apache.log4j.Logger
import org.apache.log4j.Level
import scalaz._
import Scalaz._

object SimpleTokenizer {
  def apply(text: String): IndexedSeq[String] = text
    .replaceAll("""([\?!()\";\|\[\].,'])""", " $1 ")
    .trim
    .split("\\s+")
    .toIndexedSeq
}

object FederalistPapers {

  val filename = "data/federalist.txt"

  lazy val articles = extractArticles()

  // TODO: date
  // TODO: venue (For, From, etc)
  case class FederalistArticle(id: String, header: String, author: String, addressee: String, text: String)

  val BookRE = """FEDERALIST\.? No. \d+\s+""".r
  val ArticleRE = """(?s)(.*)\n+([A-Z ]+)(\n){3,}To ([\w ]+)[\:\.][\s\n]+(.*)""".r

  def extractArticles(): IndexedSeq[FederalistArticle] =
    (BookRE.split(io.Source.fromFile(filename).mkString("")).tail)
      .zipWithIndex.map({
        case (article, i) => {
          val id = (i + 1).toString
          try {
            val ArticleRE(header, author, _, addressee, text) = article
            Some(FederalistArticle(id, header, author, addressee, text.replaceAll("\n", " ").toLowerCase))
          } catch {
            case e: Exception => {
              println("Exception during article " + id + ": " + e)
              None
            }
          }
        }
      })
      .flatMap(x => x)
      .filter(article => article.id != "71")

  def simpleFeatures(article: FederalistArticle) = {
    val tokens = SimpleTokenizer(article.text)
    val theCount = tokens.filter(_ == "the").length
    val peopleCount = tokens.filter(_ == "people").length
    val whichCount = tokens.filter(_ == "which").length
    Vector(theCount, peopleCount, whichCount)
  }

  def countem[K](xs: Seq[K]): Map[K, Int] = xs
    .map((_, 1))
    .groupBy(_._1)
    .map({ case (k, vs) => (k, vs.map(_._2).sum) })
    .withDefaultValue(0)

  val wordCutoff = 5

  val topWordCounts = articles
    .map(article => countem(SimpleTokenizer(article.text)))
    .reduce(_ |+| _)
    .toList.sortBy(_._2).reverse
    .filter(_._2 > wordCutoff)
  // .take(200)

  val topWords = topWordCounts.map(_._1)

  //    println("There are " + topWords.length + " unique words used more than " + wordCutoff + " time(s).")
  //    println(topWordCounts.mkString(", "))
  //    println

  val topBigramCounts = articles
    .map(article => {
      val tokens = SimpleTokenizer(article.text)
      countem(tokens.zip(tokens.tail))
    })
    .reduce(_ |+| _)
    .toList.sortBy(_._2).reverse
    .filter(_._2 > 1)
    .take(200)

  val topBigrams = topBigramCounts.map(_._1)
  //    println(topBigramCounts.mkString(", "))

  // TODO:
  // * relative frequencies vs counts
  // * incorporate document frequencies (tf-idf)
  // * type/token ratio
  // * capitalization patterns
  // * average sentence length

  def fullFeatures(article: FederalistArticle) = {
    val tokens = SimpleTokenizer(article.text)
    val wordCounts = countem(tokens)
    val bigrams = tokens.zip(tokens.tail)
    val bigramCounts = countem(bigrams)
    val averageWordLength = tokens.map(_.length).sum / tokens.length.toDouble
    topWords.map(wordCounts(_).toDouble).toIndexedSeq ++ Vector(averageWordLength)
  }

  lazy val numFullFeatures = topWords.length + 1

}
