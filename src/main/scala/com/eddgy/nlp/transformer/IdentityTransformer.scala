package com.eddgy.nlp.transformer

import com.eddgy.nlp._

class IdentityTransformer extends PointTransformer {
  def apply(points: IndexedSeq[Point]) = points
}
