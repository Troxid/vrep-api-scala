package vrepapiscala.common

/**
  * Created by trox on 05.04.16.
  */
case class AxisMatrix(
  xAxis: EulerAngles,
  yAxis: EulerAngles,
  zAxis: EulerAngles
){
  def toRawMatrix: Array[Float] = Array(
    xAxis.alpha, yAxis.alpha, zAxis.alpha, 0,
    xAxis.beta,  yAxis.beta,  zAxis.beta,  0,
    xAxis.gamma, yAxis.gamma, zAxis.gamma, 0
  )
}

object AxisMatrix {
  def fromRawMatrix(arr: Array[Float]): AxisMatrix = {
    AxisMatrix(
      EulerAngles(arr(0), arr(4), arr(8)),
      EulerAngles(arr(1), arr(5), arr(9)),
      EulerAngles(arr(2), arr(6), arr(10))
    )
  }
}
