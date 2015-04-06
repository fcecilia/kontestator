package actors.persistent

import java.util.Date

import akka.actor.{ActorSystem, Props}
import akka.persistence.PersistentActor

/**
 * Created by fred on 06/04/15.
 */
class EventManager extends PersistentActor {

  override def persistenceId: String = EventManager.persistenceId

  override def receiveRecover: Receive = ???

  override def receiveCommand: Receive = ???

}


object EventManager {


  /*STATE*/

  case class State()

  /*COMMANDS*/

  case class AddNewUser(id_user: String, name: String, mail: String)

  case class ModifyUser(id_user: String, name: String, mail: String)

  case class AddNewKontest(id_user: String, id_kontest: String, name: String, mail: String)

  case class ModifyKontest(id_user: String, id_kontest: String, name: String, mail: String)

  case class AddParticipant(id_kontest: String, id_user: String)

  case class RemoveParticipant(id_kontest: String, id_user: String)


  /*EVENTS*/

  case class NewUserAdded(id: String, name: String, mail: String, date: Date)

  case class UserModified(id: String, name: String, mail: String, date: Date)


  case class KontestAdded(id: String, name: String, mail: String, date: Date)

  case class KontestModified(id: String, name: String, mail: String, date: Date)

  case class ParticipantAdded(id_kontest: String, id_user: String, date: Date)

  case class ParticipantRemove(id_kontest: String, id_user: String, date: Date)


  lazy val system = ActorSystem("actors.persistent.EventManager")

  lazy val persistentActor = system.actorOf(Props[EventManager], "EventManagerActor")

  val persistenceId: String = "EventManagerID"
}
