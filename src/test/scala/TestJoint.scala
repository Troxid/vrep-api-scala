
import vrepapiscala.coppelia.remoteApi
import vrepapiscala.{ScriptType, VRepAPI}

object TestJoint extends App {


  val api: VRepAPI = VRepAPI.connect("127.0.0.1", 19997).get

  api.simulation.start()
  val simxHello = api.function("Cuboid", "hello")
  val joint = api.joint.withVelocityControl("joint").get
  val sensor = api.sensor.proximity("sensor").get
  //val res = simxHello(strs = Seq("HelloFromRemote"))

//  joint.setTargetVelocity(2.0f)
//  for(_ <- 0 to 20){
//    Thread.sleep(20)
//    val r = sensor.read
//    if(!r.detectionState){
//      println(s"dp: ${r.detectedPoint}")
//      println(s"do: ${r.detectedObject}")
//    }
//  }



  Thread.sleep(2000)
  api.simulation.stop()

}

/*
  def getAllObjects: Option[Array[Int]] = {
    val handler = new IntWA(1)
    val ret = remote.simxGetObjects(id, remoteApi.sim_handle_all, handler, remoteApi.simx_opmode_oneshot_wait)
    if (ret == remoteApi.simx_return_ok) {
      Some(handler.getArray)
    } else {
      None
    }
  }
 */

//  def testDynamicJoint(): Unit ={
//    println(j1.getMatrix.toVector)
//    j1.setTargetPosition(90)
//    Thread.sleep(1000)
//    println(j1.getMatrix.toVector)
//    j1.setTargetPosition(180)
//    Thread.sleep(1000)
//    println(j1.getMatrix.toVector)
//  }