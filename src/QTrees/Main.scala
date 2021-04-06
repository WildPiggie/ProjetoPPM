package QTrees

object Main {
  def main(args: Array[String]): Unit = {
    // testar mais coisas

    /*
    *
    * //Exemplo de QTree[Coords]

       val l1: QLeaf[Coords, Section] = QLeaf((((0,0):Point,(0,0):Point):Coords, Color.red):Section)
       val l2: QLeaf[Coords, Section] = QLeaf((((1,0):Point,(1,0):Point):Coords, Color.blue):Section)
       val l3: QLeaf[Coords, Section] = QLeaf((((0,1):Point,(0,1):Point):Coords, Color.yellow):Section)
       val l4: QLeaf[Coords, Section] = QLeaf((((1,1):Point,(1,1):Point):Coords, Color.green):Section)

       val qt: QTree[Coords] = QNode(((0,0),(1,1)), l1, l2, l3, l4)
* */

    // List(List(1ª linha), List(2ª linha), ...)
    val a = ImageUtil.readColorImage("C:\\Users\\Pombo\\Desktop\\objc2_2.png").toList

    //print(a)

    println(a(1)(2))
    val b = a map (x=>x.toList)




  }
}
