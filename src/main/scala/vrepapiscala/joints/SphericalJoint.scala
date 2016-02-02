package vrepapiscala.joints

import coppelia.remoteApi

/**
  * Created by trox on 01.02.16.
  */
class SphericalJoint private[vrepapiscala](remote: remoteApi, id: Int, handle: Int){
  private val joint = new AnyJoint(remote, id, handle)

  /** Retrieves the intrinsic transformation matrix of a joint (the transformation caused by the joint movement).
    * @return matrix: 12 values (output).
    */
  def matrix: Array[Float] = joint.getMatrix


  /** Sets the intrinsic orientation matrix of a spherical joint object.
    *  @param matrix array of 12 values.
    *  @throws IllegalArgumentException matrix must be contain 12 values
    */
  def matrix_=(matrix: Array[Float]) = joint.setSphericalMatrix(matrix)
}