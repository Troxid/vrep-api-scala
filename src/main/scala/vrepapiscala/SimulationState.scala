package vrepapiscala

import coppelia.remoteApi

/**
  * Created by trox on 02.02.16.
  */
class SimulationState private[vrepapiscala](remote: remoteApi, id: Int){
  /** Requests a start of a simulation (or a resume of a paused simulation). */
  def start(): CommandReturnCode ={
    val res = remote.simxStartSimulation(id, OpMode.OneShotWait.rawCode)
    CommandReturnCode.fromRaw(res)
  }

  /** Requests a pause of a simulation. */
  def pause(): CommandReturnCode ={
    val res = remote.simxPauseSimulation(id, OpMode.OneShotWait.rawCode)
    CommandReturnCode.fromRaw(res)
  }

  /** Requests a stop of the running simulation. */
  def stop(): CommandReturnCode ={
    val res = remote.simxStopSimulation(id, OpMode.OneShotWait.rawCode)
    CommandReturnCode.fromRaw(res)
  }
}
