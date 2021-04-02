import java.awt.Color // usamos as cores do java?

trait QTree[+A] {
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

  def makeQTree[A] (b:BitMap):QTree[A]
  def scale[A](scale:Double, qt:QTree[A]):QTree[A]
  def mirrorV[A] (qt:QTree[A]):QTree[A]
  def mirrorH[A] (qt:QTree[A]):QTree[A]
  def rotateD[A] (qt:QTree[A]):QTree[A]
  def rotateR[A] (qt:QTree[A]):QTree[A]
  def mapColourEffect[A] (f:Color => Color, qt:QTree[A]):QTree[A]

}