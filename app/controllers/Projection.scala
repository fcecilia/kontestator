package controllers

import java.io.FileWriter

import actors.views.CSVProjection
import play.api.mvc.{Action, Controller}

/**
 * Created by fred on 07/04/15.
 */
object Projection extends Controller {
  val file = new FileWriter(CSVProjection.file_path)

  def csv() = Action {



    Ok.sendFile(new java.io.File(CSVProjection.file_path))
  }
}
