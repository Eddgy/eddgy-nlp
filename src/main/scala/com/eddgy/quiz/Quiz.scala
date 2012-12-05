package com.eddgy.quiz

import scala.util.Random.nextInt

case class Quiz(name: String, factories: Seq[() => Question[_]], numQuestions: Int) {

  def questionStream(): Stream[Question[_]] =
    Stream.cons(factories(nextInt(factories.size))(), questionStream)

  lazy val questions = questionStream.take(numQuestions)

  def numberQuestionSeq() = (1 to questions.size).zip(questions)
  
}
