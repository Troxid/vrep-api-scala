package vrepapiscala.joints

import coppelia.remoteApi


/**
  * Created by trox on 02.02.16.
  */
sealed abstract class JointType(val rawCode: Int)
object JointType {
  import remoteApi._
  case object Revolute extends JointType(sim_joint_revolute_subtype)
  case object Prismatic extends JointType(sim_joint_prismatic_subtype)
  case object Spherical extends JointType(sim_joint_spherical_subtype)

  def fromRawCode(rawCode: Int): JointType = rawCode match {
    case `sim_joint_revolute_subtype` => Revolute
    case `sim_joint_prismatic_subtype` => Prismatic
    case `sim_joint_spherical_subtype` => Spherical
  }
}