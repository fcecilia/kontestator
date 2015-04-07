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


  /*KONTEST*/

  case class FrontAddNewKontest(id_user: String, title: String, description: String)



  def new_kontest() = Action.async(BodyParsers.parse.json) { request =>
    implicit val  format = Json.format[FrontAddNewKontest]

    val cmd = request.body.validate[FrontAddNewKontest]
    cmd.fold(
      errors => {
        Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      front_cmd => {
        val id_kontest = KontestHelper.generateKontestID
        val cmd = AddNewKontest(front_cmd.id_user, id_kontest, front_cmd.title, front_cmd.description)

        (EventManager.persistentActor ? cmd).map { _ =>
          Ok(Json.obj("status" -> "OK", "id" -> id_kontest))
        }
      }
    )
  }

  def kontest_info(id_kontest: String) = play.mvc.Results.TODO

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

  def list_kontests() = play.mvc.Results.TODO




}