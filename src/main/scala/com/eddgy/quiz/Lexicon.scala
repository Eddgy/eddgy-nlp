package com.eddgy.quiz

import java.math.BigDecimal

import axle._
import util.Random.shuffle

object Lexicon {

  val cars = Vector("2013 Mustang GT", "2013 BMW M3", "2013 Ferrari Spider")

  // TODO: metric speeds in Europe
  val carSpeeds = Vector(new BigDecimal("65"), new BigDecimal("75"), new BigDecimal("70"), new BigDecimal("60"))

  val cities = Vector(
    Vector("San Francisco", "Los Angeles", "Monterey"),
    Vector("Boston", "New York", "Washington, DC"),
    Vector("Berlin", "Munich", "Frankfurt"),
    Vector("Paris", "Bordeaux", "Toulouse")
  )

  val carsStream = Stream.continually(shuffle(cars)).flatten

  val cityPairStream = Stream.continually(shuffle(cities(0).permutations(2).toList)).flatten.map(pair => (pair(0), pair(1)))

  // TODO: use metric units when in Europe
  def cityDistance(from: String, to: String): BigDecimal = new BigDecimal("390") // TODO

}