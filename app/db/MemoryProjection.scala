package db

import models.{Kontest, User}

/**
 * Created by fred on 07/04/15.
 */
object MemoryProjection {

  def users : List[User]= ???
  def userById(id_user:String):Option[User] = ???

  def kontests : List[Kontest]= ???
  def kontestById(id_kontest:String):Option[User] = ???

  def participantsOfKontest(id_kontest:String):List[User] = ???
}
