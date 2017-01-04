import vrepapiscala.VRepAPI

/**
  * Created by troxid on 04.01.17.
  */
object OpenConnectionTest extends App {
  val api = VRepAPI.connect("127.0.0.1", 19997).get
  api.simulation.start()
  Thread.sleep(1000)
  api.simulation.stop()
}
