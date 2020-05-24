package job

import jobserver.client.JobServerClient
import jobserver.client.protocol.{JobStagesResponse, RegisterJobInfo}

import scala.util.{Success, Try}

class JobsService(server: String) {

  private val client = JobServerClient.defaultClient(server)

  def registerJob(job: RegisterJobInfo): Unit = {
    client.registerJob(job)
  }

  def jobStages(jobId: String): Option[JobStagesResponse] = {
    Try(client.jobStages(jobId)) match {
      case Success(stage) => Option.apply(stage)
      case _ => Option.empty
    }
  }

}
