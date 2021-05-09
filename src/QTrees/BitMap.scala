package QTrees

import QuadTree.Coords
import QuadTree.Section
import java.awt.Color

case class BitMap(img: List[List[Int]]) {
  def makeQTree(): QTree[Coords] = BitMap.makeQTree(this)
  def bitmapWriteImage(path: String): Unit = BitMap.bitmapWriteImage(this.img, path)
}

object BitMap {

  /**
   * Writes a given BitMap (List[List[Int]]) into an image in a given path.
   * @param img the list of lists of int (BitMap)
   * @param path the image path
   */
  def bitmapWriteImage(img: List[List[Int]], path: String): Unit = {
    val matrix = (img map (x=>x.toArray)).toArray
    val tokens = path.split('.')
    ImageUtil.writeImage(matrix, path, tokens.last)
  }

  /**
   * Returns a QTree given an image path
   * @param path the image path
   * @return QTree corresponding to the image specified
   */
  def makeQTree(path: String): QTree[Coords] = {
    val lst = ImageUtil.readColorImage(path).toList map (x=>x.toList)
    BitMap(lst).makeQTree()
  }

  /**
   * Returns a QTree given a BitMap. Uses a recursive auxiliary method.
   * @param b the BitMap
   * @return Qtree corresponding to the BitMap indicated
   */
  def makeQTree(b:BitMap): QTree[Coords] = {

    /**
     * Recursive method that constructs the QTree by splitting the BitMap until it reaches the pixel level. In the pixel
     * level it starts turning adjacent pixels of the same color into QLeafs and adjacent pixels of different colors in
     * QNodes.
     * @param c the coordinates
     * @param b the BitMap
     * @return
     */
    def auxMQT(c:Coords, b: BitMap): QTree[Coords] = {

      // Verifies if the coordinates correspond to only one pixel of the given image (BitMap), if so,
      // returns a new QLeaf with its color
      if( (c._1._1 == c._2._1) && (c._1._2 == c._2._2) ) {
        val color = new Color(b.img(c._1._2)(c._1._1))
        return QLeaf[Coords, Section]( (c, color):Section )
      }

      // Split the coordinates into 4 quadrants
      val sCords = splitCoords(c)

      // Verifies if the returned coordinates are valid.
      // If they're valid, recursively applies the same method. If they're not valid, returns a QEmpty.
      val qtOne   = if(coordsInbounds(c, sCords._1)) auxMQT(sCords._1, b) else QEmpty
      val qtTwo   = if(coordsInbounds(c, sCords._2)) auxMQT(sCords._2, b) else QEmpty
      val qtThree = if(coordsInbounds(c, sCords._3)) auxMQT(sCords._3, b) else QEmpty
      val qtFour  = if(coordsInbounds(c, sCords._4)) auxMQT(sCords._4, b) else QEmpty

      // Verifies if all quadrants correspond to QLeafs with the same color and if so it returns the corresponding QLeaf.
      // Otherwise it returns a QNode containing the 4 QTrees.
      (qtOne, qtTwo, qtThree, qtFour) match {
        case (q1: QLeaf[Coords, Section], q2:QLeaf[Coords, Section], QEmpty, QEmpty) =>
          if(q1.value._2 equals q2.value._2)
            new QLeaf[Coords, Section]( ((q1.value._1._1, q2.value._1._2), q1.value._2) )
          else
            new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)

        case (q1: QLeaf[Coords, Section], QEmpty, q3:QLeaf[Coords, Section], QEmpty) =>
          if(q1.value._2 equals q3.value._2)
            new QLeaf[Coords, Section]( ((q1.value._1._1, q3.value._1._2), q1.value._2) )
          else
            new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)

        case (q1: QLeaf[Coords, Section], q2:QLeaf[Coords, Section], q3:QLeaf[Coords, Section], q4:QLeaf[Coords, Section]) =>
          if( (q1.value._2 equals q2.value._2) && (q3.value._2 equals q4.value._2) && (q1.value._2 equals q4.value._2 ) )
            new QLeaf[Coords, Section]( (c, q1.value._2) )
          else
            new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)
        case _ => new QNode[Coords](c, qtOne, qtTwo, qtThree, qtFour)
      }
    }

    auxMQT( ((0,0), (b.img.head.length - 1,b.img.length - 1)), b )
  }

  /**
   * Returns 4 new coordinates that are the split of the given coordinates.
   * In our split we tend to privilege the upper left quadrant.
   * @param c the coordinates
   * @return
   */
  def splitCoords(c:Coords): (Coords, Coords, Coords, Coords) = {
    val width = c._2._1 - c._1._1 + 1
    val height = c._2._2 - c._1._2 + 1

    val c1 = ( c._1, (c._1._1+(width/2.0).ceil.toInt-1, c._1._2+(height/2.0).ceil.toInt-1) )
    val c2 = ( (c._1._1+(width/2.0).ceil.toInt, c._1._2),(c._2._1, c._1._2+(height/2.0).ceil.toInt-1) )
    val c3 = ( (c._1._1, c._1._2+(height/2.0).ceil.toInt),(c._2._1-(width/2.0).floor.toInt, c._2._2) )
    val c4 = ( (c._1._1+(width/2.0).ceil.toInt, c._1._2+(height/2.0).ceil.toInt), c._2 )
    (c1,c2,c3,c4)
  }

  /**
   * Checks if a given set of coordinates is inside of another given set of coordinates.
   * Helpful to discover invalid coordinates in construction of a QTree. Ex: when QTrees should be QEmptys.
   * @param bound the coordinates bound
   * @param coords the coordinates to be tested
   * @return returns true if the coordinates are inbounds, otherwise returns false
   */
  def coordsInbounds(bound:Coords, coords:Coords): Boolean = {
    val (boundTopX,boundTopY) = bound._1
    val (boundBotX,boundBotY) = bound._2
    val (coordsTopX,coordsTopY) = coords._1
    val (coordsBotX,coordsBotY) = coords._2

    (boundTopX<=coordsTopX) && (boundTopX<=coordsBotX) && (boundBotX>=coordsTopX) && (boundBotX>=coordsBotX) &&
      (boundTopY<=coordsTopY) && (boundTopY<=coordsBotY) && (boundBotY>=coordsTopY) && (boundBotY>=coordsBotY)
  }

  /**
   * Returns a combined BitMap from 4 given BitMaps.
   * @param b1 the 1st BitMap
   * @param b2 the 2nd BitMap
   * @param b3 the 3rd BitMap
   * @param b4 the 4th BitMap
   * @return
   */
  def combine(b1:BitMap, b2:BitMap, b3:BitMap, b4:BitMap): BitMap = {
    def auxCombine(lst1: List[List[Int]], lst2: List[List[Int]]): List[List[Int]] = {
      (lst1,lst2) match {
        case (Nil, Nil) => Nil
        case (l, Nil) => l
        case (Nil, l) => l
        case (h1::t1, h2::t2) => (h1:::h2)::auxCombine(t1,t2)
        case _ => throw new IllegalArgumentException("ERROR: BitMap has invalid dimensions.")
      }
    }
    BitMap(auxCombine(b1.img, b2.img):::auxCombine(b3.img, b4.img))
  }
}