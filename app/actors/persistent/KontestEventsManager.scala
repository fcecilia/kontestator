package actors.persistent

import java.util.Date

import actors.persistent.KontestEventManager._
import akka.actor.{ActorSystem, Props}
import akka.persistence.{RecoveryCompleted, PersistentActor}
import db.MemoryProjection
import models.Kontest
import play.api.Logger
import utils.KontestHelper

/**
 * Created by fred on 06/04/15.
 */
class KontestEventManager extends PersistentActor {
  override def preStart() = ()

  var users: List[String] = List("fred")

  override def persistenceId: String = KontestEventManager.persistenceId

  val receiveRecoverTime = System.currentTimeMillis()

  override def receiveRecover: Receive = {

    case RecoveryCompleted => Logger.error("RecoveryCompleted =====> " + (System.currentTimeMillis() - receiveRecoverTime))

    case KontestAdded(id_user: String, id_kontest: String, title: String, description: String, date: Date) =>
      MemoryProjection.add(Kontest(id_kontest, id_user, title, description))

      Logger.info("TODO => EventManager - receiveRecover AddNewKontest")

    case ModifyKontest(id_user, id_kontest, title, description) =>
      Logger.info("TODO => EventManager - receiveRecover AddNewKontest")

    case _ => Logger.info("TODO => o.O")

  }

  override def receiveCommand: Receive = {

    case AddNewKontest(id_user, title, description) =>

      Logger.info("TODO => EventManager - receiveCommand AddNewKontest")

      sender() ! None

     if (users.contains(id_user)) {
       val id_kontest = KontestHelper.generateKontestID

       val event: KontestAdded = KontestAdded(id_user, id_kontest, title, description, new Date())

       persist(event) { evt =>
         MemoryProjection.add(Kontest(id_kontest, id_user, title, description))
         sender() ! Some(id_kontest)
       }
     } else {
       sender() ! None

     }

    case ModifyKontest(id_user, id_kontest, title, description) =>

      Logger.info("TODO => EventManager - receiveCommand ModifyKontest")

      sender() ! None

    /* val kontestOpt: Option[Kontest] = MemoryProjection.kontestById(id_kontest)
    if (users.contains(id_user) && kontestOpt.nonEmpty) {

      val event: KontestModified = KontestModified(id_user, id_kontest, title, description, new Date())

      persist(event) { evt =>
        Logger.info(evt.toString)
        MemoryProjection.add(Kontest(id_kontest,id_user,title, description))
        sender() ! Some(id_kontest)
      }
    } else {
      sender() ! None

    }*/
  }
}


object KontestEventManager {


  /*STATE*/

  case class State()

  /*COMMANDS*/

  case class AddNewKontest(id_user: String, title: String, description: String)

  case class ModifyKontest(id_user: String, id_kontest: String, title: String, description: String)


  /*EVENTS*/

  case class KontestAdded(id_user: String, id_kontest: String, title: String, description: String, date: Date)

  case class KontestModified(id_user: String, id_kontest: String, title: String, description: String, date: Date)


  val system = ActorSystem("EventManager")

  val persistentActor = system.actorOf(Props[KontestEventManager], "EventManagerActor")

  val persistenceId: String = "EventManagerID"
}
