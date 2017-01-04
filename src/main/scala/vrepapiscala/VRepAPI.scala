package vrepapiscala

import java.io.{File, FileNotFoundException}
import java.util.Locale

import coppelia.remoteApi
import cz.adamh.utils.NativeUtils
import vrepapiscala.common.ReturnCommandException
import vrepapiscala.joints.Joints
import vrepapiscala.sensors._

import scala.util.{Failure, Success, Try}


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
  private var nativeIsLoaded = false
  private val remote = new remoteApi()

  def connect(ip: String, port: Int): Option[VRepAPI] = {
    checkNative()
    remote.simxFinish(-1)
    val id = remote.simxStart(ip, port, true, true, 5000, 5)
    if (id == remoteApi.simx_return_ok) {
      Some(new VRepAPI(id: Int))
    } else {
      None
    }
  }

  def loadDefaultNative(): Unit ={
    val arch = System.getProperty("sun.arch.data.model") // 32 or 64
    val opSys   = System.getProperty("os.name").toLowerCase(Locale.ENGLISH)
    println("""Native libs preload from "V-REP_PRO_EDU_V3_3_2""")
    println(s"JRE arch: $arch bit, OS: $opSys")
    val path = (arch, opSys) match {
      // Windows
      case ("32", os) if os.contains("win")  => "/win/32Bit/remoteApiJava.dll"
      case ("64", os) if os.contains("win")  => "/win/64Bit/remoteApiJava.dll"

      // Linux
      case ("32", os) if os.contains("nux")  => "/nux/32Bit/libremoteApiJava.so"
      case ("64", os) if os.contains("nux")  => "/nux/64Bit/libremoteApiJava.so"

      // MacOS
      case (_, os)    if os.contains("mac")  => "/mac/libremoteApiJava.dylib"

      // Error
      case _ =>
        throw new FileNotFoundException("not found arch and os information")
    }
    println(s"Loading... : $path")
    Try(NativeUtils.loadLibraryFromJar(path)) match {
      case Failure(exception) =>
        val absPath = new File("/native_lib/" + path.tail)
        println(s"Loading from JAR failed (${exception.getMessage}).")
        println(s"Try load from directory: ${absPath.getAbsolutePath}")
        Try(System.load(absPath.getAbsolutePath)) match {
          case Failure(exception) =>
            println(s"Loading from directory failed. ${exception.getMessage}")
          case Success(_) =>
            println("Native library loaded")
        }
      case Success(_) =>
        println("Native library loaded")
    }
    nativeIsLoaded = true
  }

  /**
    * Load native library from specific path
    * @param absolutePath - absolute path to native libs
    */
  def loadSpecificNative(absolutePath: String): Unit ={
    System.load(absolutePath)
    nativeIsLoaded = true
  }

  def checkNative(): Unit ={
    if(!this.nativeIsLoaded){
      loadDefaultNative()
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
