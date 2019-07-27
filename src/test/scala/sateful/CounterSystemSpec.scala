package sateful

import org.scalacheck.{Gen, Prop}
import org.scalacheck.commands._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen.oneOf
import state.CounterSystem

import scala.util.{Success, Try}

object CounterSystemSpec {


  object CounterCommands extends Commands {

    type Sut = CounterSystem
    type State = Long

    def newSut(state: State) = new CounterSystem(state.toInt)

    def genInitialState: Gen[State] = arbitrary[Int].map(_.toLong)

    def genCommand(state: State): Gen[Command] = oneOf(Increment, Get)

    case object Increment extends Command {
      type Result = Int

      def run(counterSystem: Sut) = counterSystem.increment()

      def nextState(s: State) = if (s < Int.MaxValue) s + 1 else s

      def preCondition(s: State) = s < Int.MaxValue

      def postCondition(s: State, result: Try[Int]) =
        if (s >= Int.MaxValue) result.isFailure
        else
          result == Success(s + 1)
    }

    case object Get extends Command {
      type Result = Int

      def run(counterSystem: Sut) = counterSystem.get

      def nextState(s: State) = s

      def preCondition(s: State) = true

      def postCondition(s: State, result: Try[Int]) =
        result == Success(s)


    }

    override def canCreateNewSut(newState: CounterCommands.State, initSuts: Traversable[CounterCommands.State],
                                 runningSuts: Traversable[CounterCommands.Sut]): Boolean = true


    override def destroySut(sut: CounterCommands.Sut): Unit = {}

    override def initialPreCondition(state: CounterCommands.State): Boolean = true

  }

  def main(args: Array[String]): Unit = {

    val counterProp = CounterCommands.property()
    counterProp.check()
  }


}



