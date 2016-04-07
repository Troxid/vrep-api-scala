package vrepapiscala

import coppelia.remoteApi
import vrepapiscala.common.ReturnCommandException
import vrepapiscala.joints.Joints
import vrepapiscala.sensors._

/**
  * Created by troxid on 22.11.15.
  */
class VRepAPI private(id: Int) extends AutoCloseable {
  import VRepAPI.remote

  val simulation = new SimulationState(remote, id)

  val joint = new Joints(remote, id)

  val sensor = new Sensors(remote, id)

  /**
    * Retrieves a V-REP script function. Call this only for non-threaded scripts,
    * and when calling simulation scripts, then simulation must be running.
    * <p>
    * '''The called script function should always have following input/output form:'''
    * {{{
    * myFunctionName=function(inInts,inFloats,inStrings,inBuffer)
    *     -- inInts, inFloats and inStrings are tables
    *     -- inBuffer is a string
    *
    *     -- Perform any type of operation here.
    *
    *     -- Always return 3 tables and a string, e.g.:
    *     return {},{},{},''
    * end
    * }}}
    * @param functionName the name of the Lua function to call in the specified script.
    * @param scriptType the type of the script
    * @param scriptDescription the name of the scene object where the script is attached to, or an
    *                          empty string if the script has no associated scene object.
    * @param opMode a remote API function operation mode
    */
  def function(
    scriptDescription: String,
    functionName: String,
    scriptType: ScriptType = ScriptType.ChildScript,
    opMode: OpMode = OpMode.OneShotWait): ScriptFunction ={
    new ScriptFunction(remote, id, functionName, scriptType, scriptDescription, opMode)
  }

  override def close(): Unit = simulation.stop()
}

object VRepAPI {
  private val remote = new remoteApi()
  remote.simxFinish(-1)

  def connect(ip: String, port: Int): Option[VRepAPI] = {
    val id = remote.simxStart(ip, port, true, true, 5000, 5)
    if (id == remoteApi.simx_return_ok) {
      Some(new VRepAPI(id: Int))
    } else {
      None
    }
  }

  private[vrepapiscala] def checkReturnCode[T](rawCode: Int):Unit ={
    CommandReturnCode.fromRaw(rawCode) match {
      case CommandReturnCode.Ok => ()
      case CommandReturnCode.NoValue => ()
      case errorCode => throw new ReturnCommandException(errorCode)
    }
  }
}
