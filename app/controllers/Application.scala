package controllers

import actors.persistent.EventManager
import actors.persistent.EventManager._
import akka.util.Timeout
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import utils.KontestHelper
import akka.pattern.ask
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  implicit val timeout = Timeout(50 seconds)

  def index = Action {
    Ok(views.html.index("Kontestator"))
  }

  case class FrontAddNewUser(name: String, mail: String)


  /*USER*/
  def user_info(id_user: String) = play.mvc.Results.TODO

  def new_user() = Action.async(BodyParsers.parse.json) { request =>
    implicit val  format = Json.format[FrontAddNewUser]
    val cmd = request.body.validate[FrontAddNewUser]
    cmd.fold(
      errors => {
        Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      front_cmd => {
        val id_user = KontestHelper.generateUserID
        val cmd = AddNewUser(id_user, front_cmd.name, front_cmd.mail)

        (EventManager.persistentActor ? cmd).map { _ =>
          Ok(Json.obj("status" -> "OK", "id" -> id_user))
        }
      }
    )
  }

  def modify_user(id_user: String) = Action.async(BodyParsers.parse.json) { request =>
    implicit val  format = Json.format[ModifyUser]
    val cmd = request.body.validate[ModifyUser]
    cmd.fold(
      errors => {
        Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      cmd => {
        (EventManager.persistentActor ? cmd).map { _ =>
          Ok(Json.obj("status" -> "OK", "id" -> id_user))
        }
      }
    )
  }


  /*KONTEST*/

  case class FrontAddNewKontest(id_user: String, name: String, mail: String)

  def list_kontests() = play.mvc.Results.TODO

  def kontest_info(id_kontest: String) = play.mvc.Results.TODO

  def new_kontest() = Action.async(BodyParsers.parse.json) { request =>
    implicit val  format = Json.format[FrontAddNewKontest]

    val cmd = request.body.validate[FrontAddNewKontest]
    cmd.fold(
      errors => {
        Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      front_cmd => {
        val id_kontest = KontestHelper.generateKontestID
        val cmd = AddNewKontest(front_cmd.id_user, id_kontest, front_cmd.name, front_cmd.mail)

        (EventManager.persistentActor ? cmd).map { _ =>
          Ok(Json.obj("status" -> "OK", "id" -> id_kontest))
        }
      }
    )
  }

  def modify_kontest(id_kontest: String) = Action.async(BodyParsers.parse.json) { request =>
    implicit val  format = Json.format[ModifyKontest]

    val cmd = request.body.validate[ModifyKontest]
    cmd.fold(
      errors => {
        Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      cmd => {
        (EventManager.persistentActor ? cmd).map { _ =>
          Ok(Json.obj("status" -> "OK", "id" -> cmd.id_kontest))
        }
      }
    )
  }


  /*PARTICIPANT*/


  def list_participants(id_kontest: String) = play.mvc.Results.TODO

  def add_new_participant(id_kontest: String, id_user: String) = Action.async { request =>
    val cmd = AddParticipant(id_kontest, id_user)
    (EventManager.persistentActor ? cmd).map { _ =>
      Ok(Json.obj("status" -> "OK", "id_user" -> cmd.id_user, "id_kontest" -> cmd.id_kontest))
    }
  }


  def remove_participants(id_kontest: String, id_user: String) = Action.async { request =>
    val cmd = RemoveParticipant(id_kontest, id_user)
    (EventManager.persistentActor ? cmd).map { _ =>
      Ok(Json.obj("status" -> "OK", "id_user" -> cmd.id_user, "id_kontest" -> cmd.id_kontest))
    }
  }

}