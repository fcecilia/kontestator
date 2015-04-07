import java.io.File

import actors.views.{MongoProjection, CSVProjection}
import play.api.GlobalSettings
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by fred on 08/10/2014.
 */
object Global extends GlobalSettings {


  override def onStart(app: play.api.Application): Unit = {
    super.onStart(app)

    //REMOVE FILE
    val file = new File(CSVProjection.file_path)
    file.delete()


    //REMOVE DB
    //MongoProjection.db.drop()
  }

  override def onStop(app: play.api.Application): Unit = {
    super.onStop(app)

  }

}
