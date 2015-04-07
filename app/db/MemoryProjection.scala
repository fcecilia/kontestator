package db

import models.{Kontest}

/**
 * Created by fred on 07/04/15.
 */
object MemoryProjection {



  def kontests : List[Kontest]= ???
  def kontestById(id_kontest:String):Option[Kontest] = ???

  def participantsOfKontest(id_kontest:String):List[Kontest] = ???
}
