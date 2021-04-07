package QTrees

import java.awt.Color
import QTrees.BitMap._

import scala.annotation.tailrec
case class BitMap(img: List[List[Int]]) {
  def makeQTree(): QTree[Coords] = BitMap.makeQTree(this)
}

object BitMap {

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  def makeQTree (b:BitMap): QTree[Coords] = {
    val x = b.img.length - 1
    val y = b.img(0).length - 1 //img(0) pode ser substituido por algo mais limpo?
    auxMQT( ((0,0):Point, (x,y):Point):Coords, b )
  }


  def auxMQT (c:Coords, b: BitMap):QTree[Coords] = {

    //temos que considerar que pode não ser quadrada e imagens com lados impares

    if( (c._1._1 == c._2._1) && (c._1._2 == c._2._2) ) {
      if(c._1._1 == -1 ){ //melhorar com enumerados talvez
        println("EMPTY")
        return QEmpty
      }
      val a = ImageUtil.decodeRgb(b.img(c._1._2)(c._1._1)).toList
      val color = new Color(a(0),a(1),a(2))
      return new QLeaf[Coords, Section]( (c, color):Section )
    }

    val sCords = splitCoords(c)

    println("sCords._1: " + sCords._1)
    println("sCords._2: " + sCords._2)
    println("sCords._3: " + sCords._3)
    println("sCords._4: " + sCords._4)

    val qtOne = auxMQT(sCords._1, b)
    val qtTwo = auxMQT(sCords._2, b)
    val qtThree = auxMQT(sCords._3, b)
    val qtFour = auxMQT(sCords._4, b)

    (qtOne, qtTwo, qtThree, qtFour) match {
      case (q1: QLeaf[Coords, Section], q2:QLeaf[Coords, Section], q3:QLeaf[Coords, Section], q4:QLeaf[Coords, Section]) => {
        if( (q1.value._2 equals q2.value._2) && (q3.value._2 equals q4.value._2) && (q1.value._2 equals q4.value._2 ) )
          new QLeaf[Coords, Section]( (c, q1.value._2):Section )
        else
          new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)
      }
      case _ => new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)
    }
  }

  def splitCoords(c:Coords): (Coords, Coords, Coords, Coords) = { //Está mal aqui probably
    val width = c._2._1 - c._1._1 + 1
    val height = c._2._2 - c._1._2 + 1
    println("Width: " + width + " Height: " + height)
    if(width == 2 && height == 1){
      print("IN1")
      val ac1 = ( (c._1),( c._2._1-width/2,c._2._2 ) )
      val ac2 = ( ( c._1._1+width/2,c._1._2),(c._2) )
      val ac3 = ( (-1,-1), (-1,-1) ) //enumerado
      val ac4 = ( (-1,-1), (-1,-1) )
      return (ac1,ac2,ac3,ac4)
    }
    if(height == 2 && width == 1){
      print("IN2")
      val ac1 = ( (c._1),( c._2._1,c._2._2-height/2 ) )
      val ac2 = ( (-1,-1), (-1,-1) )
      val ac3 = ( ( c._1._1,c._1._2+height/2),(c._2) )
      val ac4 = ( (-1,-1), (-1,-1) )
      return (ac1,ac2,ac3,ac4)
    }

    val c1 = ( (c._1),( c._2._1-(width/2).ceil.toInt,c._2._2-(height/2).ceil.toInt ) )
    val c2 = ( ( c._1._1+(width/2).ceil.toInt,c._1._2),(c._2._1,c._2._2-(height/2).floor.toInt ) )
    val c3 = ( (c._1._1,c._1._2+(height/2).ceil.toInt),(c._2._1-(width/2).floor.toInt,c._2._2) )
    val c4 = ( (c._1._1+(width/2).ceil.toInt, c._1._2+(height/2).ceil.toInt),(c._2) )
    (c1,c2,c3,c4)
  }

}