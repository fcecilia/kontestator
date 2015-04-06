package actors.views

import java.util.Date

import actors.persistent.EventManager
import actors.persistent.EventManager._
import akka.persistence.PersistentView
import play.api.Play
import play.api.Play.current
import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONDateTime, BSONHandler, Macros}
import reactivemongo.core.nodeset.Authenticate
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by fred on 06/04/15.
 */
class MongoProjection extends PersistentView {

  override def viewId: String = "MongoProjectionID"

  override def persistenceId: String = EventManager.persistenceId

  override def receive: Receive = ???
}

object MongoProjection {

  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, Date] {
    def read(time: BSONDateTime) = new Date(time.value)

    def write(jdtime: Date) = BSONDateTime(jdtime.getTime)
  }

  implicit val formatNewUserAdded = Macros.handler[NewUserAdded]
  implicit val formatUserModified = Macros.handler[UserModified]
  implicit val formatKontestAdded = Macros.handler[KontestAdded]
  implicit val formatKontestModified = Macros.handler[KontestModified]
  implicit val formatParticipantAdded = Macros.handler[ParticipantAdded]
  implicit val formatParticipantRemove = Macros.handler[ParticipantRemove]





  lazy val db = {
    val host = Play.configuration.getString("mongo.url").getOrElse("")
    val port = Play.configuration.getString("mongo.port").getOrElse("")
    val user = Play.configuration.getString("mongo.user").getOrElse("")
    val pwd = Play.configuration.getString("mongo.pwd").getOrElse("")
    val dbName = Play.configuration.getString("mongo.db").getOrElse("")
    val credentials = List(Authenticate(dbName, user, pwd))
    val driver = new MongoDriver
    val servers: Seq[String] = host.split(",").toSeq.map(s => s+":"+port)
    val connection = driver.connection(servers,authentications = credentials)
    connection(dbName)
  }

  def collection(collectionName: String) = {
    val collection: BSONCollection = db(collectionName)
    collection
  }


}
