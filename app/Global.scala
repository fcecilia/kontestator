import java.io.File
import java.util.Date

import actors.persistent.EventManager
import actors.views.{MongoProjection, CSVProjection}
import akka.persistence.{Update, Recover}
import akka.util.Timeout
import play.api.{Logger, GlobalSettings}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * Created by fred on 08/10/2014.
 */
object Global extends GlobalSettings {


  override def onStart(app: play.api.Application): Unit = {
    super.onStart(app)


    //REMOVE DB
    MongoProjection.db.drop.map{ _ =>
      MongoProjection.actor ! Recover()
    }
    import akka.pattern.ask
    import akka.util.Timeout
    import scala.concurrent.duration._

    implicit val timeout = Timeout(50 seconds)


    //Recover
    Logger.info("Recover ===========> "+ new Date())
    EventManager.persistentActor ! Recover()



    //REMOVE FILE
    val file = new File(CSVProjection.file_path)
    file.delete()
    CSVProjection.actor ! Recover()


    Logger.info("<--------------------------START-------------------------->")
  }

  override def onStop(app: play.api.Application): Unit = {
    EventManager.system.stop(EventManager.persistentActor)
    EventManager.system.shutdown()
    super.onStop(app)
  }

}
