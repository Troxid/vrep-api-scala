package vrepapiscala.joints

import coppelia.remoteApi


/**
  * Created by trox on 02.02.16.
  */
sealed abstract class JointMode(val rawCode: Int)
object JointMode {
  import remoteApi._

  case object Force extends JointMode(sim_jointmode_force)
  case object Passive extends JointMode(sim_jointmode_passive)
  case object Dependent extends JointMode(sim_jointmode_dependent)
  case object InverseKinematic extends JointMode(sim_jointmode_ik)
  case object InverseKinematicDependent extends JointMode(sim_jointmode_ikdependent)

  def fromRawCode(rawCode: Int): JointMode = rawCode match {
    case `sim_jointmode_force` => Force
    case `sim_jointmode_passive` => Passive
    case `sim_jointmode_dependent` => Dependent
    case `sim_jointmode_ik` => InverseKinematic
    case `sim_jointmode_ikdependent` => InverseKinematicDependent
  }
}

