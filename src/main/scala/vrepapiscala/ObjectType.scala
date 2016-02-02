package vrepapiscala

/**
  * Created by trox on 02.02.16.
  */
sealed abstract class ObjectType(val rawCode: Int)
object ObjectType {
  import coppelia.remoteApi._

  case object Shape extends ObjectType(sim_object_shape_type)
  case object Joint extends ObjectType(sim_object_joint_type)
  case object Graph extends ObjectType(sim_object_graph_type)
  case object Camera extends ObjectType(sim_object_camera_type)
  case object Dummy extends ObjectType(sim_object_dummy_type)
  case object ProximitySensor extends ObjectType(sim_object_proximitysensor_type)
  case object Path extends ObjectType(sim_object_path_type)
  case object VisionSensor extends ObjectType(sim_object_visionsensor_type)
  case object Volume extends ObjectType(sim_object_volume_type)
  case object Mill extends ObjectType(sim_object_mill_type)
  case object ForceSensor extends ObjectType(sim_object_forcesensor_type)
  case object Light extends ObjectType(sim_object_light_type)
  case object Mirror extends ObjectType(sim_object_mirror_type)
}

