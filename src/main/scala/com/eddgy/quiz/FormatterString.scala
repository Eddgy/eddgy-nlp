package com.eddgy.quiz

import scala.util.Random.shuffle

object FormatterString extends Formatter[String] {

  def format(answerOption: AnswerOption[_]): String =
    answerOption.value.toString + (if (answerOption.correct) " (CORRECT)" else "")

  def format(question: Question[_]): String = {
    import question._
    text + "\n\n" +
      labelAnswerSeq
      .map({ case (label, answerOption) => label + ". " + format(answerOption) })
      .mkString("\n")
  }

  def format(quiz: Quiz): String = {
    import quiz._
    name + "\n\n" +
      numberQuestionSeq
      .map({ case (n, q) => n + ". " + format(q) })
      .mkString("\n\n")
  }

}