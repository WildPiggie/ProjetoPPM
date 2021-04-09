package QTrees
import QTrees.QuadTree._

import java.awt.Color

object Main {
  def main(args: Array[String]): Unit = {
    // testar mais coisas



    //Exemplo de QTree[Coords]
/*

       val l1: QLeaf[Coords, Section] = QLeaf((((0,0):Point,(0,0):Point):Coords, Color.red):Section)
       val l2: QLeaf[Coords, Section] = QLeaf((((1,0):Point,(1,0):Point):Coords, Color.blue):Section)
       val l3: QLeaf[Coords, Section] = QLeaf((((0,1):Point,(0,1):Point):Coords, Color.yellow):Section)
       val l4: QLeaf[Coords, Section] = QLeaf((((1,1):Point,(1,1):Point):Coords, Color.green):Section)

       val qt: QTree[Coords] = QNode(((0,0),(1,1)), l1, l2, l3, l4)
*/


    // List(List(1ª linha), List(2ª linha), ...)
   val a = ImageUtil.readColorImage("src//black6x3.png").toList

    //print(a)

    val b = a map (x=>x.toList)

    val c = BitMap(b).makeQTree()

    println("QTree: " + c)
    println("")


    val quad = QuadTree(c)

    println("MirrorV: " + quad.mirrorV())
/*



    val n1 = QLeaf((((0,0):Point,(0,0):Point):Coords, Color.red):Section)
    val n2 = QLeaf((((1,0):Point,(1,0):Point):Coords, Color.blue):Section)
    val n3 = QLeaf((((0,1):Point,(0,1):Point):Coords, Color.yellow):Section)
    val n4 = QLeaf((((1,1):Point,(1,1):Point):Coords, Color.green):Section)
    val l1: QNode[Coords] = QNode( ((0,0):Point,(1,1):Point):Coords, n1, n2, n3, n4 )


    val l2: QLeaf[Coords, Section] = QLeaf((((2,0):Point,(3,1):Point):Coords, Color.blue):Section)
    val l3: QLeaf[Coords, Section] = QLeaf((((0,2):Point,(1,3):Point):Coords, Color.yellow):Section)
    val l4: QLeaf[Coords, Section] = QLeaf((((2,2):Point,(3,3):Point):Coords, Color.green):Section)

    val qt: QTree[Coords] = QNode( ((0,0),(3,3)), l1, l2, l3, l4 )
*/

    //println("MirrorV: " + quad.mirrorV())
    //println("Noise: " + quad.mapColourEffect(QuadTree.noiseEffect))


  }
}
