package vrepapiscala

import coppelia.remoteApi

/**
  * Created by troxid on 22.11.15.
  */
sealed trait CommandReturnCode
object CommandReturnCode {
  case class Ok()              extends CommandReturnCode
  case class NoValue()         extends CommandReturnCode
  case class TimeOut()         extends CommandReturnCode
  case class IllegalOpMode()   extends CommandReturnCode
  case class RemoteError()     extends CommandReturnCode
  case class SplitProgress()   extends CommandReturnCode
  case class LocalError()      extends CommandReturnCode
  case class InitializeError() extends CommandReturnCode

  def fromRaw(code: Int): CommandReturnCode = code match {
    case remoteApi.simx_return_ok                    => Ok()
    case remoteApi.simx_return_novalue_flag          => NoValue()
    case remoteApi.simx_return_timeout_flag          => TimeOut()
    case remoteApi.simx_return_illegal_opmode_flag   => IllegalOpMode()
    case remoteApi.simx_return_remote_error_flag     => RemoteError()
    case remoteApi.simx_return_split_progress_flag   => SplitProgress()
    case remoteApi.simx_return_local_error_flag      => LocalError()
    case remoteApi.simx_return_initialize_error_flag => InitializeError()
  }
}
