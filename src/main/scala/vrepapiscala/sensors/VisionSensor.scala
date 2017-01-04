package vrepapiscala.sensors

import java.awt.Color
import java.awt.image.BufferedImage

import coppelia._
import vrepapiscala.OpMode
import vrepapiscala.VRepAPI._

/**
  * Created by troxid on 22.11.15.
  */

class VisionSensor private[vrepapiscala](
  remote: remoteApi,
  id: Int,
  handle: Int, resolution: (Int, Int),
  opMode: OpMode) {

  val (resolutionX, resolutionY) = resolution
  private var fstCall = true

  def read(bufferSize: Int): (Boolean, Array[Array[Float]]) ={
    val ds = new BoolW(false)
    val av = new FloatWAA(bufferSize)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    checkReturnCode(remote.simxReadVisionSensor(
      id, handle, ds, av,
      OpMode.OneShotWait.rawCode))
    val resArr = new Array[Array[Float]](bufferSize)
    for(i <- resArr.indices){
      resArr(i) = av.getArray()(i).getArray
    }
    (ds.getValue, resArr)
  }

  /**
    *  Retrieves the image of a vision sensor.
 *
    * @param greyScale true: each image pixel is a byte (greyscale image), otherwise each image pixel is a rgb
    * @return the image data
    */
  def rawImage(greyScale: Boolean = false): Array[Int] ={
    val numOfClr = if (greyScale) 1 else 3
    val res = new IntWA(2)
    val img = new CharWA(resolutionX * resolutionY * numOfClr)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    checkReturnCode(remote.simxGetVisionSensorImage(
      id, handle, res, img,
      if (greyScale) 1 else 0,
      OpMode.OneShotWait.rawCode))
    val ra = res.getArray
    val imgArr = img.getArray.map(ch => ch.toInt)
    if(fstCall){
      fstCall = false
      checkResolutions((ra(0), ra(1)), resolution)
    }
    imgArr
  }

  /**
    * Retrieves the depth buffer of a vision sensor.
 *
    * @return the depth buffer data.
    *         Values are in the range of 0-1 (0=closest to sensor, 1=farthest from sensor).
    */
  def depthBuffer: Array[Float] = {
    val res = new IntWA(2)
    val buf = new FloatWA(resolutionX * resolutionY)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    checkReturnCode(remote.simxGetVisionSensorDepthBuffer(
      id, handle, res, buf,
      OpMode.OneShotWait.rawCode))
    val ra = res.getArray
    if(fstCall){
      fstCall = false
      checkResolutions((ra(0), ra(1)), resolution)
    }
    buf.getArray
  }

  def colorFrame: Array[Array[Color]] ={
    val img = this
      .rawImage()
      .grouped(3)
      .map(arr => new Color(arr(0), arr(1), arr(2)))
      .toVector
    val resutlArr = Array.ofDim[Color](resolutionY, resolutionX)
    for(y <- 0 until resolutionY; x <- 0 until resolutionX){
      val index = y * resolutionY + x
      resutlArr((resolutionY - 1) - y)(x) = img(index)
    }
    resutlArr
  }

  def colorImg: BufferedImage = {
    val frame = this.colorFrame
    val img = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_RGB)
    for(y <- 0 until resolutionY; x <- 0 until resolutionX){
      img.setRGB(x, y, frame(y)(x).getRGB)
    }
    img
  }

  private def checkResolutions(realRes: (Int, Int), userRes: (Int, Int)): Unit ={
    require(realRes == userRes,
      s"""Resolutions must be same
        |in program you select: $userRes
        |but in v-rep: $realRes
      """.stripMargin)
  }
}

