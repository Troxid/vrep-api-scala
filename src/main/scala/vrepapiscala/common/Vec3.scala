package vrepapiscala.common

/**
  * Created by troxid on 23.11.15.
  */
case class Vec3(x: Float, y: Float, z: Float){
  def length: Float = Math.sqrt(x*x + y*y + z*z).toFloat
}