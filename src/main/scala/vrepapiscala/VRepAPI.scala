package vrepapiscala

import coppelia.{IntW, remoteApi, IntWA}

/**
  * Created by troxid on 22.11.15.
  */
class VRepAPI private(id: Int) {
  import VRepAPI._

  def getAllObjects: Option[Array[Int]] = {
    val handler = new IntWA(1)
    val ret = remote.simxGetObjects(id, remoteApi.sim_handle_all, handler, remoteApi.simx_opmode_oneshot_wait)
    if (ret == remoteApi.simx_return_ok) {
      Some(handler.getArray)
    } else {
      None
    }
  }

  /** Requests a start of a simulation (or a resume of a paused simulation). */
  def startSimulation(): CommandReturnCode ={
    val res = remote.simxStartSimulation(id, defOpMode.rawCode)
    CommandReturnCode.fromRaw(res)
  }

  /** Requests a pause of a simulation. */
  def pauseSimulation(): CommandReturnCode ={
    val res = remote.simxPauseSimulation(id, defOpMode.rawCode)
    CommandReturnCode.fromRaw(res)
  }

  /** Requests a stop of the running simulation. */
  def stopSimulation(): CommandReturnCode ={
    val res = remote.simxStopSimulation(id, defOpMode.rawCode)
    CommandReturnCode.fromRaw(res)
  }

  def getJoint(name: String): Option[Joint] ={
    getObjectHandle(name).map(new Joint(remote, id, _))
  }

  def getProximitySensor(name: String): Option[ProximitySensor] ={
    getObjectHandle(name).map(new ProximitySensor(remote, id, _))
  }

  def getVisionSensor(name: String, resolution: (Int, Int)): Option[VisionSensor] ={
    getObjectHandle(name).map(new VisionSensor(remote, id, _, resolution))
  }

  def getForceSensor(name: String): Option[ForceSensor] ={
    getObjectHandle(name).map(new ForceSensor(remote, id, _))
  }

  def getNavigationSensor(name: String): Option[NavigationSensor] ={
    getObjectHandle(name).map(new NavigationSensor(remote, id, _))
  }

  private def getObjectHandle(name: String): Option[Int] = {
    val h = new IntW(-1)
    val res = remote.simxGetObjectHandle(id, name, h, defOpMode.rawCode)
    checkOption(res, h.getValue)
  }


}

object VRepAPI {
  private val defOpMode = OpMode.OneShotWait
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

  private def checkOption[T](res: Int, obj: T): Option[T] = {
    if (res == remoteApi.simx_return_ok) {
      Some(obj)
    } else {
      None
    }
  }

}

/*
Remote API helper functions

simxStart
simxFinish
simxGetConnectionId
simxPauseCommunication
simxCreateBuffer
simxReleaseBuffer
simxSynchronous
simxSynchronousTrigger
simxGetPingTime

General object handle retrieval

simxGetObjectHandle
simxGetObjectGroupData
simxGetObjects
simxGetObjectChild
simxGetObjectParent
simxGetUIHandle
simxGetCollisionHandle
simxGetDistanceHandle

Collision detection functionality

simxGetCollisionHandle
simxGetObjectGroupData
simxReadCollision
 */