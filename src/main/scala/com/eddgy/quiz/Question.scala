package com.eddgy.quiz

case class AnswerOption[T](value: T, correct: Boolean)

abstract class Question[T]() {

  def text(): String

  // TODO assert the # of answerOptions < 27

  def answerOptions(): Seq[AnswerOption[T]]
  
  def labelAnswerSeq() = ('A' to 'Z').zip(answerOptions())
}
