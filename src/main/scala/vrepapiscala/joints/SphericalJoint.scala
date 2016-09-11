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
  def setRawMatrix(matrix: Array[Float]): Unit =
    this.rawMatrix = matrix

  def matrix_=(m: AxisMatrix): Unit = {
    rawMatrix = m.toRawMatrix
  }
  def setMatrix(m: AxisMatrix): Unit ={
    rawMatrix = m.toRawMatrix
  }

  def matrix: AxisMatrix = AxisMatrix.fromRawMatrix(rawMatrix)

}