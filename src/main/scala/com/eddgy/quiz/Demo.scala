package com.eddgy.quiz

import scala.util.Random.nextInt

object Demo {

  def main(args: Array[String]): Unit = {

    val f = pattern.CityToCityTravelFactory.random _
    val quiz = Quiz("Some Quiz", Vector(f), 10)
    
    println(FormatterString.format(quiz))
    println()
    println(FormatterXml.format(quiz))
  }

}