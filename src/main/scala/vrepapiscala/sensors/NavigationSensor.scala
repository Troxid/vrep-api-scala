package vrepapiscala.sensors

import coppelia.{FloatWA, remoteApi}
import vrepapiscala.OpMode
import vrepapiscala.common.{EulerAngles, Vec3}

/**
  * Created by troxid on 24.11.15.
  */
class NavigationSensor private[vrepapiscala](remote: remoteApi, id: Int, handle: Int) {

  /**
    * Retrieves the position.
 *
    * @return 3D vector
    */
  def position: Vec3 ={
    val pos = new FloatWA(3)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    remote.simxGetObjectPosition(
      id, handle, -1, pos,
      OpMode.OneShotWait.rawCode)
    val ps = pos.getArray
    Vec3(ps(0), ps(1), ps(2))
  }

  /**
    * Retrieves the orientation.
 *
    * @return Euler angles
    */
  def orientation: EulerAngles = {
    val angles = new FloatWA(3)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    remote.simxGetObjectOrientation(
      id, handle, -1, angles,
      OpMode.OneShotWait.rawCode)
    val an = angles.getArray
    EulerAngles(an(0), an(1), an(2))
  }

  /**
    * Retrieves the linear and angular velocity.
 *
    * @return the linear velocity (vx, vy, vz),
    *         the angular velocity (dAlpha, dBeta, dGamma)
    */
  def velocity: (Vec3, EulerAngles) = {
    val lin = new FloatWA(3)
    val ang = new FloatWA(3)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    remote.simxGetObjectVelocity(
      id, handle, lin, ang,
      OpMode.OneShotWait.rawCode)
    val l = lin.getArray
    val a = lin.getArray
    (Vec3(l(0), l(1), l(2)), EulerAngles(a(0), a(1), a(2)))
  }
}