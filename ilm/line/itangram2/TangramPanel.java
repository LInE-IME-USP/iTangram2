/*
 * iTangram2 - Interactive/Internet Tangram: http://www.matematica.br/itangram
 *
 * Free interactive solutions to teach and learn
 *
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br / http://www.usp.br/line
 *
 * @author iTangram iLM version by Leo^nidas/LInE-IME-USP / Adapted from the Tangram version by Serge
 *
 * @description
 *
 * @see
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


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.*;


public class TangramPanel extends Panel {

  //----------------------------------------------------------------------------
  //  Data
  //----------------------------------------------------------------------------

  private TangramPosition tangramPosition;
  public  TangramPosition getTangramPosition () { return tangramPosition;  }

  private boolean onDrag = false; // allow to count numbers of "click-and-point" and "drag-and-drop"
  private boolean onMove = false; // help to count number of "point-click" (do not free 'tangramPieceOnMove' in the next 'mouseReleased'

  // Drag control: allow to drag whole area (all 7 pieces)
  private boolean isDragging = false; // during dragging moviment only 
  private int xcoorArea = 0, ycoordArea = 0; // to translate working area (dragg all)
  private int xcoorArea0 = 0, ycoordArea0 = 0; // to calculate translation of working area

  private TangramPiece tangramPieceOnMove = null; // to allow movement under model "click-and-point" ("click-get/click-relese")
  private int _onMoveX0, _onMoveY0; // to smooth movement

  // Array of the Tangram pieces maintained in z-axis order.
  // Note: LinkedList not supported by IE5 standard VM.
  private TangramPiece zAxisOrder[];
  private TangramPiece selectedPiece = null;

    // Data for Double Image Buffering
  private Tangram  tangram = null;
  private Applet   applet  = null;
  private Image    offscreenImage;     // Place to hold Image
  private Graphics offscreenGraphics;  // The second graphics context of offscreenImage


  //------------------------------------------------------------------------
  //  Tangram Pieces
  //  @see: Tangram.java: init()
  //  @see: TangramModelData.java: define variables to positioning
  //------------------------------------------------------------------------
  protected void positionPieces (boolean fromOutSide) {
    tangramPosition = new TangramPosition();
    try {
      // System.out.println("TangramPanel.java: positionPieces(): " + TangramModelData.start_position);
      tangramPosition.loadFromString(TangramModelData.start_position, "position0");
    } catch (Exception e) {
      System.err.println("TangramPanel.java: positionPieces(): error: " + e);
      }

    //D System.out.println("\n\n\nilm/line/itangram2/TangramPanel.java: positionPieces: " + TangramModelData.start_position);

    // Position piece is: tangramPieces.getXOffset(); tangramPieces.getYOffset(); tangramPieces.getRotation();
    TangramPiece [] pieces = tangramPosition.getTangramPieces();
    zAxisOrder = new TangramPiece[7];
    for (int count = 0; count < 7; count++) {
      zAxisOrder[count] = pieces[count];
      //D System.out.println(" - " + count + ": (x,y,rot)=" + pieces[count].getXOffset() + ", " + pieces[count].getYOffset() + ", " + pieces[count].getRotation() + ")");
      }
    }


  //----------------------------------------------------------------------------
  //  Constructor
  //----------------------------------------------------------------------------
  public TangramPanel (Tangram tangram) {
    super();

    this.applet  = tangram;
    this.tangram = tangram;
  
     setBackground(TangramProperties.TANGRAM_PANEL_BACKGROUND_COLOR);
    addMouseListener(new TangramPanelMouseListener());
    addMouseMotionListener(new TangramPanelMouseMotionListener());
    addKeyListener(new TangramPanelKeyListener());
    addFocusListener(new TangramPanelFocusListener());
    addComponentListener(new TangramPanelComponentListener());

    // Tangram Pieces
    // @see 'ilm.line.itangram2.Tangram.buildTangramGUI()' load positions
    positionPieces(false); // true => from outside of 'TangramPanel.java'

    } // End of Constructor

  
  //------------------------------------------------------------------------
  //  Resize the Offscreen Image
  //------------------------------------------------------------------------
  public void updateOffscreenImage () {
    Dimension size = this.getSize();
    if (size==null)
      return;
    // System.err.println("TangramPanel.updateOffscreenImage(): size=" + size);
    if (size.width<0) { size = new Dimension(280, 280); System.err.println("TangramPanel.updateOffscreenImage(): correcao size=" + size); }
    try {
      offscreenImage = createImage(size.width, size.height);
      offscreenGraphics = offscreenImage.getGraphics();
    } catch (Exception e) {
      System.err.println("TangramPanel.updateOffscreenImage(): size=" + size);
      e.printStackTrace();
      }
    }

  
  //----------------------------------------------------------------------------
  //  Painting Methods:  Repaint - Update - Paint
  //----------------------------------------------------------------------------


  //------------------------------------------------------------------------
  //  Helper Method for repainting the changed area only.
  //------------------------------------------------------------------------
  private void repaint (Rectangle before, Rectangle after) {
    Rectangle union;
    if (before == null && after == null) return;
    if (before == null)      union = after;
    else if (after  == null) union = before;
    else                     union = before.union(after);
    //D  System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
    //D System.out.println("TangramPanel.java: union=" + union.x + "," + union.y + "," + union.width + "," + union.height);
    repaint(union.x, union.y, union.width+1, union.height+1);
    }


  //------------------------------------------------------------------------
  //  Overrides the update method for double buffering.
  //------------------------------------------------------------------------
  public void update (Graphics g) {
    paint(g);
    }


  //------------------------------------------------------------------------
  //  Paint method with double buffering.
  //------------------------------------------------------------------------
  public void paint (Graphics graph) {
    Dimension size = this.getSize(); // get the size

    /* DEBUG
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
    //System.out.println("TangramPanel.java: size=" + size.getWidth() + "," + size.getHeight());
    TangramPiece [] tangramPieces = tangramPosition.getTangramPieces();
    System.out.println("TangramPanel.java: xcoorArea=" + xcoorArea + ", ycoordArea=" + ycoordArea);
    int numberOfPieces = tangramPieces.length, angle;
    int xmin = Integer.MAX_VALUE, ymin = Integer.MAX_VALUE;
    int xminPiece, yminPiece;
    for (int ii_=0; ii_<numberOfPieces; ii_++) {
      //angle = piecesComposingUnitsParam[ii_][2];
      //System.out.println( " " + ii_ + ": angle=" + angle + ": " + tangramPieces[ii_].getName() + ":" + TangramPosition.getCorners(tangramPieces[ii_]));
      xminPiece = TangramPosition.getMinCoordX(tangramPieces[ii_]);
      yminPiece = TangramPosition.getMinCoordY(tangramPieces[ii_]);
      if (xminPiece<xmin)
        xmin = xminPiece;
      if (yminPiece<ymin)
        ymin = yminPiece;
      System.out.println( " " + ii_ + ": " + tangramPieces[ii_].getName() + ":" + TangramPosition.getCorners(tangramPieces[ii_]));
      }
      System.out.println(" xmin=" + xmin + ", ymin=" + ymin);
    */

    // Draw black background working area
    offscreenGraphics.setColor(TangramProperties.TANGRAM_PANEL_BACKGROUND_COLOR);
    offscreenGraphics.fillRect(0, 0, size.width, size.height);
    for (int cnt = 0; cnt < zAxisOrder.length; cnt++) {
      if (isDragging) // during dragging moviment only
        zAxisOrder[cnt].translate(xcoorArea, ycoordArea); // allow to drag whole working area
      zAxisOrder[cnt].paint(offscreenGraphics);
      }

    // Draw light border
    offscreenGraphics.setColor(TangramProperties.TANGRAM_PANEL_BACKGROUND_BORDER);
    offscreenGraphics.drawRect(0, 0, size.width-1, size.height-1);

    graph.drawImage(offscreenImage,0,0,this);
    }

  
  //----------------------------------------------------------------------------
  // Methods for modifying the location of the pieces
  //----------------------------------------------------------------------------

    //------------------------------------------------------------------------
  //  Selects a piece.
  //------------------------------------------------------------------------
  private void selectPiece (TangramPiece selection) {
    if (selectedPiece != null) {
      selectedPiece.setSelected(false);
      }
    selectedPiece = selection;
    if (selection == null) return;
    selection.setSelected(true);

    boolean foundflag = false;
    for (int cnt = 0; cnt < zAxisOrder.length-1; cnt++) {
      if (zAxisOrder[cnt] == selection) foundflag = true;
      if (foundflag) zAxisOrder[cnt] = zAxisOrder[cnt+1];
      }
    zAxisOrder[zAxisOrder.length-1] = selection;
    }


  //------------------------------------------------------------------------
  //  Rotates a piece.
  //------------------------------------------------------------------------
  private void rotatePiece (float angle) {
    //D System.out.println("\n\n\nTangramPanel.java: rotatePiece: angle=" + angle);
    if (selectedPiece != null) {
      Rectangle before = selectedPiece.getBounds();
      selectedPiece.rotate(angle);
      Rectangle after = selectedPiece.getBounds();
      repaint(before, after);
      }
    }


  //------------------------------------------------------------------------
  //  Translates a piece.
  //------------------------------------------------------------------------
  private void translatePiece (TangramPiece selection, int deltaX, int deltaY) {
System.out.println("\n\n\nTangramPanel.java: translatePiece: " + selection.getName());
    if (selectedPiece != null) {
      selectedPiece.translate(deltaX, deltaY);
      }
    }


  //------------------------------------------------------------------------
  //  Reload initial positioning to working pieces.
  //  @see ilm/line/itangram2/TangramControls.java: loadCurrentModel()
  //------------------------------------------------------------------------
  public void loadPosition0 () {
    String strPosition0 = (String) this.tangram.getProperties("position0");
    if (strPosition0==null || strPosition0=="")
      return;
    TangramModelData.start_position = strPosition0; // initial positon in working area: shuffled                                      
    // positionPieces(true); // true => from outside of 'TangramPanel.java'

    //System.out.println("\n\n\nTangramPanel.java: loadPosition0: " + strPosition0);
    repaint();
    }



  //----------------------------------------------------------------------------
  //  Private Data for Mouse Listener
  //----------------------------------------------------------------------------
  private int mouseListenerX = 0;
  private int mouseListenerY = 0;


  //----------------------------------------------------------------------------
  //  Mouse Listener Inner Class
  //----------------------------------------------------------------------------

  class TangramPanelMouseListener extends MouseAdapter {

    //------------------------------------------------------------------------
    //  Mouse Pressed.
    //------------------------------------------------------------------------
    public void mousePressed (MouseEvent e) {
      int xMouse = e.getX();
      int yMouse = e.getY();

      // Drag working area
      isDragging = false; // default is 'not dragging'
      xcoorArea0 = xMouse; ycoordArea0 = yMouse; // to calculate translation of working area
      //D System.out.println("src/ilm/line/itangram2/TangramPanel.java: mousePressed: " + "("+xcoorArea+","+ycoordArea+")");

      // Traverse the list backwords:
      for (int cnt = zAxisOrder.length-1; cnt >= 0; cnt--) {
        TangramPiece current = zAxisOrder[cnt];
        if ( current.contains(xMouse, yMouse) ) {
          mouseListenerX = xMouse;
          mouseListenerY = yMouse;
          if (current == selectedPiece) {
            if (tangramPieceOnMove!=null) { // deselect the piece to stop move under model "click-get/click-relese"
              tangramPieceOnMove = null;
              }
            else { // new click in the piece no select => select it to move under model "click-get/click-relese"
              tangramPieceOnMove = current;
              onMove = true; // help to count number of "point-click" (do not free 'tangramPieceOnMove' in the next 'mouseReleased'
              _onMoveX0 = xMouse; _onMoveY0 = yMouse; // to do smooth movement
              }
            return;
            } // if (current == selectedPiece)

          Rectangle before = null;
          if (selectedPiece != null)
            before = selectedPiece.getBounds();
          selectPiece(current);
          Rectangle after = selectedPiece.getBounds();
          repaint(before, after);

          tangramPieceOnMove = current; // select the piece to move under model "click-get/click-relese"
          onMove = true; // help to count number of "point-click" (do not free 'tangramPieceOnMove' in the next 'mouseReleased'

          _onMoveX0 = xMouse; _onMoveY0 = yMouse; // to do smooth movement

          return;
          } // if ( current.contains(xMouse, yMouse) )

        } // for

      // No Piece has been clicked
      Rectangle before = null;

      //System.out.println("src/ilm/line/itangram2/TangramPanel.java: mousePressed: " + tangramPieceOnMove);
      if (tangramPieceOnMove!=null) { // a click in blank area while moving any piece => quit movement
        tangramPieceOnMove = null;
        onMove = false; // security
        }    

      if (selectedPiece == null) return;
      before = selectedPiece.getBounds();
      selectPiece(null);
      repaint(before, null);
      } // public void mousePressed(MouseEvent e)


  //------------------------------------------------------------------------
  //  Mouse Released.
  //------------------------------------------------------------------------
  public void mouseReleased (MouseEvent e) {
    if (selectedPiece == null && tangramPieceOnMove == null) {
      onMove = false; // security...
      onDrag = false; // security...
      return;
      }

     //D String str0 = "onDrag="+onDrag+", onMove="+onMove+", selectedPiece=" + (selectedPiece!=null?"[*]":"<>") + ", tangramPieceOnMove=" + (tangramPieceOnMove!=null?"[*]":"<>");
     //D System.out.println("TangramPanel.java: mouseReleased: PC=" + tangram.getCountPC() + ", DD=" + tangram.getCountDD() + " - " + str0);
     if (tangramPieceOnMove!=null && !onDrag && onMove) {
       //- just came from 'mousePressed(...)' under 'click-point' => ignore
       return;
       }
     else
     if (tangramPieceOnMove==null && !onDrag && onMove) {
       //- second release (after 'mousePressed', 'mouseReleased') under 'click-point' => count as 'click-point'
       tangram.setCountPC();
       }
     else
     if (selectedPiece!=null && onDrag) {
       //- just came from 'mousePressed(...)' under 'drag' => count as 'drag'
       tangram.setCountDD();
       }

     Point snapPoint = null;
     // Traverse the list backwords:
     for (int cnt = zAxisOrder.length-1; cnt >= 0; cnt--) {
       TangramPiece current = zAxisOrder[cnt];
       if (current == selectedPiece) continue; // do not try object with itself!
       if (selectedPiece!=null)
         snapPoint = current.snap(selectedPiece);
       if (snapPoint!=null) {
         break;
         }
       }
     if (snapPoint != null) {
       Rectangle before = null;
       if (selectedPiece != null)
         before = selectedPiece.getBounds();
       translatePiece(selectedPiece, snapPoint.x, snapPoint.y);
       Rectangle after = selectedPiece.getBounds();
       repaint(before, after);
       }

     //+++++++++++++++++++++++++++++++++++++
     // Compare with the model: "false" => evaluate after a movement; true => show to the learner (if allowed) the congratulation if correct
     //+++++++++++++++++++++++++++++++++++++
     boolean result = tangram.comparePositionWithModel(false, true);
     if (result) // problem solved and if in mode 'point-click' (with piece in move) => release the piece!
       tangramPieceOnMove = null;

     if (tangramPieceOnMove!=null && !onDrag && onMove) { onMove = false; }
     else if (selectedPiece!=null && onDrag) { onDrag = false; }

     } // public void mouseReleased(MouseEvent e)

  } // class TangramPanelMouseListener extends MouseAdapter



  //----------------------------------------------------------------------------
  //  Mouse Motion Listener Inner Class
  //----------------------------------------------------------------------------

  class TangramPanelMouseMotionListener extends MouseMotionAdapter {

    // MouseMotionListener
    public void mouseMoved (MouseEvent e) {
      if (tangramPieceOnMove == null) {
        return;
        }
      int xMouse = e.getX();
      int yMouse = e.getY();
      tangramPieceOnMove.translate(xMouse-_onMoveX0, yMouse-_onMoveY0);
      _onMoveX0 = xMouse; _onMoveY0 = yMouse;
      repaint();
      }


    //------------------------------------------------------------------------
    //  Mouse Dragged.
    //------------------------------------------------------------------------
    public void mouseDragged (MouseEvent e) {
      int mouseMargin = TangramProperties.TANGRAM_PANEL_MOUSE_MARGIN;
      int xMouse = e.getX();
      int yMouse = e.getY();
      if (xMouse < mouseMargin) xMouse = mouseMargin;
      if (yMouse < mouseMargin) yMouse = mouseMargin;

      // Drag working area
      if (selectedPiece == null) { // no selected piece => drag working area
        xcoorArea = xMouse-xcoorArea0; ycoordArea = yMouse-ycoordArea0; // to calculate translation of working area
        xcoorArea0 = xMouse; ycoordArea0 = yMouse; // to calculate translation of working area
        isDragging = true;
        repaint();
        return;
        }

      isDragging = false;

      onDrag = true; // allow to count numbers of "click-and-point" and "drag-and-drop"
      tangramPieceOnMove = null;

      //DSystem.out.println("TangramPanel.java: mouseDragged: clickCount=" + e.getClickCount() + ", modifiers=" + e.getModifiers());
      Dimension size = getSize();
      if (xMouse > size.width  - mouseMargin) xMouse = size.width  - mouseMargin;
      if (yMouse > size.height - mouseMargin) yMouse = size.height - mouseMargin;
      Rectangle before = selectedPiece.getBounds();
      translatePiece(selectedPiece, xMouse-mouseListenerX, yMouse-mouseListenerY);
      Rectangle after = selectedPiece.getBounds();
      repaint(before, after);
      mouseListenerX = xMouse;
      mouseListenerY = yMouse;
      if (tangramPieceOnMove!=null) { // dragging => deselect the piece to move under model "click-get/click-relese"
        tangramPieceOnMove = null;
        }

      } // public void mouseDragged(MouseEvent e)
     
   } // class TangramPanelMouseMotionListener extends MouseMotionAdapter


  //----------------------------------------------------------------------------
  //  Key Listener Inner Class
  //----------------------------------------------------------------------------

  class TangramPanelKeyListener extends KeyAdapter {
    public void keyPressed (KeyEvent e) {
      int keycode = e.getKeyCode();
      //TODO: implementar reflexao?
      if (keycode == KeyEvent.VK_LEFT) {
        if (tangram.getTangramControls().getRotationCheckState()) {
          rotatePiece(15);
          }
        else {
          rotatePiece(45);
          }
        }
      else
      if (keycode == KeyEvent.VK_RIGHT) {
        if (tangram.getTangramControls().getRotationCheckState()) {
          rotatePiece(-15);
          }
	else {
          rotatePiece(-45);
          }
        }
      }
    }


  //----------------------------------------------------------------------------
  //  Focus Listener Inner Class
  //----------------------------------------------------------------------------

  class TangramPanelFocusListener extends FocusAdapter {
    public void focusLost (FocusEvent e) {
      // Deselect the currently selected piece.
      Rectangle before = null;
      if (selectedPiece == null) return;
      before = selectedPiece.getBounds();
      selectPiece(null);
      repaint(before, null);
      }
    }


  //----------------------------------------------------------------------------
  //  Component Listener Inner Class
  //----------------------------------------------------------------------------

  class TangramPanelComponentListener extends ComponentAdapter {

    public void componentResized (ComponentEvent e) {
      updateOffscreenImage();
      }

   }


  } // public class TangramPanel extends Panel
