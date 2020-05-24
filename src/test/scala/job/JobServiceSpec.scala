package job

import java.util

import jobserver.client.protocol._
import org.scalacheck.Gen.{const, identifier, oneOf}
import org.scalacheck.commands.Commands
import org.scalacheck.{Gen, Prop}

import scala.collection.JavaConverters._
import scala.util.{Success, Try}

/*
  Testing Job Server - https://github.com/ashkrit/jobserver

 */

object JobServiceSpec {

  object JobServiceCommands extends Commands {

    type Sut = JobsService

    case class State(jobs: Map[String, RegisterJobInfo], stages: Map[String, List[JobStageInfo]])

    def newSut(state: State): Sut = new JobsService("http://localhost:8080/")

    def genInitialState: Gen[State] = State(Map.empty[String, RegisterJobInfo], Map.empty[String, List[JobStageInfo]])

    def genCommand(state: State): Gen[Command] = {
      oneOf(generateRegisterJobs, genQueryExisting(state))
    }

    val generateRegisterJobs: Gen[RegisterJobCommand] = for {
      job <- generateNewJob
    } yield RegisterJobCommand(job)

    val genQueryCommands: Gen[QueryJobStageCommand] = jobIdKey.map(id => QueryJobStageCommand(id.toString))

    def genQueryExisting(state: State): Gen[QueryJobStageCommand] =
      if (state.jobs.isEmpty) genQueryCommands
      else oneOf(state.jobs.keys.toSeq).map(QueryJobStageCommand)


    case class RegisterJobCommand(job: RegisterJobInfo) extends Command {
      type Result = Boolean

      def run(counterSystem: Sut): Result = {
        counterSystem.registerJob(job)
        true
      }

      def nextState(s: State): State = s.copy(
        jobs = s.jobs + (job.getJobId -> job)
      )

      def preCondition(s: State) = true

      def postCondition(oldState: State, result: Try[Result]): Prop = {
        result.getOrElse(false) == !oldState.jobs.contains(job.getJobId)
      }
    }

    case class QueryJobStageCommand(jobId: String) extends Command {
      type Result = Option[JobStagesResponse]

      def run(counterSystem: Sut): Result = {
        counterSystem.jobStages(jobId)
      }

      def nextState(s: State): State = s

      def preCondition(s: State) = true

      def postCondition(oldState: State, result: Try[Result]): Prop = {
        result.map(_.isDefined) == Success(oldState.jobs.contains(jobId))
      }
    }


    override def destroySut(sut: Sut): Unit = {
    }

    override def canCreateNewSut(newState: State, initSuts: Traversable[State],
                                 runningSuts: Traversable[JobsService]): Boolean = {
      initSuts.isEmpty && runningSuts.isEmpty
    }

    override def initialPreCondition(state: State): Boolean = true

  }

  val jobIdKey = Gen.uuid
  val jobName = identifier
  val jobCategory = oneOf(List("FxRate", "InterestRate"))

  val generateNewJob: Gen[RegisterJobInfo] = for {
    jobId <- jobIdKey
    jobName <- jobName
    category <- jobCategory
  } yield createNewJobInfo(jobId.toString, jobName, category)


  private def createNewJobInfo(jobId: String, jobName: String, category: String): RegisterJobInfo = {
    val job = new RegisterJobInfo()
    job.setCategory(category)
    job.setJobId(jobId)
    job.setJobName(jobName)
    job.setSubscriptions(List(newSubscription).asJava)
    job
  }

  private def newSubscription(): Subscription = {
    val subscription = new Subscription()
    subscription.setType(SubscriptionType.REST)
    subscription.setProperties(new util.HashMap[String, String]())
    subscription.getProperties.put("endpoint", "http://localhost:8080/jobserver/callback/stage")
    subscription
  }


  def main(args: Array[String]): Unit = {
    val jobServer = JobServiceCommands.property()
    jobServer.check()
  }

}
