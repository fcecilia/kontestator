package controllers

import java.util.Date

import actors.persistent.EventManager
import actors.persistent.EventManager._
import akka.pattern.ask
import akka.util.Timeout
import play.api.Logger
import scala.collection.immutable.IndexedSeq
import scala.concurrent.duration._
import db.MemoryProjection
import models.Kontest
import play.api.libs.json.{Format, JsError, Json}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends Controller {

  implicit val timeout = Timeout(50 seconds)

  def index = Action {
    Ok(views.html.index("Kontestator"))
  }


  /*KONTEST*/


  def new_kontest() = Action.async(BodyParsers.parse.json) { request =>
    implicit val format = Json.format[AddNewKontest]

    val cmd = request.body.validate[AddNewKontest]
    cmd.fold(
      errors => {
        Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      front_cmd => {
        val cmd = AddNewKontest(front_cmd.id_user, front_cmd.title, front_cmd.description)

        (EventManager.persistentActor ? cmd).mapTo[Option[String]].map {
          case Some(id_kontest) =>

            Ok(Json.obj("status" -> "OK", "id" -> id_kontest))
          case None =>
            BadRequest(Json.obj("status" -> "KO", "message" -> "BAD USER"))

        }
      }
    )
  }

  case class ModifyKontestFront(id_user: String, title: String, description: String)

  def modify_kontest(id_kontest: String) = Action.async(BodyParsers.parse.json) { request =>
    implicit val format = Json.format[ModifyKontestFront]

    val cmd = request.body.validate[ModifyKontestFront]
    cmd.fold(
      errors => {
        Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      cmd => {
        (EventManager.persistentActor ? ModifyKontest(cmd.id_user, id_kontest, cmd.title, cmd.description)).map { _ =>
          Ok(Json.obj("status" -> "OK", "id" -> id_kontest))
        }
      }
    )
  }

  def list_kontests() = Action {

    val kontests: List[Kontest] = MemoryProjection.kontests
    implicit val format: Format[Kontest] = Json.format[Kontest]
    Ok(Json.obj("size" -> kontests.size,  "kontests" -> kontests))

  }

  def kontest_info(id_kontest: String) = Action {

    val kontestOpt: Option[Kontest] = MemoryProjection.kontestById(id_kontest)
    implicit val format: Format[Kontest] = Json.format[Kontest]

    kontestOpt match {
      case Some(kontest) => Ok(Json.obj("kontest" -> kontest))
      case None => BadRequest(Json.obj("result" -> "BAD ID"))

    }

  }


  def run(nbr: Int) = Action.async{
    val begin = System.currentTimeMillis()

    if (nbr > 0) {

      val responces: IndexedSeq[Future[Option[String]]] = (1 to nbr).map {
        n =>

          val description: Date = new Date()
          val cmd = AddNewKontest("fred", "title", description.toString)

          (EventManager.persistentActor ? cmd).mapTo[Option[String]]
      }
      Future.sequence(responces).map { _ =>
        val time =System.currentTimeMillis() - begin
        Logger.info("RUN ================================> "+time)
        Ok(Json.obj("status" -> "OK", "time" -> time))
      }

    } else {
      Future(BadRequest(Json.obj("status" -> "KO", "message" -> "Seriously ?!")))

    }
  }

}