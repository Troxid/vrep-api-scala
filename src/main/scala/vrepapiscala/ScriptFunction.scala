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
    ints: Seq[Int] = Seq.empty,
    floats: Seq[Float] = Seq.empty,
    strs: Seq[String] = Seq.empty,
    buffer: String = ""): ScriptFunction.Values ={

    val inInts = new IntWA(ints.size)
    ints.copyToArray(inInts.getArray)
    val inFloats = new FloatWA(floats.size)
    floats.copyToArray(inFloats.getArray)
    val inStrs = new StringWA(strs.size)
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
      outInts.getArray.toVector,
      outFloats.getArray.toVector,
      outStrs.getArray.toVector,
      outBuffers.getArray.mkString)
  }
}

object ScriptFunction {
  case class Values(ints: Vector[Int], floats: Vector[Float], strs: Vector[String], buffer: String)
}
