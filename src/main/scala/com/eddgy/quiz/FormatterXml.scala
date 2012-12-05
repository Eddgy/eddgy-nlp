package com.eddgy.quiz

import xml._

object FormatterXml extends Formatter[Node] {

  def format(answerOption: AnswerOption[_]): Node =
    <answerOption>
      <value>{ answerOption.value.toString }</value>
      <correct>{ answerOption.correct.toString }</correct>
    </answerOption>

  def format(question: Question[_]): Node = {

    import question._

    <question>
      <text>{ text }</text>
      <options>
        {
          labelAnswerSeq
            .map({
              case (label, answerOption) =>
                <option>
                  <label>{ label }</label>
                  <foo>{ format(answerOption) }</foo>
                </option>
            }
            )
        }
      </options>
    </question>
  }

  def format(quiz: Quiz): Node = {
    import quiz._
    <quiz>
      <name>{ name }</name>
      <questions>
        {
          numberQuestionSeq
            .map({
              case (n, q) =>
                <numberedQuestion><number>{ n }</number>{ format(q) }</numberedQuestion>
            }
            )
        }
      </questions>
    </quiz>
  }

}