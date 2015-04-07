package db

import models.{Kontest}


/**
 * Created by fred on 07/04/15.
 */
object MemoryProjection {

  var mapKontest: Map[String, Kontest] = ???

  def kontests: List[Kontest] = ???

  def kontestById(id_kontest: String): Option[Kontest] = ???

}
