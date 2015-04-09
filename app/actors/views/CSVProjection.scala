package actors.views

import java.io.FileWriter

import actors.persistent.{KontestEventManager}
import actors.persistent.KontestEventManager.{KontestAdded, AddNewKontest}
import akka.actor.Props
import akka.persistence.PersistentView
import play.api.Logger

/**
 * Created by fred on 06/04/15.
 */
class CSVProjection extends PersistentView {

  override def viewId: String = "CSVProjectionID"

  override def persistenceId: String = KontestEventManager.persistenceId

  val file = new FileWriter(CSVProjection.file_path)

  def line(data: List[String]) = {
    val line: String = data.foldRight("\n")(_ + ";" + _)
    file.write(line)
    file.flush()
  }

  override def receive: Receive = {

    case KontestAdded(id_user, id_kontest, title, description, date) =>


      //TODO => CSVProjection

  }
}

object CSVProjection {

  val file_path = "rapport.csv"

  val actor = KontestEventManager.system.actorOf(Props[CSVProjection], "CSVProjection")

}
