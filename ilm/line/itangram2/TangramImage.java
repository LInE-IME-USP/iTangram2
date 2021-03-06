/*
 * iTangram2 - Interactive/Internet Tangram: http://www.matematica.br/itangram
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br / http://www.usp.br/line
 *
 * @author iTangram iLM version by Leo^nidas O. Brandao LInE-IME-USP / Adapted from Tangram version by Serge
 *
 * @description Load image in order to put in a graphical container
 * 
 * @see ilm.line.util.UtilFiles
 *  
 * @credits
 * This source is free and provided by iMath Project (University of S�o Paulo - Brazil). In order to contribute, please
 * contact the iMath coordinator Le�nidas O. Brand�o.
 * The original code is from 'javapage@serger.biz'.
 *
 * O c�digo fonte deste programa � livre e desenvolvido pelo projeto iM�tica (Universidade de S�o Paulo). Para contribuir,
 * por favor contate o coordenador do projeto iMatica, professor Le�nidas O. Brand�o.
 * O c�digo original deste sistema � de 'javapage@serger.biz'.
 * 
 */

package ilm.line.itangram2;

import ilm.line.util.Bundle;
import ilm.line.util.UtilFiles;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;


public class TangramImage extends Canvas {

  private Image offscreen  = null;  // para t�cnica de DOUBLE BUFFERING evita "flicker" e aqui consegue-se
  private Graphics offgraphics = null;

  private String nome = null; // nome do bot�o - mensagem???
  private java.awt.Container father = null;

  private Image        // images to this icon: defined in ./igeom/botoes/TangramImageMenus.java
          img = null;  //
  public static Dimension imgTamanho = new Dimension(30, TangramProperties.ALTURA_BOTAO);

  //---- DB
  public void pinta () {
    Graphics gr = null;
    if (offscreen != null) try {
       // primeiro "paint" entra antes de construir primeira "offscreen"
       gr = offscreen.getGraphics();
       if (gr!=null) //H
          offgraphics = gr;
       paint(gr);
       } catch (Exception e) { e.printStackTrace(); }
    }

  //---- DB
  private void copy2DoubleBuffer (Graphics gr) { // copy to 'offScreen' and draw the image
    // copia tudo na tela
    if (gr==null) {
       //System.err.println("Error: in button, in bouble buffering, graphics is null!! "+fileName); //, tamanho.width="+tamanho.width+", $
       return; // selecionar botao primario => deselecionar outro botao primario => faz 'repaint' dele, que esta vazio => cai aqui
       }
    else try {
       gr.drawImage(offscreen,0,0,this);
    } catch (Exception e) { e.printStackTrace(); }
    }

  public void paint (Graphics gr) {
    //throws Exception
    Dimension tamanho,
              imgTamanho = TangramImage.imgTamanho;
    int       l=1; // largura das linhas de bot�es
    int dx = 0, dy = 0;
    try {
      // tenta desenhar bot�o
      tamanho = this.getSize();
      if (img!=null)  {
         // Em Netscape 4 d� erro de permiss�o p/ carregar imagens
         imgTamanho.setSize(img.getWidth(this), img.getHeight(this)); //
         }
      else {
         //- System.out.println(msgEpaint+": "+img);
         return; //imgTamanho = new Dimension(img.getWidth(this), img.getHeight(this));
         }
      //---- DB
      if (offscreen == null) { // double buffering techniche
         // System.out.print("-");
         offscreen = createImage(tamanho.width, tamanho.height);
         }
      offgraphics = offscreen.getGraphics(); //H
      gr = offscreen.getGraphics(); // pega �ltimo buffer "gr�fico"
      //---- DB

      try { // draw image in Graphics
        gr.drawImage(img, dx + tamanho.width/2 - imgTamanho.width/2, dy + tamanho.height/2 - imgTamanho.height/2, this);
      } catch (Exception e) {
        gr.setColor(Color.white);
        gr.setFont(TangramProperties.ftPlain10); //Desenhista.FONTE_PEQUENA1
        gr.drawString(this.nome,1,TangramProperties.tamY/2);
        System.err.println("TangramImage.java: error in image " + nome + ": " + e);
        }

    } catch (java.lang.Exception npe) {
       //  System.out.println(msgEpaint+": "+img); //C npe.printStackTrace();
       tamanho = imgTamanho = new Dimension(TangramProperties.largBt, TangramProperties.altBt);
       }

     //---- DB
     copy2DoubleBuffer(this.getGraphics()); // copy to 'offScreen' and draw the image
     } // void paint(Graphics gr)


  public TangramImage (java.awt.Container father, String strImgName) {
    img = UtilFiles.getImage(this.getClass(), strImgName);

    this.nome = strImgName;
    this.img = img; // this image will be drawn
    this.father = father;
    }

  }
