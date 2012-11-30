package com.eddgy.quiz

import java.math.BigDecimal
import util.Random.{ shuffle, nextInt }
import org.joda.time.DateTime
import math.{ max, abs }
import axle._

case class Question[T](text: String, answer: T, wrongAnswers: Seq[T]) {

  override def toString(): String = {
    val shuffledAnswers = shuffle(wrongAnswers.map(_.toString) ++ Vector(answer + " (CORRECT)"))
    val labelledAnswers = ('A' to 'Z').zip(shuffledAnswers)
    text + "\n\n" + labelledAnswers.map(x => x._1 + ". " + x._2).mkString("\n")
  }
}

case class Quiz(name: String, questions: Seq[Question[_]]) {

  override def toString(): String = {
    name + "\n\n" + questions.zipWithIndex.map({ case (q, i) => i + ". " + q }).mkString("\n\n")
  }

}

object Demo {

  val cars = Vector("2013 Mustang GT", "2013 BMW M3", "2013 Ferrari Spider")

  // TODO: metric speeds in Europe
  val speeds = Vector(new BigDecimal("65"), new BigDecimal("75"), new BigDecimal("70"), new BigDecimal("60"))

  val cities = Vector(
    Vector("San Francisco", "Los Angeles", "Monterey"),
    Vector("Boston", "New York", "Washington, DC"),
    Vector("Berlin", "Munich", "Frankfurt"),
    Vector("Paris", "Bordeaux", "Toulouse")
  )

  // TODO: use metric units when in Europe
  def cityDistance(from: String, to: String): BigDecimal = new BigDecimal("390") // TODO

  def city2city1(car: String, fromCity: String, toCity: String,
    miles: BigDecimal, startTime: DateTime, mph: BigDecimal): Question[DateTime] = {

    val answer = startTime.plusMinutes((bdDivide(miles, mph).doubleValue * 60).toInt)
    val wrongAnswers = shuffle(Vector(answer.plusHours(1), answer.plusMinutes(10))) // TODO mislead better

    // TODO: mix up the structure of the text
    Question(
      "A " + car + " travels from " + fromCity + " to " + toCity + " at a rate of " + mph + " miles per hour without stopping.  " +
        "The total distance is " + miles + " miles.  " +
        "If it left at " + startTime + ", what time does it arrive in " + toCity + "?",
      answer,
      wrongAnswers)
  }

  def makeQuiz(numQuestions: Int): Quiz = {

    val carsStream = Stream.continually(shuffle(cars)).flatten
    val cityPairStream = Stream.continually(shuffle(cities(0).permutations(2).toList)).flatten

    val questions = for {
      ((i, car), cityPair) <- ((1 to numQuestions).zip(carsStream)).zip(cityPairStream)
    } yield {
      val fromCity = cityPair(0)
      val toCity = cityPair(1)
      val time = new DateTime() // TODO round and format
      val speed = speeds(nextInt(speeds.length))
      city2city1(car, fromCity, toCity, cityDistance(fromCity, toCity), time, speed)
    }
    Quiz("Some Quiz", questions)
  }

  def main(args: Array[String]): Unit = {

    val quiz = makeQuiz(10)
    println(quiz)
  }

  def bdDivide(numerator: BigDecimal, denominator: BigDecimal) = numerator.divide(
    denominator,
    max(max(numerator.precision, abs(numerator.scale)),
      max(denominator.precision, abs(denominator.scale))),
    java.math.RoundingMode.HALF_UP)

}