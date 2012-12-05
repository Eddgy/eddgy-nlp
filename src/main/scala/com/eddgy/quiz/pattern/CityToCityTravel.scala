package com.eddgy.quiz.pattern

import util.Random.{ shuffle, nextInt }
import java.math.BigDecimal
import org.joda.time.DateTime

import com.eddgy.quiz._
import Util._
import Lexicon._

object CityToCityTravelFactory {

  def random(): CityToCityTravel = {
    val car = carsStream.take(1)(0) // TODO better way of saying take(1)(0)?
    val (fromCity, toCity) = cityPairStream.take(1)(0)
    val time = new DateTime() // TODO round and format
    val carSpeed = carSpeeds(nextInt(carSpeeds.length))
    CityToCityTravel(car, fromCity, toCity, cityDistance(fromCity, toCity), time, carSpeed)
  }

}

case class CityToCityTravel(car: String, fromCity: String, toCity: String,
  miles: BigDecimal, startTime: DateTime, mph: BigDecimal) extends Question[DateTime] {

  // TODO: mix up the structure of the text
  lazy val _text =
    "A " + car + " travels from " + fromCity + " to " + toCity + " at a rate of " + mph + " miles per hour without stopping.  " +
      "The total distance is " + miles + " miles.  " +
      "If it left at " + startTime + ", what time does it arrive in " + toCity + "?"

  lazy val _answer = AnswerOption(startTime.plusMinutes((bdDivide(miles, mph).doubleValue * 60).toInt), true)

  // TODO mislead better
  lazy val _wrongAnswers = shuffle(
    Vector(
      _answer.value.plusHours(1),
      _answer.value.plusMinutes(10)
    ).map(v => AnswerOption(v, false)))

  lazy val shuffledOptions = shuffle(_wrongAnswers ++ Vector(_answer))

  def text() = _text

  def answerOptions() = shuffledOptions

}
