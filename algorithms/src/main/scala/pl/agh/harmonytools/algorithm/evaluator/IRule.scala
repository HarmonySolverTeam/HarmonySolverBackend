package pl.agh.harmonytools.algorithm.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent
import pl.agh.harmonytools.error.{RequirementChecker, UnexpectedInternalError}

trait IRule[T <: NodeContent] {
  def evaluate(connection: Connection[T]): Double

  def isBroken(connection: Connection[T]): Boolean = evaluate(connection) != 0.0

  def isNotBroken(connection: Connection[T]): Boolean = !isBroken(connection)

  def caption: String = this.getClass.getSimpleName
}

trait SoftRule[T <: NodeContent] extends IRule[T]

trait HardRule[T <: NodeContent] extends IRule[T]

abstract class AnyRule[T <: NodeContent](evaluationRatio: Double) extends HardRule[T] with SoftRule[T] {
  RequirementChecker.isRequired(evaluationRatio <= 1.0 && evaluationRatio >= 0.0, UnexpectedInternalError("Evaluation ratio not in [0,1]"))
}

trait SubRule[T <: NodeContent] extends IRule[T] {
  override def caption: String = "sub-rule"
}
