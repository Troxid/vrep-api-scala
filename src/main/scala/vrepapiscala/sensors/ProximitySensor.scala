package vrepapiscala.sensors

import coppelia.{BoolW, FloatWA, IntW, remoteApi}
import vrepapiscala.OpMode
import vrepapiscala.VRepAPI._
import vrepapiscala.common.Vec3

/**
  * Created by troxid on 22.11.15.
  */
class ProximitySensor private[vrepapiscala](remote: remoteApi, id: Int, handle: Int, opMode: OpMode) {
  import ProximitySensor._

  /** Reads the state of a proximity sensor. */
  def read: ProximitySensor.Values = {
    val ds   = new BoolW(false) // detection state
    //TODO: size of arr as function param
    val dp   = new FloatWA(20) // detected point
    val doi  = new IntW(-1) // detected object (information)
    val dsnv = new FloatWA(3) // detected surface normal vector
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    checkReturnCode(remote.simxReadProximitySensor(
      id, handle, ds, dp, doi, dsnv, opMode.rawCode))
    val dsna = dsnv.getArray
    val dpa = dp.getArray
    Values(ds.getValue,
      Vec3(dpa(0), dpa(1), dpa(2)),
      new PositionSensor(remote, id, doi.getValue, opMode),
      Vec3(dsna(0), dsna(1), dsna(2)))
  }
}

object ProximitySensor {
  /** Contains detected values of sensor
 *
    * @param detectionState the detection state.
    * @param detectedPoint the detected point coordinates (relative to the sensor reference frame).
    * @param detectedObject the geolocation information of the detected object.
    * @param detectedSurfaceNormalVector the normal vector (normalized) of the detected surface.
    *                                    Relative to the sensor reference frame.
    */
  case class Values(detectionState: Boolean,
                    detectedPoint: Vec3,
                    detectedObject: PositionSensor,
                    detectedSurfaceNormalVector: Vec3)
}

