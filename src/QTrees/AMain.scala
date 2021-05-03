package QTrees

object AMain {

  def main(args: Array[String]): Unit = {

    /*
    //Exemplo 1 de QTree[Coords] (4 pixeis)
    val l1: QLeaf[Coords, Section] = QLeaf((((0,0):Point,(0,0):Point):Coords, Color.red):Section)
    val l2: QLeaf[Coords, Section] = QLeaf((((1,0):Point,(1,0):Point):Coords, Color.blue):Section)
    val l3: QLeaf[Coords, Section] = QLeaf((((0,1):Point,(0,1):Point):Coords, Color.yellow):Section)
    val l4: QLeaf[Coords, Section] = QLeaf((((1,1):Point,(1,1):Point):Coords, Color.green):Section)

    val qt: QTree[Coords] = QNode(((0,0),(1,1)), l1, l2, l3, l4)

    val quadTree = QuadTree(qt)


    //Exemplo 2 de QTree[Coords] (4 quadrantes com um repartido em 4 pixeis)
    val n1 = QLeaf((((0,0):Point,(0,0):Point):Coords, Color.red):Section)
    val n2 = QLeaf((((1,0):Point,(1,0):Point):Coords, Color.blue):Section)
    val n3 = QLeaf((((0,1):Point,(0,1):Point):Coords, Color.yellow):Section)
    val n4 = QLeaf((((1,1):Point,(1,1):Point):Coords, Color.green):Section)
    val l1: QNode[Coords] = QNode( ((0,0):Point,(1,1):Point):Coords, n1, n2, n3, n4 )

    val l2: QLeaf[Coords, Section] = QLeaf((((2,0):Point,(3,1):Point):Coords, Color.blue):Section)
    val l3: QLeaf[Coords, Section] = QLeaf((((0,2):Point,(1,3):Point):Coords, Color.yellow):Section)
    val l4: QLeaf[Coords, Section] = QLeaf((((2,2):Point,(3,3):Point):Coords, Color.green):Section)

    val qt: QTree[Coords] = QNode( ((0,0),(3,3)), l1, l2, l3, l4 )

    val quadTree = QuadTree(qt)


    val c = BitMap.makeQTree("src//evolution.png")

    println("QTree: " + c)
    //println("")

    val quadTree = QuadTree(c)

    //println("MirrorV: " + quadTree.mirrorV())
    //println("MirrorH: " + quad.mirrorH())
    //println("Sepia: " + quadTree.mapColourEffect(QuadTree.sepiaEffect))
    //println("Noise: " + quad.mapColourEffect(QuadTree.noiseEffect))
    //println("Contrast: " + quad.mapColourEffect(QuadTree.contrastEffect))


    val n = QuadTree(quadTree.scale(.25))
    //val b = n.makeBitMap()
    //val w = QuadTree(QuadTree(b.makeQTree()).rotateR())
    //val m = QuadTree(w.scale(4))
    val o = QuadTree(n.rotateR())

    println()
    println(o.qt)

    val image = o.makeBitMap()

    image.toImage("src//a.png", "png")


     */
    val c = BitMap.makeQTree("img//evolution.png")

    println("QTree: " + c)

    val quadTree = QuadTree(c)
    val n = QuadTree(quadTree.scale(.25))
    val image = n.makeBitMap()
    image.bitmapWriteImage("img//a.png")


  }
}
