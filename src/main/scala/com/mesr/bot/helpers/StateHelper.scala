package com.mesr.bot.helpers

import com.bot4s.telegram.models.Message
import com.mesr.bot._
import com.mesr.bot.sdk.StatefulBot

import scala.util.Try

trait StateHelper extends StatefulBot[AftabeState] with Constants {

  def defaultGame(level: Int = 0) = GameState(level, 0, Seq.empty)

  def defaultState(userId: Long) = AftabeState(UserState(userId, startCoin, isEnteringInviteCode = false, None), defaultGame())

  val db = Database(
    IndexedSeq(
      Level("1856114092:-2456185831143174655:1", "رنگین کمان"),
      Level("1856114092:-2456185831143174655:1", "کمان رنگی")
    )
  )

  def withCurrentState[S, T](f: (AftabeState, Option[Level]) => T)(implicit msg: Message): T = {
    withChatState { implicit optState =>
      val currentState = optState.getOrElse(defaultState(msg.source))
      val currentLevel = Try(db.levels(currentState.gameState.level)).toOption
      setChatState(currentState)

      f(currentState, currentLevel)
    }
  }

}
