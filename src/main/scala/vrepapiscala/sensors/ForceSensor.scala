package vrepapiscala.sensors

import coppelia.{FloatWA, IntW, remoteApi}
import vrepapiscala.OpMode
import vrepapiscala.VRepAPI._
import vrepapiscala.common.Vec3

/**
  * Created by troxid on 23.11.15.
  */
class ForceSensor private[vrepapiscala](remote: remoteApi, id: Int, handle: Int, opMode: OpMode) {
  import ForceSensor._

  /**
    * Reads the force and torque applied to a force sensor (filtered values are read),
    * and its current state ('unbroken' or 'broken').
    */
  def read: ForceSensor.Values = {
    val state = new IntW(-1)
    val forceVec = new FloatWA(3)
    val torqueVec = new FloatWA(3)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    checkReturnCode(remote.simxReadForceSensor(
      id, handle, state, forceVec, torqueVec, opMode.rawCode))
    val fa = forceVec.getArray
    val ta = torqueVec.getArray
    Values(if(state.getValue == 1) true else false, Vec3(fa(0), fa(1), fa(2)), Vec3(ta(0), ta(1), ta(2)))
  }

  /**
    * Allows breaking a force sensor during simulation.
    * A broken force sensor will lose its positional and orientational constraints.
    */
  def break(): Unit = {
    checkReturnCode(remote.simxBreakForceSensor(
      id, handle, opMode.rawCode))
  }


}

object ForceSensor {
  /** Contains detected values of sensor
    * @param state: the state of the force sensor .
    *             false : force and torque data is available, otherwise it is not (yet) available
    *             (e.g. when not enough values are present for the filter) true: force sensor is broken,
    *             otherwise it is still intact ('unbroken')
    * @param forceVector: the force vector.
    * @param torqueVector: the torque vector.
    */
  case class Values(state: Boolean, forceVector: Vec3, torqueVector: Vec3)
}
