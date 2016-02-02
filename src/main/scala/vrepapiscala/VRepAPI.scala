package vrepapiscala

import coppelia._
import vrepapiscala.joints.Joints
import vrepapiscala.sensors._

/**
  * Created by troxid on 22.11.15.
  */
class VRepAPI private(id: Int) {
  import VRepAPI.remote

  val simulation = new SimulationState(remote, id)

  val joint = new Joints(remote, id)

  val sensor = new Sensors(remote, id)
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
}
