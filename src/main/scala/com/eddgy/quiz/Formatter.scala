package com.eddgy.quiz

trait Formatter[T] {

  def format(answerOption: AnswerOption[_]): T
  
  def format(question: Question[_]): T

  def format(quiz: Quiz): T

}
