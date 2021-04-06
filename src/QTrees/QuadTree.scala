package QTrees

import java.awt.Color
import QTrees.QuadTree._

case class QuadTree(qt: QTree[Coords]){

  def makeBitMap (quadT: QTree[Coords]): BitMap = QuadTree.makeBitMap(qt) //correto?
  def scale (scale:Double, quadT:QTree[Coords]):QTree[Coords] = ???
  def mirrorV (quadT:QTree[Coords]):QTree[Coords] = ???
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
  def scale (scale:Double, qt:QTree[Coords]):QTree[Coords] = ???
  def mirrorV (qt:QTree[Coords]):QTree[Coords] = ???
  def mirrorH (qt:QTree[Coords]):QTree[Coords] = ???
  def rotateD (qt:QTree[Coords]):QTree[Coords] = ???
  def rotateR (qt:QTree[Coords]):QTree[Coords] = ???
  def mapColourEffect (f:Color => Color, qt:QTree[Coords]):QTree[Coords] = ???
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
