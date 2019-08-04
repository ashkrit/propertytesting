package cache

import cache.impl.NaiveKeyValueSystem
import org.scalacheck.Gen.{const, frequency, identifier, nonEmptyListOf, oneOf, someOf}
import org.scalacheck.commands.Commands
import org.scalacheck.{Gen, Prop}

import scala.util.{Success, Try}

object KeyValueSystemSpec {

  object KeyValueSystemCommands extends Commands {

    type Sut = KeyValueSystem[String, String]

    case class State(entries: scala.collection.immutable.Map[String, String])

    def newSut(state: State): Sut = new NaiveKeyValueSystem[String, String]()

    def genInitialState: Gen[State] = State(scala.collection.immutable.Map.empty[String, String])

    def genCommand(state: State): Gen[Command] = {
      frequency(
        (10, generateNewEntries),
        (2, genGetExisting(state)),
        (6, genGetCommands),
        (10, genDelExisting(state)),
        (1, const(CheckSize)),
        (10, generateNewEntries),
        (6, genGetCommands)
      )
    }

    val genKey = identifier
    val genVal = identifier

    val generateNewEntries: Gen[PutValue] = for {
      key <- genKey
      value <- genVal
    } yield PutValue(key, value)

    val genGetCommands: Gen[GetValue] = genKey.map(GetValue)
    val genDelCommands: Gen[DeleteValue] = nonEmptyListOf(genKey).map(DeleteValue)

    def genGetExisting(state: State): Gen[GetValue] =
      if (state.entries.isEmpty) genGetCommands
      else oneOf(state.entries.keys.toSeq).map(GetValue)

    def genDelExisting(state: State): Gen[DeleteValue] =
      if (state.entries.isEmpty) genDelCommands
      else someOf(state.entries.keys.toSeq).map(DeleteValue)

    case class PutValue(key: String, value: String) extends Command {
      type Result = Boolean

      def run(counterSystem: Sut): Result = counterSystem.put(key, value)

      def nextState(s: State): State = s.copy(
        entries = s.entries + (key -> value)
      )

      def preCondition(s: State) = true

      def postCondition(s: State, result: Try[Result]): Prop = {
        result == Success(true)
      }
    }

    case class GetValue(key: String) extends Command {
      type Result = Option[String]

      def run(counterSystem: Sut): Result = counterSystem.get(key)

      def nextState(s: State): State = s

      def preCondition(s: State): Boolean = true

      def postCondition(s: State, result: Try[Option[String]]): Prop = {
        result == Success(s.entries.get(key))
      }

    }

    case class DeleteValue(keysToDelete: Seq[String]) extends Command {
      type Result = Int

      def run(counterSystem: Sut): Result = {
        keysToDelete
          .map(counterSystem.remove)
          .filter(result => result)
          .size
      }

      def nextState(s: State): State = {
        s.copy(entries = s.entries -- keysToDelete)
      }

      def preCondition(s: State): Boolean = true

      def postCondition(previousState: State, result: Try[Int]): Prop = {
        val size = previousState.entries.filter(entry => keysToDelete.contains(entry._1)).size
        result == Success(size)
      }
    }

    case object CheckSize extends Command {
      type Result = Int

      def run(counterSystem: Sut): Result = {
        counterSystem.size()
      }

      def nextState(s: State): State = s

      def preCondition(s: State): Boolean = true

      override def postCondition(state: State, result: Try[Int]): Prop = {
        result == Success(state.entries.size)
      }

    }

    override def destroySut(sut: Sut): Unit = {
    }

    override def canCreateNewSut(newState: State, initSuts: Traversable[State], runningSuts: Traversable[KeyValueSystem[String, String]]): Boolean = {
      initSuts.isEmpty && runningSuts.isEmpty
    }

    override def initialPreCondition(state: State): Boolean = true

  }

  def main(args: Array[String]): Unit = {
    val keyValueSystemProperties = KeyValueSystemCommands.property()
    keyValueSystemProperties.check()
  }

}
