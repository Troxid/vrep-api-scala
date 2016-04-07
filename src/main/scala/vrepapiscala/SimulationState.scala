package vrepapiscala

import coppelia.remoteApi
import VRepAPI._

/**
  * Created by trox on 02.02.16.
  */
class SimulationState private[vrepapiscala](remote: remoteApi, id: Int){
  /** Requests a start of a simulation (or a resume of a paused simulation). */
  def start(): Unit ={
    checkReturnCode(remote.simxStartSimulation(id, OpMode.OneShotWait.rawCode))
  }

  /** Requests a pause of a simulation. */
  def pause(): Unit ={
    checkReturnCode(remote.simxPauseSimulation(id, OpMode.OneShotWait.rawCode))
  }

  /** Requests a stop of the running simulation. */
  def stop(): Unit ={
    checkReturnCode(remote.simxStopSimulation(id, OpMode.OneShotWait.rawCode))
  }
}
