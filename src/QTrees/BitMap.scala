package QTrees

import QuadTree.Point
import QuadTree.Coords
import QuadTree.Section
import java.awt.Color

case class BitMap(img: List[List[Int]]) {
  def makeQTree(): QTree[Coords] = BitMap.makeQTree(this)
  def toImage(path: String, format: String): Unit = BitMap.toImage(this.img, path, format)
}

object BitMap {

  def toImage(img: List[List[Int]], path: String, format: String): Unit = {
    val matrix = (img map (x=>x.toArray)).toArray
    ImageUtil.writeImage(matrix, path, format)
  }

  def makeQTree(filename: String): QTree[Coords] = {
    val lst = ImageUtil.readColorImage(filename).toList map (x=>x.toList)
    BitMap(lst).makeQTree()
  }

  // Função para a criação de uma QTree a partir de um BitMap
  def makeQTree(b:BitMap): QTree[Coords] = auxMQT( ((0,0), (b.img.head.length - 1,b.img.length - 1)), b )

  // Calcula a largura e altura de imagem para obter as coordenadas da QTree que servirá como raiz da imagem
  def auxMQT(c:Coords, b: BitMap): QTree[Coords] = {

    // Verifica se a QTree corresponde apenas a um pixel da imagem original
    // e caso o seja devolve uma QLeaf com a cor correspondente
    if( (c._1._1 == c._2._1) && (c._1._2 == c._2._2) ) {
      val color = new Color(b.img(c._1._2)(c._1._1))
      return QLeaf[Coords, Section]( (c, color):Section )
    }

    // Divisão das coordenadas em 4 quadrantes
    val sCords = splitCoords(c)

    // Verifica se as coordenadas devolvidas são válidas.
    // Caso sejam é realizado o mesmo processo para o quadrante devolvido, ou então é indicado como sendo QEmpty
    val qtOne   = if(coordsInbounds(c, sCords._1)) auxMQT(sCords._1, b) else QEmpty
    val qtTwo   = if(coordsInbounds(c, sCords._2)) auxMQT(sCords._2, b) else QEmpty
    val qtThree = if(coordsInbounds(c, sCords._3)) auxMQT(sCords._3, b) else QEmpty
    val qtFour  = if(coordsInbounds(c, sCords._4)) auxMQT(sCords._4, b) else QEmpty

    // Verifica se todos os quadrantes correspondem a QLeafs com a mesma cor, se sim é devolvida a QLeaf correspondente ao caso.
    // Caso contrário é devolvido uma QNode contendo as quatro QTrees
    (qtOne, qtTwo, qtThree, qtFour) match {
      case (q1: QLeaf[Coords, Section], q2:QLeaf[Coords, Section], QEmpty, QEmpty) => {
        if(q1.value._2 equals q2.value._2)
          new QLeaf[Coords, Section]( ((q1.value._1._1, q2.value._1._2), q1.value._2) )
        else
          new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)
      }
      case (q1: QLeaf[Coords, Section], QEmpty, q3:QLeaf[Coords, Section], QEmpty) => {
        if(q1.value._2 equals q3.value._2)
          new QLeaf[Coords, Section]( ((q1.value._1._1, q3.value._1._2), q1.value._2) )
        else
          new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)
      }
      case (q1: QLeaf[Coords, Section], q2:QLeaf[Coords, Section], q3:QLeaf[Coords, Section], q4:QLeaf[Coords, Section]) =>
        if( (q1.value._2 equals q2.value._2) && (q3.value._2 equals q4.value._2) && (q1.value._2 equals q4.value._2 ) )
          new QLeaf[Coords, Section]( (c, q1.value._2) )
        else
          new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)
      case _ => new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)
    }
  }

  // Função para repartir as coordenadas dadas em quatro coordenadas
  // correspondendo a quatro quadrantes da imagem
  def splitCoords(c:Coords): (Coords, Coords, Coords, Coords) = {

    val width = c._2._1 - c._1._1 + 1
    val height = c._2._2 - c._1._2 + 1

    val c1 = ( c._1, (c._1._1+(width/2.0).ceil.toInt-1, c._1._2+(height/2.0).ceil.toInt-1) )
    val c2 = ( (c._1._1+(width/2.0).ceil.toInt, c._1._2),(c._2._1, c._1._2+(height/2.0).ceil.toInt-1) )
    val c3 = ( (c._1._1, c._1._2+(height/2.0).ceil.toInt),(c._2._1-(width/2.0).floor.toInt, c._2._2) )
    val c4 = ( (c._1._1+(width/2.0).ceil.toInt, c._1._2+(height/2.0).ceil.toInt), c._2 )
    (c1,c2,c3,c4)
  }

  def combine(b1:BitMap, b2:BitMap, b3:BitMap, b4:BitMap): BitMap = {

    def auxCombine(lst1: List[List[Int]], lst2: List[List[Int]]): List[List[Int]] = {
      (lst1,lst2) match {
        case (Nil, Nil) => Nil
        case (l, Nil) => l
        case (Nil, l) => l
        case (h1::t1, h2::t2) => {
          (h1:::h2)::auxCombine(t1,t2)
        }
        case _ => {
          //Se entrar aqui é erro! Nunca devia acontecer! (a altura dos bitMaps deve ser igual)
          throw new IllegalArgumentException("ERROR: BitMap has invalid dimensions.")
        }
      }
    }

    BitMap(auxCombine(b1.img, b2.img):::auxCombine(b3.img, b4.img))
  }

  //Função aninhada para indicar se umas coordenadas estão contidas noutras
  // (útil para descobrir coordenadas inválidas, usado para descobrir que QTrees devem ser QEmptys)
  def coordsInbounds(bound:Coords, coords:Coords): Boolean = {
    val (boundTopX,boundTopY) = bound._1
    val (boundBotX,boundBotY) = bound._2
    val (coordsTopX,coordsTopY) = coords._1
    val (coordsBotX,coordsBotY) = coords._2

    (boundTopX<=coordsTopX) && (boundTopX<=coordsBotX) && (boundBotX>=coordsTopX) && (boundBotX>=coordsBotX) &&
    (boundTopY<=coordsTopY) && (boundTopY<=coordsBotY) && (boundBotY>=coordsTopY) && (boundBotY>=coordsBotY)
  }
}
