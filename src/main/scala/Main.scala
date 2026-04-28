package org.expr.brkga

import org.scalajs.dom.*
import org.scalajs.dom.html

case class Point(x: Double, y: Double, index: Int = 1)
var points: List[Point] = Nil

val PointSize = 3

object Point:
  def draw(ctx: CanvasRenderingContext2D, p: Point): Unit =
    ctx.fillStyle = "red"
    ctx.strokeStyle = "black"
    ctx.beginPath()
    ctx.arc(p.x, p.y, PointSize, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()
    ctx.fillStyle = "black"
    ctx.font = "12px Arial"
    ctx.fillText(s"${p.index}", p.x + PointSize, p.y - PointSize)

  
  def distance(p1: Point, p2: Point): Double = 
    Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2))

val document = org.scalajs.dom.document
val canvas = document.getElementById("canvas").asInstanceOf[html.Canvas]
val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
val buttonreset = document.getElementById("buttonreset").asInstanceOf[html.Input]
val buttoncalculate = document.getElementById("buttoncalculate").asInstanceOf[html.Input]
val divstatus = document.getElementById("status")

def resizeCanvas(): Unit =
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  drawPoints(ctx)

def drawPoints(ctx: CanvasRenderingContext2D): Unit = 
  points.foreach(p => Point.draw(ctx, p))

def status(text: String): Unit = 
  divstatus.textContent = text

def registerEvents(): Unit = 
  resizeCanvas()
  window.onresize = (_: Event) => resizeCanvas()
  canvas.onclick = (e: MouseEvent) => {
    val rect = canvas.getBoundingClientRect()
    val x = e.clientX - rect.left
    val y = e.clientY - rect.top
    val n = points.size 
    points = Point(x, y, n + 1) :: points
    drawPoints(ctx)
    status(s"Point added at ($x, $y). Number of points: ${points.size}")
  }
  buttonreset.onclick = (e: MouseEvent) => {
    ctx.fillStyle = "white"
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    points = Nil
    status("Canvas reset")
  }
  buttoncalculate.onclick = (e: MouseEvent) => {
    ctx.fillStyle = "white"
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    if (points.size < 2) {
      status("Please add at least 2 points to calculate the TSP solution.")
    } else {
      status("Calculating TSP solution...")
      val ga = createga(points.toArray)
      val finalPop = iterate(ga, generations = 1000)
      val bestCh = best(finalPop)
      val bestTour = bestCh.genes.zipWithIndex.sortBy(_._1).map(_._2)
      // Draw the best tour
      ctx.strokeStyle = "blue"
      ctx.beginPath()
      val startPoint = points(bestTour.head)
      ctx.moveTo(startPoint.x, startPoint.y)
      bestTour.tail.foreach(i => {
        val p = points(i)
        ctx.lineTo(p.x, p.y)
      })
      ctx.lineTo(startPoint.x, startPoint.y) // Return to start
      ctx.stroke()
      drawPoints(ctx)
      status(s"TSP solution calculated. Total distance: ${bestCh.cost}")
    }
  }

@main def hello(): Unit =
  registerEvents()
  status("Ready.")
  
