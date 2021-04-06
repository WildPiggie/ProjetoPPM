package QTrees

import java.awt.Color

case class BitMap(img: List[List[Int]]){

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  def makeQTree (b:BitMap): QTree[Coords] = {
    val x = b.img.length - 1
    val y = b.img(0).length - 1 //img(0) pode ser substituido por algo mais limpo?
    auxMQT( ((0,0):Point, (x,y):Point):Coords, b )
  }

  def auxMQT (c:Coords, b: BitMap):QTree[Coords] = {

    if(c._1 == c._2) {
      val a = ImageUtil.decodeRgb(img(c._1._1)(c._1._1)).toList
      val color = new Color(a(0),a(1),a(2))
      new QLeaf(c, color)
    }

    val sCords = splitCoords(c)

    val qtOne = auxMQT(sCords._1, b)
    val qtTwo = auxMQT(sCords._2, b)
    val qtThree = auxMQT(sCords._3, b)
    val qtFour = auxMQT(sCords._4, b)

    //if(qtOne isInstanceOf) ???

    //QNode( ( )//(value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A])
  }

  def splitCoords(c:Coords): (Coords, Coords, Coords, Coords) = {
    val side = c._2._1 - c._1._1
    val c1 = ( (c._1),(c._2._1-side/2,c._2._2-side/2) )
    val c2 = ( (c._1._1+side/2,c._1._2),(c._2._1,c._2._2-side/2) )
    val c3 = ( (c._1._1,c._1._2+side/2),(c._2._1-side/2,c._2._2) )
    val c4 = ( (c._1._1+side/2, c._1._2+side/2),(c._2) )
    (c1,c2,c3,c4)
  }
}
