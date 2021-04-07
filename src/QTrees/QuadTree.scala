package QTrees

import java.awt.Color
import QTrees.QuadTree._

case class QuadTree(qt: QTree[Coords]){

  def makeBitMap (): BitMap = QuadTree.makeBitMap(this.qt)
  def scale (scale:Double, quadT:QTree[Coords]):QTree[Coords] = ???
  def mirrorV ():QTree[Coords] = QuadTree.mirrorV(this.qt)
  def mirrorH (quadT:QTree[Coords]):QTree[Coords] = ???
  def rotateD (quadT:QTree[Coords]):QTree[Coords] = ???
  def rotateR (quadT:QTree[Coords]):QTree[Coords] = ???
  def mapColourEffect (f:Color => Color, quadT:QTree[Coords]):QTree[Coords] = ???

}

object QuadTree{

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  def makeBitMap (qt: QTree[Coords]): BitMap = ???
  def scale (scale:Double, qt:QTree[Coords]): QTree[Coords] = ??? //verificar se é possível realizar o scaling ou não
  def mirrorV (qt:QTree[Coords]):QTree[Coords] = {

    qt match {
      case (ql: QLeaf[Coords, Section]) => return qt
      case (qn: QNode[Coords]) => {
        val t1 = mirrorV(qn.one)
        val t2 = mirrorV(qn.two)
        val t3 = mirrorV(qn.three)
        val t4 = mirrorV(qn.four)
        val (one, three) = switchQTrees(t1,t3)
        val (two, four) = switchQTrees(t2,t4)
        QNode( qn.value, three, four, one, two)
      }
    }
  }
  def mirrorH (qt:QTree[Coords]):QTree[Coords] = ???
  def rotateD (qt:QTree[Coords]):QTree[Coords] = ???
  def rotateR (qt:QTree[Coords]):QTree[Coords] = ???
  def mapColourEffect (f:Color => Color, qt:QTree[Coords]):QTree[Coords] = ???

  def switchQTrees(qt1: QTree[Coords], qt2: QTree[Coords]): (QTree[Coords], QTree[Coords]) = {
    (qt1,qt2) match {
      case (qn1:QNode[Coords], qn2:QNode[Coords]) =>
        ( QNode(qn2.value, qn1.one, qn1.two, qn1.three, qn1.four), QNode(qn1.value, qn2.one, qn2.two, qn2.three, qn2.four) )
      case (qn:QNode[Coords], ql:QLeaf[Coords, Section]) =>
        ( QNode(ql.value._1, qn.one, qn.two, qn.three, qn.four), QLeaf( (qn.value,ql.value._2) ))
      case (ql:QLeaf[Coords, Section], qn:QNode[Coords]) =>
        ( QLeaf( (qn.value,ql.value._2) ), QNode(ql.value._1, qn.one, qn.two, qn.three, qn.four) )
      case (ql1:QLeaf[Coords, Section], ql2:QLeaf[Coords, Section]) =>
        ( QLeaf( (ql2.value._1, ql1.value._2) ) , QLeaf( (ql1.value._1, ql2.value._2) ) )
      //case (q1, QEmpty) => (QEmpty, q1)   adicionar casos :)
      case _ => (QEmpty, QEmpty)
    }
  }
}



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
