package vrepapiscala

import coppelia.{IntW, FloatWA, BoolW, remoteApi}
import vrepapiscala.common.Vec3

/**
  * Created by troxid on 22.11.15.
  */
class ProximitySensor private[vrepapiscala](remote: remoteApi, id: Int, handle: Int) {

  /** Reads the state of a proximity sensor. */
  def read: ProximitySensor#Values = {
    val ds   = new BoolW(false)
    //TODO: size of arr as function param
    val dp   = new FloatWA(20)
    val doh  = new IntW(-1)
    val dsnv = new FloatWA(3)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    remote.simxReadProximitySensor(id, handle, ds, dp, doh, dsnv, OpMode.OneShot.rawCode)
    val dsna = dsnv.getArray
    Values(ds.getValue, dp.getArray, doh.getValue, Vec3(dsna(0), dsna(1), dsna(2)))
  }

  /** Contains detected values of sensor
    * @param detectionState the detection state.
    * @param detectedPoint the detected point coordinates (relative to the sensor reference frame).
    * @param detectedObjectHandle the handle of the detected object.
    * @param detectedSurfaceNormalVector the normal vector (normalized) of the detected surface.
    *                                    Relative to the sensor reference frame.
    */
  case class Values(detectionState: Boolean,
                    detectedPoint: Array[Float],
                    detectedObjectHandle: Int,
                    detectedSurfaceNormalVector: Vec3)

}

