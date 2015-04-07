import java.io.File

import actors.persistent.EventManager
import actors.views.{MongoProjection, CSVProjection}
import akka.persistence.{Update, Recover}
import play.api.{Logger, GlobalSettings}
import scala.concurrent.ExecutionContext.Implicits.global


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

    //Recover
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
