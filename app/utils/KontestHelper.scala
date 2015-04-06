package utils

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by fred on 06/04/15.
 */
object KontestHelper {

  private def randomAlphaNumericString(length: Int): String = {
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    randomStringFromCharList(length, chars)
  }


  private def randomStringFromCharList(length: Int, chars: Seq[Char]): String = {
    val sb = new StringBuilder
    for (i <- 1 to length) {
      val randomNum = util.Random.nextInt(chars.length)
      sb.append(chars(randomNum))
    }
    sb.toString
  }

  private val format = new SimpleDateFormat("DHms")

  private def generateID: String = format.format(new Date) + randomAlphaNumericString(4)


  def generateUserID = "U" + generateID

  def generateKontestID = "K" + generateID


}
