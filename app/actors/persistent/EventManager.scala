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

  case class AddNewKontest(id_user: String, id_kontest: String, title: String, description: String)

  case class ModifyKontest(id_user: String, id_kontest: String, title: String, description: String)



  /*EVENTS*/

  case class KontestAdded(id_user: String, id_kontest: String, title: String, description: String, date: Date)

  case class KontestModified(id: String,  id_kontest: String, title: String, description: String, date: Date)




  lazy val system = ActorSystem("EventManager")

  lazy val persistentActor = system.actorOf(Props[EventManager], "EventManagerActor")

  val persistenceId: String = "EventManagerID"
}
