package QTrees

import java.awt.Color

class QuadTree{

  type BitMap = List[List[Int]]
  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  def makeQTree (b:BitMap):QTree[Color] = {

    ???
  }
  def makeBitMap (qt: QTree[Color]): BitMap = ???
  def scale(scale:Double, qt:QTree[Color]):QTree[Color] = ???
  def mirrorV (qt:QTree[Color]):QTree[Color] = ???
  def mirrorH (qt:QTree[Color]):QTree[Color] = ???
  def rotateD (qt:QTree[Color]):QTree[Color] = ???
  def rotateR (qt:QTree[Color]):QTree[Color] = ???
  def mapColourEffect (f:Color => Color, qt:QTree[Color]):QTree[Color] = ???
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
