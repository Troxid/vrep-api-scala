package vrepapiscala

import coppelia.remoteApi
import coppelia.remoteApi._

/**
  * Created by troxid on 22.11.15.
  */
sealed abstract class OpMode(val rawCode: Int)
object OpMode {
  import remoteApi._

  /* Regular operation modes */

  /** The command is sent and a previous reply to the same command returned (if available).
    * @note Non-blocking mode.
    * @note Regular operation modes.
    */
  case object OneShot     extends OpMode(simx_opmode_oneshot)

  /** The command is sent, and the function will wait for the actual reply
    * and return it (if the function doesn't time out). The received command reply will be removed
    * from the inbox buffer (other operation modes will leave their command replies in the inbox buffer)
    * @note Regular operation modes. Blocking mode.
    * @note
    */
  case object OneShotWait extends OpMode(simx_opmode_oneshot_wait)

  /** The command is sent and a previous reply to the same command returned (if available).
    * The command will be continuously executed on the server side.
    * Alpha is a value between 0-65535 representing the delay in ms wanted, before the same command
    * gets executed again on the server side.
    * @note Non-blocking mode.
    * @note Regular operation modes.
    */
  case class Streaming(msDelay: Int = 0)   extends OpMode(simx_opmode_streaming + msDelay){
    require(msDelay >= 0 && msDelay <= 65535, "Alpha must be between 0-65535")
  }

  /* Operation modes for heavy data */

  /**
    * The command is sent in small chunks, and a previous reply to the same command returned (if available).
    * The server will also send the reply in small chunks. The function doesn't wait for the actual reply.
    * Beta is a value between 100 and 65535 representing the maximum chunk size in bytes to send.
    * Small values won't slow down the communication framework, but it will take more time until the full
    * command has been transferred. With large values, commands are transferred faster, but the communication
    * framework might appear frozen while chunks are being transferred.
    * @note Non-blocking mode.
    * @note Operation modes for heavy data.
    * @note Not recommended
    */
  case class OneShotSplit(chunkSize: Int = 100)    extends OpMode(simx_opmode_oneshot_split + chunkSize){
    require(chunkSize >= 100 && chunkSize <= 65535, "Beta must be between 100-65535")
  }

  /**
    * The command is sent in small chunks and a previous reply to the same command returned (if available).
    * The command will be continuously executed on the server side, which will also send the replies in small chunks.
    * The function doesn't wait for the actual reply. Beta is a value between 100 and 65535 representing
    * the maximum chunk size in bytes to send. Small values won't slow down the communication framework, but it
    * will take more time until the full command has been transferred. With large values, commands are
    * transferred faster, but the communication framework might appear frozen while chunks are being transferred.
    * @note Non-blocking mode.
    * @note Operation modes for heavy data.
    * @note Not recommended
    */
  case class StreamingSplit(chunkSize: Int = 100)  extends OpMode(simx_opmode_streaming_split + chunkSize){
    require(chunkSize >= 100 && chunkSize <= 65535, "Beta must be between 100-65535")
  }

  /* Special operation modes */

  /**
    * The command is sent and a previous reply to the same command returned (if available).
    * A same command will be erased from the server side if the command is of streaming or continuous type.
    * The same will happen on the client's input buffer. The function doesn't wait for the actual reply.
    * @note Non-blocking mode.
    * @note Special operation modes
    */
  case object Discontinue extends OpMode(simx_opmode_discontinue)

  /**
    * A previous reply to the same command is returned (if available).
    * The command is not send, nor does the function wait for the actual reply.
    * @note Non-blocking mode.
    * @note Special operation modes
    */
  case object Buffer      extends OpMode(simx_opmode_buffer)

  /**
    * A previous reply to the same command is cleared from the input buffer (if available).
    * The command is not send, nor does the function return any specific values, except for the return code.
    * Can be useful to free some memory on the client side.
    * @note Non-blocking mode.
    * @note Special operation modes
    */
  case object Remove     extends OpMode(simx_opmode_remove)
}