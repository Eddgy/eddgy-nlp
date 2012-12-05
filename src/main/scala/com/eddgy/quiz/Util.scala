package com.eddgy.quiz

import math.{ max, abs }
import java.math.BigDecimal

object Util {

  // TODO precision:

  def bdDivide(numerator: BigDecimal, denominator: BigDecimal) = numerator.divide(
    denominator,
    max(max(numerator.precision, abs(numerator.scale)),
      max(denominator.precision, abs(denominator.scale))),
    java.math.RoundingMode.HALF_UP)

}