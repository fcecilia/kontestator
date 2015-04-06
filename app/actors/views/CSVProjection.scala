package actors.views

import java.io.FileWriter

import actors.persistent.EventManager
import akka.persistence.PersistentView

/**
 * Created by fred on 06/04/15.
 */
class CSVProjection extends PersistentView {

  override def viewId: String = "CSVProjectionID"

  override def persistenceId: String = EventManager.persistenceId

  val file = new FileWriter(CSVProjection.file_path)

  def add(data: List[String]) = {
    data.foldRight("\n")(_ + "," + _)
    file.flush()
  }

  override def receive: Receive = ???
}

object CSVProjection {

  val file_path = "rapport.csv"
}
