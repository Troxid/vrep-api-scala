package vrepapiscala.joints

import coppelia.remoteApi
import vrepapiscala.OpMode
import vrepapiscala.common.{AxisMatrix, EulerAngles}

/**
  * Created by trox on 01.02.16.
  */
class SphericalJoint private[vrepapiscala](
  remote: remoteApi, id: Int, handle: Int, opMode: OpMode){
  private val joint = new AnyJoint(remote, id, handle, opMode)

  /** Retrieves the intrinsic transformation matrix of a joint (the transformation caused by the joint movement).
    *
    * @return matrix: 12 values (output).
    */
  def rawMatrix: Array[Float] = joint.getMatrix


  /** Sets the intrinsic orientation matrix of a spherical joint object.
    *
    *  @param matrix array of 12 values.
    *  @throws IllegalArgumentException matrix must be contain 12 values
    */
  def rawMatrix_=(matrix: Array[Float]): Unit = joint.setSphericalMatrix(matrix)

  def matrix_=(m: AxisMatrix): Unit = {
    val arr = Array(
      m.xAxis.alpha, m.yAxis.alpha, m.zAxis.alpha, 0,
      m.xAxis.beta,  m.yAxis.beta,  m.zAxis.beta, 0,
      m.xAxis.gamma, m.yAxis.gamma, m.zAxis.gamma, 0
    )
    rawMatrix = arr
  }

  def matrix: AxisMatrix = {
    val arr = rawMatrix
    AxisMatrix(
      EulerAngles(arr(0), arr(4), arr(8)),
      EulerAngles(arr(1), arr(5), arr(9)),
      EulerAngles(arr(2), arr(6), arr(10))
    )
  }
}