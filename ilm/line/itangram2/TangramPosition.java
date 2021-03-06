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
 * @description A set of all 7 pieces and their associated data. Used with 'TangramModel' (model) and 'TangramPanel' (learner pieces)
 * 
 * @see ilm/line/itangram2/TangramModel.java: constructor makes 'modelData = new TangramModelData();'
 * @see ilm/line/itangram2/TangramPanel.java: positionPieces(boolean) makes 'tangramPosition = new TangramPosition();'
 *  
 * @credits
 * This source is free and provided by iMath Project (University of S�o Paulo - Brazil). In order to contribute, please
 * contact the iMath coordinator Leo^nidas O. Branda~o.
 * The original code is from 'javapage@serger.biz'.
 *
 * O co'digo fonte deste programa e' livre e desenvolvido pelo projeto iMa'tica (Universidade de Sa~o Paulo). Para contribuir,
 * por favor contate o coordenador do projeto iMatica, professor Leo^nidas O. Branda~o.
 * Este sistema foi baseado no co'digo original de 'javapage@serger.biz'.
 * 
 */

package ilm.line.itangram2;

class TangramPosition {


 //------------------------------------------------------------------
 //  Static Methods
 //------------------------------------------------------------------

  //------------------------------------------------------------------------
  //  Get the Composing Units of an array of TangramPieces.
  //------------------------------------------------------------------------
  // Number of 'unitary triangle' that compose eachr piece: ilm/line/itangram2/TangramPiece.java
  // * 0 = "LargeTriangle"  : 8 = 2 "MediumTriangle" = 8 "basic triangles"
  // * 1 = "LargeTriangle"  : 8
  // * 2 = "MediumTriangle" : 4 = 2 "SmallTriangle"  = 4 "basic triangles"
  // * 3 = "SmallTriangle"  : 2 =                      2 "basic triangles"
  // * 4 = "SmallTriangle"  : 2
  // * 5 = "Square"         : 4 = 2 "SmallTriangle"  = 4 "basic triangles"
  // * 6 = "Lozange"        : 4 = 2 "SmallTriangle"  = 4 "basic triangles"
  static int [][] getComposingUnits (TangramPiece [] pieces) {
   int [][] units = new int[32][]; //
   int [][] currentPiece;
   int poscount = 0;
     
   //T System.out.println("TangramPosition.java: getComposingUnits(...):");
   for (int cnt = 0; cnt < pieces.length; cnt++) {
     // ilm/line/itangram2/TangramPiece.java: int[32][3], where second is 'rad=PI*rotation/180, cos(rad), sinus(rad)' (radians)
     currentPiece = pieces[cnt].getComposingUnits();
     //T System.out.println(" " + cnt + ": pieces.length=" + pieces.length + ": " + currentPiece.length);
     // Stuffing my array
     for (int ncnt = 0; ncnt < currentPiece.length; ncnt++) {
      units[poscount++] = currentPiece[ncnt];
      }
     }
   units = translateToOrigin(units);
   return units;
   }

  static int pieceByBasicTriangles (int i) {
     if (i<8)  return 0; // LargeTriangle
     if (i<16) return 1; // LargeTriangle
     if (i<20) return 2; // MediumTriangle
     if (i<22) return 3; // SmallTriangle
     if (i<24) return 4; // SmallTriangle
     if (i<28) return 5; // Square
     //if (i<32)
     return 6; // Lozange
     }
   
  //------------------------------------------------------------------------
  //  Translate the Composing Units or Pieces to the origin.
  //------------------------------------------------------------------------
  static int [][] translateToOrigin (int [][] units) {
   int x_min = units[0][0];
   int y_min = units[0][1];
  
   for (int cnt = 0; cnt < units.length; cnt++) {
     if (units[cnt][0] < x_min) x_min = units[cnt][0];
     if (units[cnt][1] < y_min) y_min = units[cnt][1];
     }
   for (int cnt = 0; cnt < units.length; cnt++) {
     units[cnt][0] -= x_min;
     units[cnt][1] -= y_min;
     }
   return units;
   }


  //------------------------------------------------------------------
  //  Data
  //------------------------------------------------------------------
  private TangramPiece [] tangramPieces = new TangramPiece[7];
  public  TangramPiece [] getTangramPieces () { return tangramPieces;  }

  // The composing units of the current position.
  private int [][] composingUnits;
  public  int [][] getComposingUnits () { return composingUnits;  }


  //------------------------------------------------------------------
  //  Constructor
  //------------------------------------------------------------------
  public TangramPosition () {
   tangramPieces[0] = new TangramPiece(TangramPiece.TANGRAM_FORM_LARGE_TRIANGE); // "LargeTriangle"
   tangramPieces[1] = new TangramPiece(TangramPiece.TANGRAM_FORM_LARGE_TRIANGE); // "LargeTriangle"
   tangramPieces[2] = new TangramPiece(TangramPiece.TANGRAM_FORM_MEDIUM_TRIANGE); // "MediumTriangle"
   tangramPieces[3] = new TangramPiece(TangramPiece.TANGRAM_FORM_SMALL_TRIANGE); // "SmallTriangle"
   tangramPieces[4] = new TangramPiece(TangramPiece.TANGRAM_FORM_SMALL_TRIANGE); // "SmallTriangle"
   tangramPieces[5] = new TangramPiece(TangramPiece.TANGRAM_FORM_SQUARE);        // "Square"
   tangramPieces[6] = new TangramPiece(TangramPiece.TANGRAM_FORM_LOZENGE);       // "Lozange"
   }


  static int count=0;

  //DEBUG
  public static String getCorners (TangramPiece tangramPiece) {
   String str = "";
   int [] x = tangramPiece.getXpoints();
   int [] y = tangramPiece.getYpoints();
   for (int i=0; i<x.length; i++)
      str += "(" + x[i] + "," + y[i] + ") ";
   return str;
   }

  //DEBUG
  public static int getMinCoordX (TangramPiece tangramPiece) {
   int [] x = tangramPiece.getXpoints();
   int xmin = x[0];
   for (int i=1; i<x.length; i++)
      if (x[i] < xmin)
        xmin = x[i];
   return xmin;
   }

  public static int getMinCoordY (TangramPiece tangramPiece) {
   int [] y = tangramPiece.getYpoints();
   int ymin = y[0];
   for (int i=1; i<y.length; i++)
      if (y[i] < ymin)
        ymin = y[i];
   return ymin;
   }


  private static int countGetXY = 0;

  //----------------------------------------------------------------------------
  //  Get top most piece (their coordinates (x,y))
  //  @see TangramPanel.java: protected void positionPieces(boolean fromOutSide): tangramPosition.loadFromString(TangramModelData.start_position, "position0") => correctIt = true
  //  @see TangramModel.java: public void loadModelPosition (String modelString): modelPosition.loadFromString(modelString, "model");                          => correctIt = false
  //----------------------------------------------------------------------------
  private static int [] getMinXY (int [][] piecesComposingUnitsParam, TangramPiece [] tangramPieces, boolean correctIt) {

   //System.out.println("\ngetMinXY(): " + (correctIt ? "TangramPanel" : "Model"));
   //try { String str__=""; System.out.print(str__.charAt(3)); } catch (Exception except) { except.printStackTrace(); }
   countGetXY++;

   if (piecesComposingUnitsParam==null)
     return new int [] { Integer.MIN_VALUE, Integer.MIN_VALUE };

   int [] lineCoordX;
   int [] lineCoordY;
   int numberOfPieces = piecesComposingUnitsParam.length;
   int sizeOf;
   int xmin = Integer.MIN_VALUE, ymin = Integer.MIN_VALUE;
   int x_coordX, y_coordY, angle;
   int xminPiece, yminPiece; // temporary: minimum for each piece

   lineCoordX = tangramPieces[0].getXpoints();
   lineCoordY = tangramPieces[0].getYpoints();
   xmin = lineCoordX[0];
   ymin = lineCoordY[0];

   for (int ii_=0; ii_<numberOfPieces; ii_++) {
     x_coordX = piecesComposingUnitsParam[ii_][0];
     y_coordY = piecesComposingUnitsParam[ii_][1];
     angle = piecesComposingUnitsParam[ii_][2];

     //D String str = getCorners(tangramPieces[ii_]);
     tangramPieces[ii_].translate(x_coordX, y_coordY);
     tangramPieces[ii_].rotate(angle);
     //D System.out.println( " " + ii_ + ": angle=" + angle + ": " + tangramPieces[ii_].getName() + ":" + getCorners(tangramPieces[ii_]));

     lineCoordX = tangramPieces[ii_].getXpoints(); // TangramPiece.xPoints
     lineCoordY = tangramPieces[ii_].getYpoints(); // TangramPiece.yPoints

     xminPiece = getMinCoordX(tangramPieces[ii_]);
     yminPiece = getMinCoordY(tangramPieces[ii_]);
     if (xminPiece<xmin)
       xmin = xminPiece;
     if (yminPiece<ymin) {
       ymin = yminPiece;
       // exerc_triangle1.itg2       - NAO
       // exerc_triangle1_resp2.itg2 - SIM

       if (y_coordY==102) { //TODO: why only with Lozange? Only with 102 (angle=225)?
         //D System.out.println("\nTangramPosition.java: getMinXY(): x_coordX=" + x_coordX + ", y_coordY=" + y_coordY + "------------------------------------ countGetXY="+countGetXY);
         //D System.out.println("TangramPosition.java: getMinXY(): final = ("+xmin+","+ymin+")\n");
         //D System.out.println( " " + ii_ + ": angle=" + angle + ": " + tangramPieces[ii_].getName() + ":" + getCorners(tangramPieces[ii_]));
         ymin -= TangramProperties.TRANS_YN; // extra shift to avoid the peak do not appears!
         }

       }

     //D System.out.println("TangramPosition.java: getMinXY(): ("+xmin+","+ymin+")\n");
     } // for (int ii_=0; ii_<numberOfPieces; ii_++)

   return new int [] { xmin, ymin };
   } // private static int [] getMinXY(int [][] piecesComposingUnitsParam, TangramPiece [] tangramPieces)

  // TangramPanel.java:
  // public TangramPanel (Tangram tangram) 
  //   positionPieces(false);
  // public void loadPosition0 (): comentei :  pois nao era de fora de 'TangramPanel.java'...
  //    positionPieces(true); // true => from outside of 'TangramPanel.java'

  //----------------------------------------------------------------------------
  //  Compute Margin: static method to be used in 'TangramModel' and 'TangramPosition'
  //----------------------------------------------------------------------------

  // If the registered positioning with any vertex before (0,0) => translate all to fix it
  // Positioning string 'strPositioning' has 28 characters with upper and lower case letters ('0'-'9', 'A'-'Z', 'a'-'z')
  // Each piece has 4 int descriptor: #piecesComposingUnitsParam[i] = 3 :: (x,y,r)
  // 
  // * correctIt = true  => came from "TangramPanel" : position0
  // * correctIt = false => came from "TangramModel" : model
  protected static int [] findCorrectTopCorner (int [][] piecesComposingUnitsParam, TangramPiece [] tangramPieces, boolean correctIt) {
   int imin=0, jmin=0; //D index
   int xmin = 0, ymin = 0;
   int xpoint = 0, ypoint = 0;
   boolean needCorrection = false;
   int xTrans = 0, yTrans = 0;
   int [] translationToCorrect = { 0,0, 0,0, 0,0 };
   String str0="",str1="",str2="",strX="",strY="";

   // piecesComposingUnitsParam.length==32 => from model => uses "basic triangles" (big triangle with 8 and small with 2)
   //DEBUG if (piecesComposingUnitsParam.length==7 && correctIt) 
   // try { String str__=""; System.out.print(str__.charAt(3)); } catch (Exception except) { except.printStackTrace(); }

   translationToCorrect = getMinXY(piecesComposingUnitsParam, tangramPieces, correctIt);
   xmin = translationToCorrect[0];
   ymin = translationToCorrect[1];
   int xmargin = TangramProperties.TANGRAM_WORKING_DISPLAY_MARGIN - xmin, // xTrans, //
       ymargin = TangramProperties.TANGRAM_WORKING_DISPLAY_MARGIN - ymin; // yTrans; //

   //D System.out.println("TangramPosition.java: findCorrectTopCorner: xmin="+xmin+", ymin="+ymin+", ("+xmargin+","+ymargin+")");
   for (int cnt=0; cnt < piecesComposingUnitsParam.length; cnt++) {
     piecesComposingUnitsParam[cnt][0] += xmargin; // coodinate X
     piecesComposingUnitsParam[cnt][1] += ymargin; // coodinate Y
     translationToCorrect[0] = xmin;
     translationToCorrect[1] = ymin;
     }

   return translationToCorrect;
   } // protected static int [] findCorrectTopCorner(int [][] piecesComposingUnitsParam, boolean correctIt)



  //-----------------------------------------------------------------------------------------------------------
  //  Load the Position from a String Representation to model (TangramModel) and working area (TangramPosition)
  //  Each set o iTangram pieces has its associated positioning (from TangramPosition)
  // 
  //  @see ilm/line/itangram2/TangramPanel.java: protected void positionPieces(boolean fromOutSide): tangramPosition.loadFromString(TangramModelData.start_position, "position0");
  //  @see ilm/line/itangram2/TangramModel.java: public void loadModelPosition (String modelString): modelPosition.loadFromString(modelString, "model");
  //
  //-----------------------------------------------------------------------------------------------------------
  public void loadFromString (String str, String strModelOrWork) throws Exception {
   // Positioning string 'strPositioning' has 28 characters with upper and lower case letters ('0'-'9', 'A'-'Z', 'a'-'z')
   if (str==null || str.trim().length()==0) { // empty model => stundent must provide some free draw
      return;
      }
   //D System.err.println("TangramPosition.java: loadFromString: *** str=" + str);

   int [][] position = TangramPositionCoder.decodePosition(str); // each piece has 4 int
   int []   currentPiece;
   int []   translationToCorrect;
   int xTrans=0, yTrans=0, xmin, ymin;
   if (position == null) {
     System.err.println("TangramPosition.java: ERROR: position null...\ndecode=" + str);
     throw new Exception("Invalid String Code Argument");
     }

   // "TangramPanel" : position0
   // "TangramModel" : model
   if (strModelOrWork!=null && strModelOrWork.equals("position0")) // it is from TangramPanel
     translationToCorrect = findCorrectTopCorner(position, tangramPieces, true); // for each "basic triangle", get its smallest (considering signal) one
   else { // it is from TangramModel
     translationToCorrect = findCorrectTopCorner(position, tangramPieces, false); // for each "basic triangle", get its smallest (considering signal) one
     //DEBUG //if (count++==2) { String str_=""; try { System.out.println(str_.charAt(3)); } catch (Exception e) { e.printStackTrace(); } }
     }

   xmin = translationToCorrect[0];
   ymin = translationToCorrect[1];
   //D System.out.println("ilm/line/itangram2/TangramPosition.java: loadFromString " + strModelOrWork + ": (xmin,ymin)=(" + xmin + "," + ymin +")");

   // Use the minimal coordinator to shift all pieces
   int pos_x, pos_y;
   int [] margins = new int [] { TangramProperties.TANGRAM_WORKING_DISPLAY_MARGIN - xmin, TangramProperties.TANGRAM_WORKING_DISPLAY_MARGIN - ymin };

   xTrans += TangramProperties.TANGRAM_WORKING_DISPLAY_MARGIN + TangramProperties.TRANS_X0;
   yTrans += TangramProperties.TANGRAM_WORKING_DISPLAY_MARGIN + TangramProperties.TRANS_Y0;

   // if (Tangram.ISDEBUG) System.out.println("src/ilm/line/itangram2/TangramPosition.java: working pieces positioning: piece 0: (x,y)=(" + position[0][0] + "," + position[0][1] + "): (xTrans,yTrans)=("+xTrans+","+yTrans+")");
   // if (Tangram.ISDEBUG) System.out.println("src/ilm/line/itangram2/TangramPosition.java: " + strModelOrWork + ": working pieces positioning: (xTrans,yTrans)=("+xTrans+","+yTrans+")");

   for (int count = 0; count < position.length; count++) {
     currentPiece = position[count];
     pos_x = currentPiece[0] + xTrans;
     pos_y = currentPiece[1] + yTrans;
     // TangramPiece: setTranslation (int newX, int newY): tangramPiecePolygon.translate(newX-xOffset, newY-yOffset); xOffset = newX; yOffset = newY;
     tangramPieces[count].setTranslation(pos_x, pos_y);
     tangramPieces[count].setRotation(currentPiece[2]);
     }

   //_ System.out.println("src/ilm/line/itangram2/TangramPosition.java: #TangramPosition.id=" + this.id + ", #position=" + position.length);
   updateComposingUnits(); // composingUnits = getComposingUnits(tangramPieces);
   }


  // Rotation
  // [ x1 ]   [ cosinus  -sinus ] [ x0 ]
  // [    ] = [                 ] [    ]
  // [ y1 ]   [   sinus cosinus ] [ y0 ]

  //DEBUG: to trace which 'TangramPosition.java'
  private static int idS=0;
  public final int id=idS++;

  //------------------------------------------------------------------
  //  Update Composing Units
  //------------------------------------------------------------------
  public void updateComposingUnits () {
   composingUnits = getComposingUnits(tangramPieces);
   }


  //------------------------------------------------------------------
  //  Write a String Representation of the Position
  //------------------------------------------------------------------
  public String writeToString () {
   //D System.out.println("src/ilm/line/itangram2/TangramPosition.java: writeToString()");
   int [][] position = new int[7][3];
   for (int count = 0; count < position.length; count++) {
     position[count][0] = tangramPieces[count].getXOffset(); // + TangramProperties.TRANS_X0;
     position[count][1] = tangramPieces[count].getYOffset(); // + TangramProperties.TRANS_Y0;
     position[count][2] = tangramPieces[count].getRotation();
     //D System.out.println( " " + count + ": " + position[count][0] + "," + position[count][1] + "," + position[count][2]);

     }
   position = translateToOrigin(position); //
   return TangramPositionCoder.encodePosition(position);
   }


  //----------------------------------------------------------------------------
  //  Test a passed-in position against this model
  //  @calledby ilm/line/itangram2/Tangram.java: after each piece's movement (fromEvalButton=false) or after click in eval button (fromEvalButton=true)
  //            boolean comparePositionWithModel(boolean fromTangramPanel): boolean result = (modlPos.equals(currPos, fromEvalButton));
  //----------------------------------------------------------------------------
  public boolean equals (TangramPosition candidate, boolean fromEvalButton) {
   int [][] thisUnits = this.composingUnits; // from 'TangramPosition modlPos'
   int [][] candUnits = candidate.composingUnits; // from 'TangramPosition currPos'

   //D String str_=""; try { System.out.println(str_.charAt(3)); } catch (Exception e) { e.printStackTrace(); }

   float dist_min = Float.POSITIVE_INFINITY, dist;
   boolean result = true;
   int [] currentTriangle;

   if (thisUnits==null) { // empty model => stundent must provide some free draw
      return false;
      }

   if (fromEvalButton) // came from evaluation button, presents the distance between "answer model" and "current positioning"
     System.out.println("TangramPosition.java: equals(...): thisUnits.length=" + thisUnits.length); //DEBUG

   // Try to match each piece from the model 'modlPos' with one from the 'currPos'
   boolean error = false;
   outer:
   for (int cnt1 = 0; cnt1 < thisUnits.length; cnt1++) {
     dist_min = Float.POSITIVE_INFINITY;
     inner:
     for (int cnt2 = 0; cnt2 < candUnits.length; cnt2++) { // this.composingUnits
       dist = Math.abs(thisUnits[cnt1][0] - candUnits[cnt2][0]) + Math.abs(thisUnits[cnt1][1] - candUnits[cnt2][1]) + Math.abs(thisUnits[cnt1][2] - candUnits[cnt2][2]);
       if (dist < dist_min)
         dist_min = dist;
       // if ( (thisUnits[cnt1][0] == candUnits[cnt2][0]) && (thisUnits[cnt1][1] == candUnits[cnt2][1]) && (thisUnits[cnt1][2] == candUnits[cnt2][2]) ) continue outer;
       if (dist == 0) {
         if (fromEvalButton) { // came from evaluation button, presents the distance between "answer model" and "current positioning"
           int i = pieceByBasicTriangles(cnt1), j =  pieceByBasicTriangles(cnt2); // ilm/line/itangram2/TangramPiece.java: getName
             System.out.println("TangramPosition.java: [" + cnt1 + "," + cnt2 + "] : piece " + i + " = " + j + " (" + this.tangramPieces[i].getName() + "=" + this.tangramPieces[j].getName() + ")"); //DEBUG
           }
         continue outer;
         }
       } // for (int cnt2 = 0; cnt2 < candUnits.length; cnt2++)

     if (fromEvalButton) { // came from evaluation button, presents the distance between "answer model" and "current positioning"
       // If for the piece cnt1 it is not equal, return false...
       int i = pieceByBasicTriangles(cnt1); // ilm/line/itangram2/TangramPiece.java: getName
       System.out.println("TangramPosition.java: not equals to piece " + i + " (" + this.tangramPieces[i].getName() + "): " + dist_min + ""); //DEBUG
       // System.out.println("TangramPosition.java: not equals to piece cnt1=" + cnt1 + ": " + dist_min + "");
       }

     // return false;
     error = true;

     } // for (int cnt1 = 0; cnt1 < thisUnits.length; cnt1++)

   System.out.println("TangramPosition.java: equals: " + dist_min); //DEBUG

   if (error) return false;
   return true;
   } //public boolean equals(TangramPosition candidate)


  } // class TangramPosition
