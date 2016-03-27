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
 * @description This is the Model and Controller to the movable pieces (those moved by the learner).
 *
 * @see ilm/line/itangram2/TangramPanel.java:
 *      + paint(Graphics): makes 'TangramPiece [] zAxisOrder[i].paint(offscreenGraphics)
 *      + selectPiece(TangramPiece): is the controll to track pieces movements (mouse treatment)
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


class TangramPiece {

  // Types of iTangram pieces
  protected static final String
    TANGRAM_FORM_LARGE_TRIANGE = "LargeTriangle",
    TANGRAM_FORM_MEDIUM_TRIANGE = "MediumTriangle",
    TANGRAM_FORM_SMALL_TRIANGE = "SmallTriangle",
    TANGRAM_FORM_SQUARE = "Square",
    TANGRAM_FORM_LOZENGE = "Lozange";

  //----------------------------------------------------------------------------
  // Private Data
  //----------------------------------------------------------------------------

  // Points coordinates, offset and angle of rotation
  private int []  xPoints; // x-coordinates to draw the piece using Polygon
  private int []  yPoints; // y-coordinates (these are defined in 'ilm.line.itangram.TangramProperties' as fixed to each piece)
  private int    xOffset  = 0;
  private int    yOffset  = 0;
  private float  rotation = 0;

  private Polygon tangramPiecePolygon; // tangramPiecePolygon = new Polygon(xPoints, yPoints, xPoints.length)
  private boolean isSelected = false;

  // Snapping behaviour
  private int []  xSnappingPoints = null;
  private int []  ySnappingPoints = null;
  private int []  xRotatedSnappingPoints = null;
  private int []  yRotatedSnappingPoints = null;

  private int [][] composingUnits;

  String name = "";

  // To find top most piece (its first coordinate (x,y))
  // @see TangramPosition.java:  getMinXY(int [][] piecesComposingUnitsParam, TangramPiece [] tangramPieces)
  protected int [] getXpoints () { return xPoints; }
  protected int [] getYpoints () { return yPoints; }


  // TangramPosition.tangramPieces[]
  // protected static int [] getMinXY (int [][] piecesComposingUnitsParam, TangramPiece [] tangramPieces) -> para TangramPosition.java
  // -> para TangramPosition.java

  // protected static int [] findCorrectTopCorner (int [][] piecesComposingUnitsParam, TangramPiece [] tangramPieces, boolean correctIt)
  // -> para TangramPosition.java


  //----------------------------------------------------------------------------

  public int getXOffset()  { return xOffset;  }
  public int getYOffset()  { return yOffset;  }
  public int getRotation() { return (int)rotation;  }


  //----------------------------------------------------------------------------
  // Constructor
  //----------------------------------------------------------------------------
  private void multiplyArraysByUnit () {
   int lengthUnit = 24;
   for (int count = 0; count < xPoints.length; count++) {
    xPoints[count] *= lengthUnit;
    yPoints[count] *= lengthUnit;
    }
   for (int count = 0; count < xSnappingPoints.length; count++) {
    xSnappingPoints[count] *= lengthUnit;
    ySnappingPoints[count] *= lengthUnit;
    }
   for (int count = 0; count < composingUnits.length; count++) {
    composingUnits[count][0] *= lengthUnit;
    composingUnits[count][1] *= lengthUnit;
    }
   }


  // @see TangramPosition.java
  public String getName () {
   return this.name;
   }


  // Creation of each kind of Tangran pieces
  public TangramPiece (String type) {
   // if (Tangram.ISDEBUG) System.out.println("src/ilm/line/itangram2/TangramPiece.java: type=" + type);
   this.name = type;
   if (type.equals(TANGRAM_FORM_LARGE_TRIANGE)) {
    xPoints         = TangramProperties.largeTriangle_xPoints;
    yPoints         = TangramProperties.largeTriangle_yPoints;
    xSnappingPoints = TangramProperties.largeTriangle_xSnappingPoints;
    ySnappingPoints = TangramProperties.largeTriangle_ySnappingPoints;
    composingUnits  = TangramProperties.largeTriangle_composingUnits;
    //multiplyArraysByUnit();
    }
   else if (type.equals(TANGRAM_FORM_MEDIUM_TRIANGE)) {
    xPoints         = TangramProperties.mediumTriangle_xPoints;
    yPoints         = TangramProperties.mediumTriangle_yPoints;
    xSnappingPoints = TangramProperties.mediumTriangle_xSnappingPoints;
    ySnappingPoints = TangramProperties.mediumTriangle_ySnappingPoints;
    composingUnits  = TangramProperties.mediumTriangle_composingUnits;
    //multiplyArraysByUnit();
    }
   else if (type.equals(TANGRAM_FORM_SMALL_TRIANGE)) {
    this.xPoints         = TangramProperties.smallTriangle_xPoints;
    this.yPoints         = TangramProperties.smallTriangle_yPoints;
    this.xSnappingPoints = TangramProperties.smallTriangle_xSnappingPoints;
    this.ySnappingPoints = TangramProperties.smallTriangle_ySnappingPoints;
    this.composingUnits  = TangramProperties.smallTriangle_composingUnits;
    //multiplyArraysByUnit();
    }
   else if (type.equals(TANGRAM_FORM_SQUARE)) {
    this.xPoints         = TangramProperties.square_xPoints;
    this.yPoints         = TangramProperties.square_yPoints;
    this.xSnappingPoints = TangramProperties.square_xSnappingPoints;
    this.ySnappingPoints = TangramProperties.square_ySnappingPoints;
    this.composingUnits  = TangramProperties.square_composingUnits;
    }
   else if (type.equals(TANGRAM_FORM_LOZENGE)) {
    this.xPoints         = TangramProperties.lozenge_xPoints;
    this.yPoints         = TangramProperties.lozenge_yPoints;
    this.xSnappingPoints = TangramProperties.lozenge_xSnappingPoints;
    this.ySnappingPoints = TangramProperties.lozenge_ySnappingPoints;
    this.composingUnits  = TangramProperties.lozenge_composingUnits;
   } else {
    System.err.println("Error: Invalid type for Tangram Piece consutructor");
    return;
    }
   rotatePoints();
   }


  //----------------------------------------------------------------------------
  // Public Methods
  //----------------------------------------------------------------------------

  //------------------------------------------------------------------------
  //  Paint the piece.
  //  @see ilm/line/itangram2/TangramPanel.java: paint(Graphics graph)
  //------------------------------------------------------------------------
  public void paint (Graphics graph) {
   graph.setColor((isSelected) ? TangramProperties.PIECE_SELECTED_FILL_COLOR : TangramProperties.PIECE_FILL_COLOR);
   graph.fillPolygon(tangramPiecePolygon);
   graph.setColor((isSelected) ? TangramProperties.PIECE_SELECTED_OUTLINE_COLOR : TangramProperties.PIECE_OUTLINE_COLOR);
   graph.drawPolygon(tangramPiecePolygon);
   }

  //------------------------------------------------------------------------
  //  Select this piece. @see ilm/line/itangram2/TangramPanel.java
  //------------------------------------------------------------------------
  public void setSelected (boolean flag) {
   // if (Tangram.ISDEBUG) System.out.println("src/ilm/line/itangram2/TangramPiece.java: name=" + name);
   isSelected = flag;
   }

  //------------------------------------------------------------------------
  //  Gets the bounds of the piece.
  //------------------------------------------------------------------------
  public Rectangle getBounds () {
   return new Rectangle(tangramPiecePolygon.getBounds());
   }

  //------------------------------------------------------------------------
  //  Tests if the piece contains a point.
  //------------------------------------------------------------------------
  public boolean contains (int x, int y) {
   return tangramPiecePolygon.contains(x, y);
   }

  //------------------------------------------------------------------------
  //  Rotates the piece.
  //------------------------------------------------------------------------
  public void rotate (float angle) {
   rotation += angle;
   while (rotation <    0) rotation += 360;
   while (rotation >= 360) rotation -= 360;
   rotatePoints();
   }

  //------------------------------------------------------------------------
  //  Translates the piece.
  //------------------------------------------------------------------------
  public void translate (int deltaX, int deltaY) {
   xOffset += deltaX;
   yOffset += deltaY;
   tangramPiecePolygon.translate(deltaX, deltaY);
   }

  //------------------------------------------------------------------------
  //  Set the angle of rotatation of the piece.
  //------------------------------------------------------------------------
  public void setRotation (float angle) {
   rotation = angle;
   rotatePoints();
   }

  //------------------------------------------------------------------------
  //  Translates the piece.
  //------------------------------------------------------------------------
  public void setTranslation (int newX, int newY) {
   tangramPiecePolygon.translate(newX-xOffset, newY-yOffset);
   xOffset = newX;
   yOffset = newY;
   }

  //------------------------------------------------------------------------
  //  Tests if one of the points of the passed-in piece is close enough
  //  to one of the points of this piece to be eligible for snapping.
  //  If true, returns the snapping point offset, else returns null.
  //------------------------------------------------------------------------
  public Point snap (TangramPiece other) {
   Point  snappingPoint    = null;
   double snappingDistance = TangramProperties.TANGRAM_PANEL_SNAP_DISTANCE;
   if (other==null) {
    System.err.println(this.getClass().getName() + ": error, no other piece!");
    return null;
    }
   for (int this_cnt = 0; this_cnt < this.xRotatedSnappingPoints.length; this_cnt++) {
    for (int other_cnt = 0; other_cnt < other.xRotatedSnappingPoints.length; other_cnt++) {
     int deltaX  =  this.xRotatedSnappingPoints[this_cnt]  +  this.xOffset;
         deltaX -= other.xRotatedSnappingPoints[other_cnt] + other.xOffset;
     int deltaY  =  this.yRotatedSnappingPoints[this_cnt]  + this.yOffset;
         deltaY -= other.yRotatedSnappingPoints[other_cnt] + other.yOffset;
     double distance = deltaX * deltaX + deltaY * deltaY;
     if (distance <= snappingDistance) {
      snappingDistance = distance;
      snappingPoint    = new Point(deltaX, deltaY);
      }
     }
    }
   //System.out.println("Snapping distance = " + snappingDistance);
   return snappingPoint;
   }

  //-----------------------------------------------------------------------------------------------------------
  //  Get the coordinates and rotation angle of all the unitary triangles that compose this piece.
  //  { 2 * LargeTriangle, MediumTriangle, 2 * SmallTriangle, Square, Lozange } => # = { 8, 4, 2, 2, 4, 4 }
  //  A small triangle has 2 "basic triangles" => Square has 2 SmallTriangle => Square has 4 "basic triangles"
  //-----------------------------------------------------------------------------------------------------------
  public int [][] getComposingUnits () {
   int [][] rotatedUnits = new int[composingUnits.length][3];

   double radians = Math.PI * rotation / 180;
   double cosinus = Math.cos(radians);
   double sinus   = Math.sin(radians);
  
   int [] currentElement = null;
  
   for (int count = 0; count < composingUnits.length; count++) {
    currentElement = composingUnits[count];
    rotatedUnits[count][0] = xOffset + (int) Math.round( cosinus * currentElement[0] + sinus   * currentElement[1]);
    rotatedUnits[count][1] = yOffset + (int) Math.round( -sinus  * currentElement[0] + cosinus * currentElement[1]);
    rotatedUnits[count][2] = (int) (currentElement[2] + rotation + 720) % 360;
    }
   return rotatedUnits;
   }


  //----------------------------------------------------------------------------
  // Private Helper Methods
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Rotates the points of the piece
  //    Transformation matrix for rotation angle a:
  //       [ x' ]   [ cos(a)   -sin(a) ] [ x ]     /  x' =  cos(a) * x - sin(a) * y
  //       [    ] = [                  ] [   ] => <
  //       [ y' ]   [ sin(a)    cos(a) ] [ y ]     \  y' =  sin(a) * x + cos(a) * y
  //
  //    ATTENTION in iTangram it is using anti-clockwise, i.e., 
  //         x' =  cos(a) * x + sin(a) * y
  //         y' = -sin(a) * x + cos(a) * y
  //----------------------------------------------------------------------------

  // Rotate points
  private void rotatePoints () {
   double radians = Math.PI * rotation / 180;
   double cosinus = Math.cos(radians);
   double sinus   = Math.sin(radians);

   int [] rotated_xPoints = new int[xPoints.length];
   int [] rotated_yPoints = new int[xPoints.length];
   for (int cnt = 0; cnt < xPoints.length; cnt++) {
    rotated_xPoints[cnt] = (int) Math.round( cosinus * xPoints[cnt] + sinus   * yPoints[cnt]); //ATTENTION: here in THE system it is been
    rotated_yPoints[cnt] = (int) Math.round(-sinus   * xPoints[cnt] + cosinus * yPoints[cnt]); //           used anti-clockwise
    }
   //D System.err.println("TangramPiece.rotatePoints(): (" + xPoints[0] + "," + yPoints[0] + ") -> (" + rotated_xPoints[0] + "," + rotated_yPoints[0] + ")");
   tangramPiecePolygon = new Polygon(rotated_xPoints, rotated_yPoints, xPoints.length);
   tangramPiecePolygon.translate(xOffset, yOffset);

   // Polygon for snapping behaviour
   if (xSnappingPoints == null || ySnappingPoints == null) {
    System.err.println("Snapping Points Not Defined for: " + name);
    xRotatedSnappingPoints = rotated_xPoints;
    yRotatedSnappingPoints = rotated_yPoints;
    return;
    }
   xRotatedSnappingPoints = new int[xSnappingPoints.length];
   yRotatedSnappingPoints = new int[xSnappingPoints.length];
   for (int cnt = 0; cnt < xSnappingPoints.length; cnt++) {
    xRotatedSnappingPoints[cnt] = (int) Math.round( cosinus * xSnappingPoints[cnt] + sinus   * ySnappingPoints[cnt]);
    yRotatedSnappingPoints[cnt] = (int) Math.round(-sinus   * xSnappingPoints[cnt] + cosinus * ySnappingPoints[cnt]);
    }

   } // private void rotatePoints()


  } // class TangramPiece
