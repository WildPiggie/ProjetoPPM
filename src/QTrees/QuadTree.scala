package QTrees

import java.awt.Color
import QTrees.QuadTree._

case class QuadTree(qt: QTree[Coords]){

  def makeBitMap (quadT: QTree[Coords]): BitMap = QuadTree.makeBitMap(this.qt) //correto?
  def scale (scale:Double, quadT:QTree[Coords]):QTree[Coords] = ???
  def mirrorV ():QTree[Coords] = QuadTree.mirrorV(this.qt)
  def mirrorH ():QTree[Coords] = QuadTree.mirrorH(this.qt)
  def rotateD (quadT:QTree[Coords]):QTree[Coords] = ???
  def rotateR (quadT:QTree[Coords]):QTree[Coords] = ???
  def mapColourEffect (f:Color => Color):QTree[Coords] = QuadTree.mapColourEffect(f, this.qt)

}

object QuadTree{

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  def makeBitMap (qt: QTree[Coords]): BitMap = ???
  def scale (scale:Double, qt:QTree[Coords]):QTree[Coords] = ???

  def mirrorV (qt:QTree[Coords]):QTree[Coords] = {
    qt match {
      case (qn: QNode[Coords]) => {
        val height = qn.value._2._2 - qn.value._1._2
        def newCoords(coords: Coords): Coords = { //Podemos colocar como função lambda dentro da chamada
          ( (coords._1._1, height-coords._2._2), (coords._2._1, height-coords._1._2) )
        }
        def switchQTreeOrder(qn: QNode[Coords]): QNode[Coords] = {
          QNode(qn.value, qn.three, qn.four, qn.one, qn.two)
        }
        auxMirror(qn, newCoords, switchQTreeOrder)
      }
      case _ => qt
    }
  }

  def mirrorH (qt:QTree[Coords]):QTree[Coords] = {
    qt match {
      case (qn: QNode[Coords]) => {
        val width = qn.value._2._1 - qn.value._1._1
        def newCoords(coords: Coords): Coords = { //Podemos colocar como função lambda dentro da chamada
          ( (width-coords._2._1, coords._1._2), (width-coords._1._1, coords._2._2) )
        }
        def switchQTreeOrder(qn: QNode[Coords]): QNode[Coords] = {
          QNode(qn.value, qn.two, qn.one, qn.four, qn.three)
        }
        auxMirror(qn, newCoords, switchQTreeOrder)
      }
      case _ => qt
    }
  }

  def auxMirror(qt:QTree[Coords], f: Coords => Coords, switchQTreeOrder:QNode[Coords] => QNode[Coords]): QTree[Coords] = {
    qt match {

      case (qn: QNode[Coords]) => {
        val (m1, m2, m3, m4) = (auxMirror(qn.one, f, switchQTreeOrder), auxMirror(qn.two, f, switchQTreeOrder), auxMirror(qn.three, f, switchQTreeOrder), auxMirror(qn.four, f, switchQTreeOrder))
        val (upd1, upd2, upd3, upd4) = ( updateCoords(m1, f), updateCoords(m2, f), updateCoords(m3, f), updateCoords(m4, f) )
        switchQTreeOrder( QNode(qn.value, upd1, upd2, upd3, upd4) )
      }
      case _ => qt
    }
  }

  def updateCoords(qt: QTree[Coords], f: Coords => Coords): QTree[Coords] = {
    qt match {
      case (qn:QNode[Coords]) =>
        QNode( f(qn.value) , qn.one, qn.two, qn.three, qn.four)
      case (ql:QLeaf[Coords, Section]) =>
        QLeaf( f(ql.value._1), ql.value._2 )
      case _ => qt
    }
  }

  def samePoints(a: Point, b: Point): Boolean = (a._1==b._1) && (a._2==b._2)

  def rotateD (qt:QTree[Coords]):QTree[Coords] = ???
  def rotateR (qt:QTree[Coords]):QTree[Coords] = ???


  def mapColourEffect (f:Color => Color, qt:QTree[Coords]):QTree[Coords] = {
    qt match {
      case ql: QLeaf[Coords, Section] => QLeaf((ql.value._1, f(ql.value._2)))
      case qn: QNode[Coords] => {
        val (t1,t2,t3,t4) = (mapColourEffect(f, qn.one), mapColourEffect(f, qn.two), mapColourEffect(f, qn.three), mapColourEffect(f, qn.four))
        QNode(qn.value, t1, t2, t3, t4)
      }
      case _ => qt
    }
  }

  def noiseEffect (color: Color): Color ={
    if(Math.random()>=0.5) {
      Color.gray
    } else
      color
  }

  def contrastEffect (color: Color): Color ={
    if(ImageUtil.luminance(color.getRed, color.getGreen, color.getBlue) >= 128)
      color.brighter()
    else
      color.darker()
  }

  def sepiaEffect (color: Color): Color ={
    val red= Math.min((.393 * color.getRed) + (.769 * color.getGreen) + (.189 * color.getBlue), 255.0).toInt
    val green= Math.min((.349 * color.getRed) + (.686 * color.getGreen) + (.168 * color.getBlue), 255.0).toInt
    val blue= Math.min((.272 * color.getRed) + (.534 * color.getGreen) + (.131 * color.getBlue), 255.0).toInt
    new Color(red, green, blue)
  }
}

/*
  def auxMirrorV(qt:QTree[Coords], height: Int): QTree[Coords] = {
    qt match {
      case (ql: QLeaf[Coords, Section]) => qt
      case (qn: QNode[Coords]) => {
        val (t1, t2, t3, t4) = (auxMirrorV(qn.one, height), auxMirrorV(qn.two, height), auxMirrorV(qn.three, height), auxMirrorV(qn.four, height))
        val (one, two, three, four) = (updateCoordsV(t1, height), updateCoordsV(t2, height), updateCoordsV(t3, height), updateCoordsV(t4, height))
        QNode(qn.value, three, four, one, two)
      }
    }
  }
*/

/* def updateCoordsV(qt: QTree[Coords], height: Int): QTree[Coords] = {
   qt match {
     case (qn:QNode[Coords]) =>
       QNode( ((qn.value._1._1, height-qn.value._2._2), (qn.value._2._1, height-qn.value._1._2)), qn.one, qn.two, qn.three, qn.four)
     case (ql:QLeaf[Coords, Section]) => {
       QLeaf( (  ((ql.value._1._1._1, height-ql.value._1._2._2), (ql.value._1._2._1, height-ql.value._1._1._2))  , ql.value._2 ))
     }
   }
 }*/

/**
 * T1. makeQTree(b:BitMap):QTree criação de uma quadtree a partir de um bitmap
 * fornecido e método oposto i.e. para transformar uma quadtree num bitmap;
 * T2. scale(scale:Double, qt:QTree):QTree operação de ampliação/redução de
 * uma imagem, segundo o fator fornecido (por exemplo 1.5 ampliará a imagem
 * aumentando ambos os seus lados em 50%);
 * T3. mirrorV / mirrorH (qt:QTree):QTree operações de espelhamento vertical e
 * horizontal;
 * T4. rotateD / rotateR (qt:QTree):QTree operações de rotação de 90 graus nos
 * dois sentidos;
 * T5. mapColourEffect(f:Colour => Colour, qt:QTree):QTree mapeamento
 * uniforme de uma função em toda a imagem. Deverá utilizar este método para ilustrar
 * a aplicação dos efeitos Noise, Contrast e Sepia.
 */