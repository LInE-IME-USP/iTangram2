/*
 * iTangram2 - Interactive/Internet Tangram: http://www.matematica.br/itangram
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br / http://www.usp.br/line
 *
 * @author iTangram iLM version by Leo^nidas O. Branda~o LInE-IME-USP / Adapted from Tangram version from Serge
 *
 * @description Button with a graphical image in background.
 *
 * @see import ilm.line.util.UtilFiles;
 *  
 * @credits
 * This source is free and provided by iMath Project (University of São Paulo - Brazil). In order to contribute, please
 * contact the iMath coordinator Leônidas O. Brandão.
 * The original code is from 'javapage@serger.biz'.
 *
 * O código fonte deste programa é livre e desenvolvido pelo projeto iMática (Universidade de São Paulo). Para contribuir,
 * por favor contate o coordenador do projeto iMatica, professor Leônidas O. Brandão.
 * O código original deste sistema é de 'javapage@serger.biz'.
 * 
 */

package ilm.line.itangram2;

import ilm.line.util.Bundle;
import ilm.line.util.Util;
import ilm.line.util.UtilFiles;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import java.util.StringTokenizer;


public class TangramButton extends Canvas implements MouseListener {

  // Used with:
  // + buttons: { img/button_exerc_evaluate.gif; img/button_send.gif }
  // + buttons: { img/button_prev.gif; img/button_next.gif; img/button_getcode.gif; img/button_entercode.gif } all wigh 32x32
  protected static final int
     WIDTH = 32, HEIGHT = 32; // default size of images: img/button_entercode.gif  img/button_getcode.gif  img/button_next.gif  img/button_prev.gif
  public static final Dimension IMGSIZE = new Dimension(WIDTH+1, HEIGHT+1);

  private static final Color
     BUTTON_UNDER_CLICK = new Color(200,200,0), // when button are been pressed (during)
     BUTTON_SELECTED = new Color(234,234,234);  // when button is pressed (selected)

  private Image offscreen  = null;  // para técnica de DOUBLE BUFFERING evita "flicker" e aqui consegue-se
  private Graphics offgraphics = null;
  private static long tempoAnterior = 0; // truque para evitar "flicker" ao deixar "mouse" sobre borda de botão
  private static boolean removeThisButton = false;

  private String name = null; // nome do botão - mensagem???
  private TangramControls tangramControls; // treat mouse click
  private int acao;    // identificador da ação associada ao botão

  private int PU_HEIGHT;
  private int PU_WIDTH;
  private int LINE_HEIGHT;
  private int NUMBER_OF_LINES;
  private String [] msgArray; // para poder mostrar msg grandes: colocar \n para quebrá-la

  private boolean mostrePopUp;
  private static TangramButton estePopUp = null; // truque para remover PopUp sobrando, via TangramButton ou via AreaDeDesenho
  private Image img; // images to this icon: defined in ./igeom/botoes/TangramButtonMenus.java

  private boolean abaixado, ativado = true;
  private boolean selecionado = false;

  private PopMenu menu = null;
  private Component owner = null;
  private Container mainContainer;
  private LayoutManager mainLayout;

  public void setSelected (boolean bool) {
    if (bool) { // selected
       selecionado = true;
       abaixado = true;
       }
    else { // not selected
       selecionado = false;
       abaixado = false;
       }
    removePopUp(); // mostrePopUp = false
    myPaint(); // repaint()
    // System.out.println("TangramButton.setSelected: bool="+bool+" ------------------------");
    }

  //HH -----------
  private class PopMenu extends Canvas {
    String msg;
    PopMenu (String msg) {
       this.msg = msg;
       // System.out.println(" -> msg="+msg);
       }

    public void paint (Graphics g) {
       // if (this.name.equals("Medir")) System.out.println("TangramButton: "+this.name+" owner="+owner.getFont()+" FONTE_PEQUENA1="+Desenhista.FONTE$
       //System.out.println("TangramButton: this.msg="+this.msg);
       g.setFont(TangramProperties.ftPlain10);
       g.setColor(TangramProperties.COR_FUNDO); //
       g.fillRect(1,1,getSize().width -2, getSize().height -2);
       g.setColor(Color.BLACK);
       g.drawRect(0,0,getSize().width -1, getSize().height -1);
       int pos = TangramProperties.ALTURA_LINHA;
       for (int i=0; i<msgArray.length; i++) {
           g.drawString(msgArray[i], 3, pos);
           pos += TangramProperties.ALTURA_LINHA;
           }
       }
    } // class PopMenu extends Canvas


  // static int countS=0;

  private void drawButtonImage (Graphics gr, Dimension tamanho) {
    int       l=1; // largura das linhas de botões
    int dx = 0, dy = 0;
    try {
      //if (this.img==null) return; // the first call to 'paint(Graphics)' occurs before constructor 'TangramButton' defining 'img' => ignore its first call

      //---- DB
      if (offscreen == null) { // double buffering techniche
         offscreen = createImage(tamanho.width, tamanho.height);
         }

      offgraphics = offscreen.getGraphics(); //H
      gr = offscreen.getGraphics(); // pega último buffer "gráfico"
      //---- DB
       
       // gr.setColor(Color.lightGray); // just in case - missign texture => use gray
       // gr.fillRect(0, 0, tamanho.width, tamanho.height);

       try {
         // Here the icons has a background border
	 if (abaixado) {
           gr.setColor(Color.darkGray);
           gr.fillRect(1,1, tamanho.width, tamanho.height); // background
           }
         else {
           //x gr.setColor(Color.lightGray);
           //x gr.fillRect(1,1, tamanho.width, tamanho.height); // background
           }

         gr.drawImage(img, 0, 0, this);

         // Draw border - images now has a nice border
         //x gr.setColor(Color.white);
         //x gr.drawLine(0, 0, tamanho.width, 0); // left and top
         //x gr.drawLine(0, 0, 0, tamanho.height);
         //x gr.setColor(Color.lightGray);  // right and botton
         //x gr.drawLine(tamanho.width, 0, tamanho.width, tamanho.height-1);    // right
         //x gr.drawLine(0, tamanho.height-1, tamanho.width, tamanho.height-1); // botton

       } catch (Exception e) {
         gr.setColor(Color.white);
         gr.setFont(TangramProperties.ftPlain10); // Desenhista.FONTE_PEQUENA1
         gr.drawString(this.name, 1, TangramProperties.tamY/2);
         System.err.println("TangramButton.drawButtonImage: (3) error when loading background image to button " + this.name + ": "+e);
         }

        //---- DB
        copy2DoubleBuffer(this.getGraphics()); // copy to 'offScreen' and draw the image
       
    } catch (java.lang.Exception npe) {
       npe.printStackTrace(); //  System.out.println(msgEpaint+": "+img); //C npe.printStackTrace();
       }
    } // private void drawButtonImage(Graphics gr, Dimension tamanho)

  public void paint (Graphics gr) {
    Dimension tamanho,
              imgTamanho = TangramButton.IMGSIZE;
    try {
      // tenta desenhar botão
      tamanho = imgTamanho; //_ this.getSize();
      if (img==null)  { //SECURITY: imgTamanho is 'TangramButton.IMGSIZE' and the last is fixed ''
         System.err.println("TangramButton.java: " + this.name + " empty image? Img=" + img);
         imgTamanho = TangramButton.IMGSIZE; // 
         }

      // Draw button with image
      drawButtonImage(gr, tamanho);

    } catch (java.lang.Exception npe) {       //  System.out.println(msgEpaint+": "+img); //C npe.printStackTrace();
       tamanho = imgTamanho = new Dimension(TangramProperties.largBt, TangramProperties.altBt);
       }

     } // void paint(Graphics gr)

  //---- DB
  protected void myPaint () { // from 'TangramControls.java: ActionListener buttonListener'
    Graphics gr = null;
    if (offscreen != null) { // primeiro "paint" entra antes de construir primeira "offscreen"
       gr = offscreen.getGraphics();
       if (gr!=null)
          offgraphics = gr;
       paint(gr);
       }
    //D else System.out.println("TangramButton.myPaint: ERRO! ----------------------------------- offscreen="+offscreen);
    }
  private void copy2DoubleBuffer (Graphics gr) { // copy to 'offScreen' and draw the image
    // copia tudo na tela
    if (gr==null) {
       return; // selecionar botao primario => deselecionar outro botao primario => faz 'repaint' dele, que esta vazio => cai aqui
       }
    else
       gr.drawImage(offscreen,0,0,this);
    }
  //---- DB

  private void addPopUp (int px1, int py1) {
    int px = 0, py = 0,
        px_horOFFSET = 0, py_vertOFFSET = 0;
    boolean vazio = false;
    int tam_msg=0;
    // Primary button => appears above the button
    // (and iGeom HAS 3 Panels in 'PainelTopoBDD')
    px_horOFFSET = 0; // TangramProperties.LARGURA_BOTAO; // nao pode ficar sobre menu
    py_vertOFFSET = TangramProperties.ALTURA_BOTAO; // fica acima
    if (owner!=null && mainContainer!=null)  {
       px = owner.getLocationOnScreen().x - mainContainer.getLocationOnScreen().x + px_horOFFSET; // px_horOFFSET + owner.getLocationOnScreen().x; //
       py = owner.getLocationOnScreen().y - mainContainer.getLocationOnScreen().y + py_vertOFFSET; // py_vertOFFSET + owner.getLocationOnScreen().y; //
       //DSystem.out.println("TangramButton.java: addPopUp(" + px1 + "," + py1 + "): (" + px + "," + py + ") - (" + owner.getLocationOnScreen().x + "," + owner.getLocationOnScreen().y + ") - " +"(" + mainContainer.getLocationOnScreen().x + "," + mainContainer.getLocationOnScreen().y + ")");

       // correção: todo o texto tem que ficar dentro da área visível
       // pos_msg + tam_msg > tam_janela
       tam_msg = getLocation().x + menu.getSize().width + TangramProperties.DELTAx;
       if (mainContainer.getSize().width < tam_msg) {
          px -= (tam_msg-mainContainer.getSize().width);
          }
       }
    else  {     
       vazio = true;
       System.err.println("Error: in button, conteiner empty in " + this.name);
       }

    menu.setLocation(px, py); //H
    menu.setVisible(true);

    mostrePopUp = true;
    } // private void addPopUp(int px1, int py1)

  private void removePopUp () {
    if (mostrePopUp && mainContainer!=null) {
       menu.setVisible(false);
       }
    mostrePopUp = false;
    }

  public static void desativePopUp () {
    if (estePopUp!=null)
       estePopUp.removePopUp();
    }


  public void mouseSobreBotao (int acaoBotao) {
    setCursor(new Cursor(TangramProperties.cursorPadrao)); // volta cursor ao "normal"
    }

  // -- MouseMotionListener
  // public void mouseDragged (MouseEvent mouseevt) {    }	
  // public void mouseMoved (MouseEvent mouseevt) {    }	


  // Every button click falls here just after the 'void mouseClicked(java.awt.event.MouseEvent me)'
  private void myMouseClicked (java.awt.event.MouseEvent me) {
    this.tangramControls.treatMouseClick(me); //
    }

  // Every button click falls here first!
  public void mouseClicked (java.awt.event.MouseEvent me) {
    }
  public void mouseReleased (java.awt.event.MouseEvent me) {
    int modifiers = me.getModifiers(); // help to identify 'dragg' with mouse out of button when released => need repaint()
    int clickCount = me.getClickCount();
    if (ativado) {
       abaixado = false;
       }
    myPaint();
    myMouseClicked(me); // Control in 'TangramControls.treatMouseClick(me)'

    } // void mouseReleased(java.awt.event.MouseEvent me)

  public void mousePressed (java.awt.event.MouseEvent me) { // sempre que clica sobre botão cai aqui
    // int modifiers = me.getModifiers(); // help to identify 'dragg' with mouse out of button when released => need repaint()
    if (ativado) {
       if (!abaixado) {
          abaixado = true;
          }
       }
    myPaint(); // provide effect of "pressing" the button
    } // void mousePressed(java.awt.event.MouseEvent me)

  public void mouseEntered (java.awt.event.MouseEvent evt) {
    desativePopUp();
    estePopUp = this;
    int x0 = evt.getX(), y0 = evt.getY();
    // Evita "flicker" ao passar mouse entre 2 botões
    // truque para evitar "flicker" ao deixar "mouse" sobre borda de botão
    removeThisButton = false;
    try  {
      long tempoAtual = System.currentTimeMillis();
      if (tempoAnterior+4>tempoAtual) {
         removeThisButton = true; return; 
         }
      tempoAnterior = tempoAtual;
    } catch (Exception e)  {
      System.err.println("TangramButton " + this.name + ": error " + e.toString()); 
      }

    // if (y0==30 || y0==31) removeThisButton = true; // entrando por baixo => 3 entradas/saídas imediatas

    Point p = this.getLocation();
    int x = p.x, y = p.y;

    //
    addPopUp(x,y);

    try  {
      this.mouseSobreBotao(acao);
      return;
    } catch (Exception e)  {
      return; 
      }

    } // void mouseEntered(java.awt.event.MouseEvent evt)

  public void mouseExited (java.awt.event.MouseEvent me) {
    int modifiers = me.getModifiers(); // help to identify 'dragg' with mouse out of button when released => need repaint()
    // Evita "flicker" ao passar mouse entre 2 botões
    // truque para evitar "flicker" ao deixar "mouse" sobre borda de botão
    try {
      long tempoAtual = System.currentTimeMillis();
      if (tempoAnterior+2>tempoAtual && removeThisButton) return;
    } catch (Exception e) { System.err.println("TangramButton " + this.name + ": error " + e.toString()); }

    removePopUp(); //HH remove a msg da janela
    } // void mouseExited(java.awt.event.MouseEvent me)

  public void setEnabled (boolean tf) {
    if (tf) {   // true
      ativado = true;
      abaixado = false;
      myPaint();
      }
   else { // botoes de script "recorrencia" e "cancela" caem aqui apos finalizar gravacao de script
      ativado = false;
      abaixado = false;
      myPaint();
      }
    }

  public void addMouseListener () {
    addMouseListener(this);
    //ml addMouseMotionListener(this); // mouseDragged and mouseMoved
    }


  private void defineMsg (String msg) {
    FontMetrics fm = getFontMetrics(TangramProperties.ftPlain10); //
    StringTokenizer st = new StringTokenizer(msg,"\n"); // from BarraDeDesenho.mensagem[this.acao]
    NUMBER_OF_LINES = st.countTokens();
    msgArray = new String[NUMBER_OF_LINES];
    PU_WIDTH = 0;
    for (int i=0; i<NUMBER_OF_LINES; i++) { // caso msg tenha mais de uma linha - while (st.hasMoreTokens())
      msgArray[i] = st.nextToken();
      int size = fm.stringWidth(msgArray[i]);
      if (size>PU_WIDTH) PU_WIDTH = size;
      }
    LINE_HEIGHT = fm.getHeight();
    PU_HEIGHT = (LINE_HEIGHT)*(NUMBER_OF_LINES);
    }

  public TangramButton (java.awt.Container father, java.awt.Frame frameF, TangramControls tangramControls, String msg, String strImgName) {
    img = UtilFiles.getImage(this.getClass(), strImgName);
    this.tangramControls = tangramControls; // to treat mouse click
    this.name = strImgName; // in Button
    this.img = img; // this image will be drawn
    if (frameF!=null) // is application
       this.mainContainer = frameF;
    else
       this.mainContainer = father; // (java.awt.Container)
    this.owner = this;
    msg = Bundle.msg(msg);
    this.menu = new PopMenu(msg); // PopUp com msg sobre botão ao passar mouse sobre - esta opção não tem "timer"

    // if (mainContainer!=null) mainContainer.setLayout(null); - resulta erro: java.lang.IllegalArgumentException: Width (-280) and height (0) cannot be <= 0
    defineMsg(msg); // define mensagem
    menu.setVisible(false);
    menu.setSize(PU_WIDTH + TangramProperties.DELTAx , PU_HEIGHT + TangramProperties.DELTAy); //
    mainContainer.add(menu, 0); //H (this, 0);
    mainContainer.validate();
    addMouseListener(); // ours listerners

    }

  }
