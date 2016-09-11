package vrepapiscala

import coppelia._

/**
  * Created by trox on 05.04.16.
  */
class ScriptFunction private[vrepapiscala](
  remote: remoteApi,
  id: Int,
  functionName: String,
  scriptType: ScriptType,
  scriptDescription: String = "",
  opMode: OpMode = OpMode.OneShotWait)  {
  import ScriptFunction._
  private final val sizeParams = 10

  def apply(
    ints: Array[Int] = Array.empty,
    floats: Array[Float] = Array.empty,
    strs: Array[String] = Array.empty,
    buffer: String = ""): ScriptFunction.Values ={

    val inInts = new IntWA(ints.length)
    ints.copyToArray(inInts.getArray)
    val inFloats = new FloatWA(floats.length)
    floats.copyToArray(inFloats.getArray)
    val inStrs = new StringWA(strs.length)
    strs.copyToArray(inStrs.getArray)
    val inBuffers = new CharWA(buffer)

    val outInts = new IntWA(sizeParams)
    val outFloats = new FloatWA(sizeParams)
    val outStrs = new StringWA(sizeParams)
    val outBuffers = new CharWA(sizeParams)

    val code = remote.simxCallScriptFunction(id, scriptDescription, scriptType.rawCode, functionName,
      inInts, inFloats, inStrs, inBuffers,
      outInts, outFloats, outStrs, outBuffers,
      OpMode.OneShotWait.rawCode)
    VRepAPI.checkReturnCode(code)
    Values(
      outInts.getArray,
      outFloats.getArray,
      outStrs.getArray,
      outBuffers.getArray.mkString)
  }
}

object ScriptFunction {
  case class Values(ints: Array[Int], floats: Array[Float], strs: Array[String], buffer: String)
}
