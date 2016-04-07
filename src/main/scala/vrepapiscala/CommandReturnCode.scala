package vrepapiscala

import coppelia.remoteApi

/**
  * Created by troxid on 22.11.15.
  */
sealed abstract class CommandReturnCode(val msg: String)
object CommandReturnCode {
  case object Ok
    extends CommandReturnCode("Ok")

  case object NoValue
    extends CommandReturnCode("Input buffer doesn't contain the specified command")

  case object TimeOut
    extends CommandReturnCode("Command reply not received in time for opmode_oneshot_wait operation mode")

  case object IllegalOpMode
    extends CommandReturnCode("Command doesn't support the specified operation mode")

  case object RemoteError
    extends CommandReturnCode("Command caused an error on the server side")

  case object SplitProgress
    extends CommandReturnCode("Previous similar command not yet fully processed " +
      "(applies to opmode_oneshot_split operation modes)")

  case object LocalError
    extends CommandReturnCode("Command caused an error on the client side")

  case object InitializeError
    extends CommandReturnCode("simxStart was not yet called")

  case class UndefCode(code: Int)
    extends CommandReturnCode(s"Undefined return code: $code")

  def fromRaw(code: Int): CommandReturnCode = code match {
    case remoteApi.simx_return_ok                    => Ok
    case remoteApi.simx_return_novalue_flag          => NoValue
    case remoteApi.simx_return_timeout_flag          => TimeOut
    case remoteApi.simx_return_illegal_opmode_flag   => IllegalOpMode
    case remoteApi.simx_return_remote_error_flag     => RemoteError
    case remoteApi.simx_return_split_progress_flag   => SplitProgress
    case remoteApi.simx_return_local_error_flag      => LocalError
    case remoteApi.simx_return_initialize_error_flag => InitializeError
    case undefCode                                   => UndefCode(code)
  }
}
