package QTrees

import java.awt.Color
import QTrees.QuadTree._

case class QuadTree(qt: QTree[Coords]){

  def makeBitMap (): BitMap = QuadTree.makeBitMap(this.qt)
  def scale (scale:Double):QTree[Coords] = QuadTree.scale(scale)(this.qt)
  def mirrorV ():QTree[Coords] = QuadTree.mirrorV(this.qt)
  def mirrorH ():QTree[Coords] = QuadTree.mirrorH(this.qt)
  def rotateR ():QTree[Coords] = QuadTree.rotateR(this.qt)
  def rotateL ():QTree[Coords] = QuadTree.rotateL(this.qt)
  def mapColourEffect (f:Color => Color):QTree[Coords] = QuadTree.mapColourEffect(f)(this.qt)
  def mapColourEffectWithState(f:(Color,RandomWithState) => (Color,RandomWithState)):QTree[Coords] = QuadTree.mapColourEffectWithState(f)(this.qt)
  def sepiaColourEffect(): QTree[Coords] = QuadTree.mapColourEffect(sepiaEffect)(this.qt)
  def contrastColourEffect(): QTree[Coords] = QuadTree.mapColourEffect(contrastEffect)(this.qt)
  def noiseImpureColourEffect(): QTree[Coords] = QuadTree.mapColourEffect(noiseEffect)(this.qt)
  def noisePureColourEffect(): QTree[Coords] = QuadTree.mapColourEffectWithState(noiseEffectWithState)(this.qt)
  def toImage(filename: String, format: String): QTree[Coords] = QuadTree.toImage(filename, format)(this.qt)
}

object QuadTree{

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  def makeBitMap (qt: QTree[Coords]): BitMap = {
    qt match {
      case (qn:QNode[Coords]) => BitMap.combine(makeBitMap(qn.one), makeBitMap(qn.two), makeBitMap(qn.three), makeBitMap(qn.four))
      case (ql:QLeaf[Coords, Section]) => {
        val lst = List.fill(ql.value._1._2._1 - ql.value._1._1._1 + 1)(ql.value._2.getRGB)
        val sectionLst = List.fill(ql.value._1._2._2 - ql.value._1._1._2 + 1)(lst)
        new BitMap(sectionLst)
      }
      case _ => new BitMap(Nil)
    }
  }

  //Guarda a QTree como imagem no endereço indicado e devolve a QTree (usado no menu textual)
  def toImage(path: => String, format: => String)(qt: QTree[Coords]): QTree[Coords] = {
    val image = makeBitMap(qt)
    //verificar se format valido
    image.toImage(path, format)
    qt
  }

  def averageColour(qt: QTree[Coords]):Option[Color] = {
    def avgRGB(lst: List[Option[Color]]): Color = {
      val definedColours = (lst filter (x => x.isDefined)) map (aux => (aux.get.getRed, aux.get.getGreen, aux.get.getBlue) )
      val size = definedColours.length
      val sum = (definedColours foldRight (0,0,0)) ( (h,t) => (h._1+t._1,h._2+t._2,h._3+t._3))
      new Color(sum._1/size,sum._2/size,sum._3/size)
    }

    qt match {
      case qn: QNode[Coords] => Some(avgRGB(List(averageColour(qn.one),averageColour(qn.two),averageColour(qn.three),averageColour(qn.four))))
      case ql: QLeaf[Coords, Section] => Some(ql.value._2)
      case _ => None
    }
  }

  def scale (scale: => Double)(qt:QTree[Coords]):QTree[Coords] = {

    def aux(width: Int, height: Int, upperLeft: Point, qtAux:QTree[Coords]): QTree[Coords] = {
      qtAux match {
        case qn: QNode[Coords] => {
          val (widthQ1, heightQ1) = ((width/2.0).ceil.toInt, (height/2.0).ceil.toInt)
          val (widthQ2, heightQ2) = ((width/2.0).floor.toInt, heightQ1)
          val (widthQ3, heightQ3) = (widthQ1, (height/2.0).floor.toInt)
          val (widthQ4, heightQ4) = (widthQ2, heightQ3)

          val newCoords = ( upperLeft, (upperLeft._1 + width - 1, upperLeft._2 + height - 1) )

          if( (newCoords._1._1==newCoords._2._1) && (newCoords._1._2==newCoords._2._2) || (width == 1 || height == 1)  )
            return QLeaf(newCoords, averageColour(qn).get)

          //println(newCoords)

          if(qn.one == QEmpty && qn.two == QEmpty){
            val q3 = aux(widthQ3,heightQ3+heightQ1,upperLeft,qn.three)
            val q4 = aux(widthQ4,heightQ4+heightQ2,(upperLeft._1+widthQ3,upperLeft._2),qn.four)
            QNode(newCoords,QEmpty,QEmpty,q3,q4)
          }
          else if(qn.two == QEmpty && qn.four == QEmpty) {
            val q1 = aux(widthQ1+widthQ2,heightQ1,upperLeft,qn.one)
            val q3 = aux(widthQ3+widthQ4,heightQ3,(upperLeft._1,upperLeft._2+heightQ1),qn.three)
            QNode(newCoords,q1,QEmpty,q3,QEmpty)
          }
          else if(qn.four == QEmpty && qn.three == QEmpty) {
            val q1 = aux(widthQ1,heightQ1+heightQ3,upperLeft,qn.one)
            val q2 = aux(widthQ2,heightQ2+heightQ4,(upperLeft._1+widthQ1,upperLeft._2),qn.two)
            QNode(newCoords,q1,q2,QEmpty,QEmpty)
          }
          else if(qn.three == QEmpty && qn.one == QEmpty) {
            val q2 = aux(widthQ2+widthQ1,heightQ2,upperLeft,qn.two)
            val q4 = aux(widthQ4+widthQ3,heightQ4,(upperLeft._1,upperLeft._2+heightQ2),qn.four)
            QNode(newCoords,QEmpty,q2,QEmpty,q4)
          }
          else{
            val q1 = aux(widthQ1,heightQ1,upperLeft,qn.one)
            val q2 = aux(widthQ2,heightQ2,(upperLeft._1+widthQ1,upperLeft._2),qn.two)
            val q3 = aux(widthQ3,heightQ3,(upperLeft._1,upperLeft._2+heightQ1),qn.three)
            val q4 = aux(widthQ4,heightQ4,(upperLeft._1+widthQ1,upperLeft._2+heightQ1),qn.four)
            QNode(newCoords,q1,q2,q3,q4)
          }
        }
        case (ql: QLeaf[Coords, Section]) => {
          val newCoords = (upperLeft, (upperLeft._1 + width - 1 , upperLeft._2 + height - 1))
          //println("Leaf: " + newCoords)
          //BitMap.coordsInbounds(ql.value._1, newCoords)
          QLeaf(newCoords,ql.value._2)
        }
        case _ => qt
      }
    }

    val scaleCall = scale //Para lidar com a lazy evaluation (para o menu textual)
    if(scaleCall <= 0)
      throw new IllegalArgumentException("Invalid scale value.")

    if(scaleCall == 1.0)
      qt
    else {
      qt match {
        case qn: QNode[Coords] => {
          val width = qn.value._2._1 - qn.value._1._1 + 1
          val height = qn.value._2._2 - qn.value._1._2 + 1
          val newWidth = (width * scaleCall).toInt
          val newHeight = (height * scaleCall).toInt
          if (newWidth == width || newHeight == height) {
            IO_Utils.printMessage("ERROR: Scale value too small.")
            qn
          } else if(newWidth == 0 || newHeight == 0){
            IO_Utils.printMessage("ERROR. Scale reduces too much.")
            qn
          }
          else
          aux(newWidth,newHeight,qn.value._1,qn)
        }
        case ql: QLeaf[Coords, Section] => {
          val width = ql.value._1._2._1 - ql.value._1._1._1 + 1
          val height = ql.value._1._2._2 - ql.value._1._1._2 + 1
          val newWidth = (width * scaleCall).toInt
          val newHeight = (height * scaleCall).toInt
          if (newWidth == width || newHeight == height) {
            println("ERROR. Scale value too small.")
            return ql
          } else if(newWidth == 0 || newHeight == 0){
            println("ERROR. Scale reduces too much.")
            return ql
          }
          aux(newWidth,newHeight,ql.value._1._1,ql)
        }
        case _ => qt
      }
    }
  }

  // Realiza o espelhamento vertical da imagem (sob o eixo horizontal)
  def mirrorV (qt:QTree[Coords]):QTree[Coords] = {
    qt match {
      case (qn: QNode[Coords]) => {
        val height = qn.value._2._2 - qn.value._1._2

        // Função para atualização das coordenadas
        def newCoords(coords: Coords): Coords = ( (coords._1._1, height-coords._2._2), (coords._2._1, height-coords._1._2) )

        // Função que troca a ordem das QTrees numa QNode
        def switchQTreeOrder(qn: QNode[Coords]): QNode[Coords] = QNode(qn.value, qn.three, qn.four, qn.one, qn.two)

        recursiveSwapper(qn, newCoords, switchQTreeOrder)
      }
      case _ => qt
    }
  }

  // Realiza o espelhamento horizontal da imagem (sob o eixo vertical)
  def mirrorH (qt:QTree[Coords]):QTree[Coords] = {
    qt match {
      case (qn: QNode[Coords]) => {
        val width = qn.value._2._1 - qn.value._1._1

        def newCoords(coords: Coords): Coords = ( (width-coords._2._1, coords._1._2), (width-coords._1._1, coords._2._2) )

        def switchQTreeOrder(qn: QNode[Coords]): QNode[Coords] = QNode(qn.value, qn.two, qn.one, qn.four, qn.three)

        recursiveSwapper(qn, newCoords, switchQTreeOrder)
      }
      case _ => qt
    }
  }

  def rotateR (qt:QTree[Coords]):QTree[Coords] = {
    qt match {
      case (qn: QNode[Coords]) => {
        val width = qn.value._2._1 - qn.value._1._1
        val height = qn.value._2._2 - qn.value._1._2

        def newCoords(coords: Coords): Coords = {
          //centrar nos eixos x e y
          val xsupC = coords._1._1 - width/2.0
          val ysupC = coords._1._2 - height/2.0
          val xinfC = coords._2._1 - width/2.0
          val yinfC = coords._2._2 - height/2.0

          //rodar + descentrar dos eixos
          val x1 = ( (-ysupC) + height/2.0).toInt
          val y1 = ( xsupC + width/2.0).toInt
          val x2 = ( (-yinfC) + height/2.0).toInt
          val y2 = ( xinfC + width/2.0).toInt

          ((x1 min x2,y1 min y2),(x1 max x2,y1 max y2))
        }

        def switchQTreeOrder(qn: QNode[Coords]): QNode[Coords] = QNode(qn.value, qn.three, qn.one, qn.four, qn.two)

        val newQN = QNode((qn.value._1,(qn.value._2._2, qn.value._2._1)), qn.one, qn.two, qn.three, qn.four)

        recursiveSwapper(newQN, newCoords, switchQTreeOrder)
      }
      case _ => qt
    }
  }

  def rotateL (qt:QTree[Coords]):QTree[Coords] = {
    qt match {
      case (qn: QNode[Coords]) => {
        val width = qn.value._2._1 - qn.value._1._1
        val height = qn.value._2._2 - qn.value._1._2

        def newCoords(coords: Coords): Coords = {
          // centrar nos eixos x e y
          val xsupC = coords._1._1 - width/2.0
          val ysupC = coords._1._2 - height/2.0
          val xinfC = coords._2._1 - width/2.0
          val yinfC = coords._2._2 - height/2.0

          // rodar + descentrar dos eixos
          val x1 = ( ysupC + width/2.0).toInt
          val y1 = ( (-xsupC) + height/2.0).toInt
          val x2 = ( yinfC + width/2.0).toInt
          val y2 = ( (-xinfC) + height/2.0).toInt

          ((x1 min x2,y1 min y2),(x1 max x2,y1 max y2))
        }

        def switchQTreeOrder(qn: QNode[Coords]): QNode[Coords] = QNode(qn.value, qn.two, qn.four, qn.one, qn.three)

        val newQN = QNode((qn.value._1,(qn.value._2._2, qn.value._2._1)), qn.one, qn.two, qn.three, qn.four)

        recursiveSwapper(newQN, newCoords, switchQTreeOrder)
      }
      case _ => qt
    }
  }

  def recursiveSwapper(qt:QTree[Coords], f: Coords => Coords, switchQTreeOrder:QNode[Coords] => QNode[Coords]): QTree[Coords] = {

    //Função aninhada que transforma as coordenadas de uma QTree de acordo com uma função indicada
    def updateCoords(qt: QTree[Coords], f: Coords => Coords): QTree[Coords] = {
      qt match {
        case (qn:QNode[Coords]) => {
          //println(f(qn.value))
          QNode( f(qn.value) , qn.one, qn.two, qn.three, qn.four)
        }
        case (ql:QLeaf[Coords, Section]) => {
          //println(f(ql.value._1))
          QLeaf( f(ql.value._1), ql.value._2 )
        }
        case _ => qt
      }
    }

    qt match {
      case (qn: QNode[Coords]) => {
        val (m1, m2, m3, m4) = (recursiveSwapper(qn.one, f, switchQTreeOrder), recursiveSwapper(qn.two, f, switchQTreeOrder), recursiveSwapper(qn.three, f, switchQTreeOrder), recursiveSwapper(qn.four, f, switchQTreeOrder))

        //Realiza a atualização das coordenadas de todas as QTrees da QNode
        val (upd1, upd2, upd3, upd4) = ( updateCoords(m1, f), updateCoords(m2, f), updateCoords(m3, f), updateCoords(m4, f) )

        //Realiza a troca da ordem das QTrees da QNode de forma a reorganizá-la
        switchQTreeOrder( QNode(qn.value, upd1, upd2, upd3, upd4) )
      }
      case _ => qt
    }
  }


  // Função recursiva que realiza o efeito da função f sobre todas as cores (em todas as leafs) da QTree
  def mapColourEffect (f:Color => Color)(qt:QTree[Coords]):QTree[Coords] = {
    qt match {
      case ql: QLeaf[Coords, Section] => QLeaf((ql.value._1, f(ql.value._2)))
      case qn: QNode[Coords] => QNode(qn.value, mapColourEffect(f)(qn.one), mapColourEffect(f)(qn.two), mapColourEffect(f)(qn.three), mapColourEffect(f)(qn.four))
      case _ => qt
    }
  }

  // Efeito de ruído, devolve com 50% de probabilidade a cor cinzenta,
  // caso contrário a cor passada como parametro é devolvida
  def noiseEffect (color: Color): Color ={
    if(Math.random()>=0.5)
      Color.darkGray
    else
      color
  }

  // Dependendo da luminosidade da cor recebida, a função devolve uma cor mais clara ou mais escura
  def contrastEffect (color: Color): Color ={
    if(ImageUtil.luminance(color.getRed, color.getGreen, color.getBlue) >= 128)
      color.brighter()
    else
      color.darker()
  }

  // Aplica o efeito Sepia à cor recebida como argumento
  def sepiaEffect (color: Color): Color ={
    val red   = Math.min((.393 * color.getRed) + (.769 * color.getGreen) + (.189 * color.getBlue), 255.0).toInt
    val green = Math.min((.349 * color.getRed) + (.686 * color.getGreen) + (.168 * color.getBlue), 255.0).toInt
    val blue  = Math.min((.272 * color.getRed) + (.534 * color.getGreen) + (.131 * color.getBlue), 255.0).toInt
    new Color(red, green, blue)
  }


  // Função recursiva PURA que realiza o efeito da função f sobre todas as cores
  // (em todas as leafs) da QTree
  def mapColourEffectWithState(f:(Color,RandomWithState) => (Color,RandomWithState)) (qt:QTree[Coords]): QTree[Coords] = {
    val random = MyRandom(1)

    def aux(f:(Color,RandomWithState) => (Color,RandomWithState), qt:QTree[Coords], r: RandomWithState): (QTree[Coords],RandomWithState) = {
      qt match {
        case ql: QLeaf[Coords,Section] => {
          val ax = f(ql.value._2,r)
          (QLeaf(ql.value._1,ax._1),ax._2)
        }
        case qn: QNode[Coords] => {
          val t1 = aux(f,qn.one,r)
          val t2 = aux(f,qn.two,t1._2)
          val t3 = aux(f,qn.three,t2._2)
          val t4 = aux(f,qn.four,t3._2)
          (QNode(qn.value,t1._1,t2._1,t3._1,t4._1),t4._2)
        }
        case _ => (qt,r)
      }
    }
    aux(f,qt,random)._1
  }

  // Efeito ruído PURO
  def noiseEffectWithState(color: Color, r:RandomWithState): (Color,RandomWithState) ={
    val i = r.nextInt(2)
    if(i._1 == 1)
      (Color.darkGray,i._2)
    else
      (color,i._2)
  }
}