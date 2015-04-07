package db

import models.{Kontest}


/**
 * Created by fred on 07/04/15.
 */
object MemoryProjection {

  private var mapKontest: Map[String, Kontest] = Map.empty[String, Kontest]

  def kontests: List[Kontest] = mapKontest.map(_._2).toList

  def kontestById(id_kontest: String): Option[Kontest] = mapKontest.get(id_kontest)

  def add(kontest: Kontest): Unit = {
    mapKontest = mapKontest.filter(_._1 != kontest.id) + (kontest.id -> kontest)
  }

}
