import java.io.File
import java.util.Date

import actors.persistent.{KontestEventManager}
import actors.views.{CSVProjection, MongoProjection}
import akka.persistence.Recover
import play.api.{GlobalSettings, Logger}

import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by fred on 08/10/2014.
 */
object Global extends GlobalSettings {


  override def onStart(app: play.api.Application): Unit = {
    super.onStart(app)


    //REMOVE DB
    MongoProjection.db.drop.map { _ =>
      MongoProjection.actor ! Recover()
    }
    import akka.util.Timeout

    import scala.concurrent.duration._

    implicit val timeout = Timeout(50 seconds)


    //Recover
    Logger.info("Recover ===========> " + new Date())
    KontestEventManager.persistentActor ! Recover()


    //REMOVE FILE
    val file = new File(CSVProjection.file_path)
    file.delete()
    CSVProjection.actor ! Recover()


    Logger.info("<--------------------------START-------------------------->")
  }

  override def onStop(app: play.api.Application): Unit = {
    KontestEventManager.system.stop(KontestEventManager.persistentActor)
    KontestEventManager.system.shutdown()
    super.onStop(app)
  }

}
