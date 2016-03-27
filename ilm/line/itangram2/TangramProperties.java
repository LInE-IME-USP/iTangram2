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
 * @description Class that holds the properties of the Tangram.
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

import ilm.line.util.Bundle;

import java.awt.*;


public class TangramProperties {

 //------------------------------------------------------------------
 //  Constructor
 //------------------------------------------------------------------
 public TangramProperties () {
  }

 //----------------------------------------------------------------------------
 //  Colors
 //----------------------------------------------------------------------------

 public static final Color TANGRAM_PANEL_BACKGROUND_BUTTONS = new Color(190, 200, 220); // bg color of a block panel (Label and buttons)
 public static final Color CONTROLS_PANEL_BACKGROUND_COLOR = new Color(150, 180, 210); // Color.lightGray // color panel with model and buttons

 /*
 //TODO permitir gravar cores personalizadas
 Color background dark
 public static final Color TANGRAM_PANEL_BACKGROUND_BORDER  = Color.white; // border of 'black model' and 'black working area'
 // Working area
 public static final Color TANGRAM_PANEL_BACKGROUND_COLOR   = Color.black; // 'black model' and 'black working area'
 public static final Color PIECE_FILL_COLOR                = new Color(220, 220, 240); // Color.lightGray
 public static final Color PIECE_SELECTED_FILL_COLOR       = new Color(120, 120, 240); // Color.orange
 public static final Color PIECE_OUTLINE_COLOR             = Color.blue; // Color.white
 public static final Color PIECE_SELECTED_OUTLINE_COLOR    = Color.white;

 // Model
 public static final Color CONTROLS_PANEL_BACKGROUND_COLOR = new Color(150, 180, 210); // Color.lightGray
 public static final Color HELP_PANEL_BACKGROUND_COLOR     = new Color(190, 200, 220);
 public static final Color HELP_TEXT_BACKGROUND_COLOR      = new Color(220, 220, 240);
 public static final Color MODEL_FILL_COLOR                = PIECE_FILL_COLOR; // Color.lightGray
 public static final Color MODEL_BACKGROUND_COLOR          = Color.black;
 */

 // Lighter color
 public static final Color TANGRAM_PANEL_BACKGROUND_BORDER  = Color.black; // Color.white; // border of 'black model' and 'black working area'
 // Working area
 public static final Color TANGRAM_PANEL_BACKGROUND_COLOR  = new Color(220, 220, 230); // Color.lightGray Color.white; // 'black model' and 'black working area'
 public static final Color PIECE_FILL_COLOR                = new Color(100, 100, 120); // Color.lightGray Color.gray; //
 public static final Color PIECE_SELECTED_FILL_COLOR       = new Color(120, 120, 240); // Color.orange
 public static final Color PIECE_OUTLINE_COLOR             = Color.black; // Color.blue; // Color.white
 public static final Color PIECE_SELECTED_OUTLINE_COLOR    = Color.white;

 // Model
 public static final Color HELP_PANEL_BACKGROUND_COLOR     = new Color(190, 200, 220);
 public static final Color HELP_TEXT_BACKGROUND_COLOR      = new Color(220, 220, 240);
 public static final Color MODEL_FILL_COLOR                = PIECE_FILL_COLOR; // Color.lightGray
 public static final Color MODEL_BACKGROUND_COLOR          = TANGRAM_PANEL_BACKGROUND_COLOR; // Color.black;


 //----------------------------------------------------------------------------
 //  Fonts
 //----------------------------------------------------------------------------

 public static final Font  FONT_DEFAULT = new Font("Helvetica", Font.BOLD, 11);
   // Font("Dialog", Font.BOLD, 12);
   

 //----------------------------------------------------------------------------
 //   Speciall to button configuration 'TangramButton.java'
 //----------------------------------------------------------------------------
 public static final Font
   ftPlain10     = new Font ("Helvetica", Font.PLAIN, 10); // TangramButton.java

 public static final Color COR_FUNDO = new Color(255, 241, 168); // cor bordo
 public static final Color COR_FUNDO_LOGO = new Color(145, 152, 163); // background color of superior panel (one of the colors in 'IMG_HELP') : 9198a3=145,152,163 dark color in 'img/img_logo_bg.gif'
 public static final int cursorPadrao     = Cursor.DEFAULT_CURSOR;   // ao passar mouse sobre objeto
 public static final int cursorPega       = Cursor.CROSSHAIR_CURSOR; // ao passar mouse sobre objeto

 public static final int ITANGRAM_WIDTH  = 800; // frame size of iTangram
 public static final int ITANGRAM_HEIGTH = 600; 

 public static final int largBt = 18, altBt = 18; // ilm/line/itangram2/TangramButton.java: re-dimensioning buttons
 public static final int DELTAx        = 10; // ilm/line/itangram2/TangramButton.java: deslocamento vertical adicional
 public static final int DELTAy        = 10; // ilm/line/itangram2/TangramButton.java: deslocamento horizontal adicional: para mostrar msg inteira
 public static final int ALTURA_LINHA  = 15; // altura de cada linha de msg (msg grandes podem ter \n)
 public static final int ALTURA_BOTAO  = 32; // altura dos botoes
 public static final int LARGURA_BOTAO = 32; // largura dos botoes
 public static final int BOTAO_OFFSET  = 32; // primario => acima do bota; secundario => abaixo do botao
 public static final int tamX          = 50; // para o caso de erro de leitura das imagens
 public static final int tamY          = 50;
 public static final String IMG_LOGOTOP = "img/img_logo_bg.gif"; // image logo - 60x30     
 public static final String IMG_EVALUATE = "img/button_exerc_evaluate.gif"; // image to button 'Evaluate' - 32x30
 public static final String IMG_HELP = "img/button_help.gif"; // image to button 'Help' - 32x30
 public static final String IMG_PREV = "img/button_prev.gif"; //
 public static final String IMG_NEXT = "img/button_next.gif"; // 
 public static final String IMG_GETCODE = "img/button_getcode.gif"; // 
 public static final String IMG_ENTERCODE = "img/button_entercode.gif"; // 


 //----------------------------------------------------------------------------
 //  Captions
 //----------------------------------------------------------------------------

 public static final String TANGRAM_FRAME_TITLE                = "TANGRAM_FRAME_TITLE";                //_ "Tangram"

 public static final String CAPTION_CONTROL_MODEL_LABEL        = "CAPTION_CONTROL_MODEL_LABEL";        //_ "Model "
 public static final String CAPTION_CONTROL_BUTTON1            = "CAPTION_CONTROL_BUTTON1";            //_ "Model to be built"
 public static final String CAPTION_CONTROL_BUTTON2            = "CAPTION_CONTROL_BUTTON2";

 public static final String CAPTION_CONTROL_HELP_BUTTON        = "CAPTION_CONTROL_HELP_BUTTON";        //_ "Help"
 public static final String CAPTION_CONTROL_EVALUATE_BUTTON    = "CAPTION_CONTROL_EVALUATE_BUTTON";
 public static final String CAPTION_CONTROL_PREV_BUTTON        = "CAPTION_CONTROL_PREV_BUTTON";        //_ "Previous"
 public static final String CAPTION_CONTROL_NEXT_BUTTON        = "CAPTION_CONTROL_NEXT_BUTTON";        //_ "Next"
 public static final String CAPTION_CONTROL_COMPARE_BUTTON     = "CAPTION_CONTROL_COMPARE_BUTTON";     //_ "Compare"

 public static final String CAPTION_CONTROL_GETCODE_BUTTON     = "CAPTION_CONTROL_GETCODE_BUTTON";     //_ "Get Code"
 public static final String EXPLAIN_CONTROL_GETCODE            = "EXPLAIN_CONTROL_GETCODE";            //_ "Get the code for the current positioning"
 public static final String CAPTION_CONTROL_ENTERCODE_BUTTON   = "CAPTION_CONTROL_ENTERCODE_BUTTON";   //_ "Enter Code"
 public static final String EXPLAIN_CONTROL_ENTERCODE          = "EXPLAIN_CONTROL_ENTERCODE";          //_ "Use the current positioning to define the model"

 public static final String CAPTION_CONTROL_ROTATIONS_CHECKBOX = "CAPTION_CONTROL_ROTATIONS_CHECKBOX"; //_ "Small Rotations"


 //----------------------------------------------------------------------------
 //  Messages
 //----------------------------------------------------------------------------

 public static final String DIALOG_COMPARE_TITLE   = "DIALOG_COMPARE_TITLE"; //_ "Comparing with the model";
 public static final String DIALOG_COMPARE_MESSAGE = "DIALOG_COMPARE_MESSAGE"; //_ "Congratulations!  You solved this puzzle.";

 public static final String DIALOG_GETCODE_TITLE   = "DIALOG_GETCODE_TITLE"; //_ "Get Code";
 public static final String DIALOG_ENTERCODE_TITLE = "DIALOG_ENTERCODE_TITLE"; //_ "Enter Code";
 public static final String DIALOG_HELP_TITLE      = "DIALOG_HELP_TITLE"; //_ "Tangram Help";

 public static final String DIALOG_GETCODE_MESSAGE         = "DIALOG_GETCODE_MESSAGE"; //_ "This is the code for your current position";
 public static final String DIALOG_ENTERCODE_ERROR_TITLE   = "DIALOG_ENTERCODE_ERROR_TITLE"; //_ "Error";
 public static final String DIALOG_ENTERCODE_ERROR_MESSAGE = "DIALOG_ENTERCODE_ERROR_MESSAGE"; //_ "Invalid Code";
 public static final String DIALOG_ENTERCODE_MESSAGE       = "DIALOG_ENTERCODE_MESSAGE"; //_ "Enter a code to load the corresponding model";

 public static final String WIN_MESSAGE          = "WIN_MESSAGE"; //_ "Congratulations!";
 public static final String LOSE_MESSAGE         = "LOSE_MESSAGE"; //_ "Nope, try again!";
 private static final String HELP_MESSAGE = buildHelpMessage(); //DEBUG: do not use! Instead use 'buildHelpMessage()' to allow language change
 public static final int HELP_DIALOG_WIDTH  = 450;
 public static final int HELP_DIALOG_HEIGHT = 300;

 public static String buildHelpMessage() {
  String message = "";
  message += Bundle.msg("aboutTangramMsg1"); //_ "\n  In a Few Words";
  message += Bundle.msg("aboutTangramMsg2"); //_ "\n---------------------------";
  message += Bundle.msg("aboutTangramMsg3"); //_ "\nUse the \"Previous\" and \"Next\" buttons to navigate the models.";
  message += Bundle.msg("aboutTangramMsg4"); //_ "  Use the mouse and arrow keys to reproduce the shape of the model with all the 7 pieces of the tangram.";
  message += Bundle.msg("aboutTangramMsg5"); //_ "  The pieces cannot overlap.";
  message += Bundle.msg("aboutTangramMsg6"); //_ "  You automatically get a message when you get the right answer.";
  message += Bundle.msg("aboutTangramMsg7"); //_ "\nTo create your own model, compose your shape with the pieces.";
  message += Bundle.msg("aboutTangramMsg8"); //_ "  Then use the \"Get Code\" button and copy the code (it represents your new model).";
  message += Bundle.msg("aboutTangramMsg9"); //_ "  To load your new model, use the \"Enter Code\" button and enter your code.";
  message += Bundle.msg("aboutTangramMsg10"); //_ "\n";
  message += Bundle.msg("aboutTangramMsg11"); //_ "\n  Goal";
  message += Bundle.msg("aboutTangramMsg12"); //_ "\n---------------------------";
  message += Bundle.msg("aboutTangramMsg13"); //_ "\nThe goal of this game is to reproduce the shape of the model using all the 7 pieces of the tangram.";
  message += Bundle.msg("aboutTangramMsg14"); //_ "  The pieces cannot overlap.";
  message += Bundle.msg("aboutTangramMsg15"); //_ "\n";
  message += Bundle.msg("aboutTangramMsg16"); //_ "\n  Controls";
  message += Bundle.msg("aboutTangramMsg17"); //_ "\n---------------------------";
  message += Bundle.msg("aboutTangramMsg18"); //_ "\nUse the mouse to move the pieces.";
  message += Bundle.msg("aboutTangramMsg19"); //_ "\nUse the arrow keys to rotate the pieces.";
  message += Bundle.msg("aboutTangramMsg20"); //_ "\n";
  message += Bundle.msg("aboutTangramMsg21"); //_ "\n  Buttons";
  message += Bundle.msg("aboutTangramMsg22"); //_ "\n---------------------------";
  message += Bundle.msg("aboutTangramMsg23"); //_ "\nPrevious:  shows the previous model.";
  message += Bundle.msg("aboutTangramMsg24"); //_ "\nNext:  shows the next model.";
  message += Bundle.msg("aboutTangramMsg25"); //_ "\nGet Code:  gets the code that corresponds to your current position.";
  message += Bundle.msg("aboutTangramMsg26"); //_ "\nEnter Code:  loads the model that corresponds the code you enter.";
  message += Bundle.msg("aboutTangramMsg27"); //_ "\n";
  message += Bundle.msg("aboutTangramMsg28"); //_ "\n  Checkboxes";
  message += Bundle.msg("aboutTangramMsg29"); //_ "\n---------------------------";
  message += Bundle.msg("aboutTangramMsg30"); //_ "\nSmall Rotations:  when selected, rotations are 15° instead of 45°.";
  message += Bundle.msg("aboutTangramMsg31"); //_ "\n";
  message += Bundle.msg("aboutTangramMsg32"); //_ "\n  Snapping";
  message += Bundle.msg("aboutTangramMsg33"); //_ "\n---------------------------";
  message += Bundle.msg("aboutTangramMsg34"); //_ "\nThe tangram pieces have snapping points.";
  message += Bundle.msg("aboutTangramMsg35"); //_ " When you drop a piece, if a snapping point of the dropped piece is close enough";
  message += Bundle.msg("aboutTangramMsg36"); //_ " to the snapping point of another piece, the dropped piece will automatically";
  message += Bundle.msg("aboutTangramMsg37"); //_ " move so that the two snapping points overlap.";
  message += Bundle.msg("aboutTangramMsg38"); //_ " It's a little hard to explain, but it's easy to understand when you try it out.";
  message += Bundle.msg("aboutTangramMsg39"); //_ " A dropped piece will prefer to snap to the pieces that were last moved.";
  message += Bundle.msg("aboutTangramMsg40"); //_ "\nThe corners of the pieces are all snapping points.";
  message += Bundle.msg("aboutTangramMsg41"); //_ "\nThere are additional snapping points on the sides of the pieces:";
  message += Bundle.msg("aboutTangramMsg42"); //_ "\nSmall triangle: 1 point on each side.";
  message += Bundle.msg("aboutTangramMsg43"); //_ "\nMedium triangle: 3 points on the hypothenuse, 1 point on the small sides.";
  message += Bundle.msg("aboutTangramMsg44"); //_ "\nLarge triangle : 3 points on each side.";
  message += Bundle.msg("aboutTangramMsg45"); //_ "\nSquare : 1 point on each side.";
  message += Bundle.msg("aboutTangramMsg46"); //_ "\nDiamond : 3 points on the large sides, 1 point on the small sides.";
  message += "\n\n";
  message += Bundle.msg("line") + "\n";       //_ "Laboratory of Informatics in Education (LINE)"
  message += Bundle.msg("lineURL");           //_ "http://www.usp.br/line"
  message += "\n";
  message += "\n";
  return message;
 
  }


 //----------------------------------------------------------------------------
 //  Distances
 //----------------------------------------------------------------------------

 public static final int TANGRAM_PANEL_SNAP_DISTANCE     = 100;
 public static final int TANGRAM_MODEL_DISPLAY_MARGIN    = 4; // top-left margin to Model (left side)
 public static final int TANGRAM_PANEL_MOUSE_MARGIN      = 4;


 //TODO: rever isso - comparar posicoes iniciais com alterada por HTML (iLM_PARAM_Assignment)
 // private static final int TRANS_X0 = 20, TRANS_Y0 = 20; // to adjust margin of pieces in working area
 public static final int
   TRANS_X0 =  5,
   TRANS_Y0 =  5,  // to adjust margin of pieces in working area
   TRANS_YN = 35; // if 'TangramPosition.ymin<0' add extra 'TRANS_Y0' to avoid the peak of triangle do not appears

 public static final int TANGRAM_WORKING_DISPLAY_MARGIN  = 0; // top-left margin to working pieces


 //----------------------------------------------------------------------------
 //  Layout Sizes and Insets: used in ./src/ilm/line/itangram2/TangramControls.java
 //----------------------------------------------------------------------------

 public static final int     TANGRAM_CONTROL_PANEL_PREF_WIDTH = 280;
 public static final Insets  TANGRAM_CONTROL_PANEL_INSETS     = new Insets(10,10,10,10);

 public static final int     TANGRAM_MODEL_PANEL_WIDTH  = 260; // TangramModel.java
 public static final int     TANGRAM_MODEL_PANEL_HEIGTH = 260;


 //***************************************************************************
 //  Tangram Pieces and composing unitary triangles
 //***************************************************************************
 // Composing Units are Triangles in which all pieces can be decomposed.                  ^
 //  These triangles are identified by the x and y coordinates of the right angle         |
 //  and the angle of rotation considering vertex (0,0):                            --+---+---+---
 //    (1)   0 for triangle (0,0) (1, 0) ( 0, 1)                                        /2|1\
 //    (2)  90 for triangle (0,0) (0, 1) (-1, 0)                                    --+---+---+-->
 //    (3) 180 for triangle (0,0) (-1,0) ( 0,-1)                                        \3|4/
 //    (4) 270 for triangle (0,0) (0,-1) ( 1, 0)                                    --+---+---+---
 //***************************************************************************

 // LENGTH UNIT FOR THE PIECES
 //   this corresponds to half of the length of the side of one unitary triangle
 //   the length unit must be carefully chosen not to distort the pieces when they are rotated.
 //   When changed, remember to adjust the following parameter: TANGRAM_CONTROL_PANEL_PREF_WIDTH
 //   (It should be about 8 times the value of "unit".
 //   All the codes for the models also need to be updated in TangramModelData.
 //   Recommended value: 24
 //     -->  24 * cos(45) = 16.97  -->  rounded to 17  -->  Error = 0.03/16.97 = 0.18%
 public static final int unit = 24; // attention this change the distance between pieces in Model!

 // LARGE TRIANGLE
 public static final int [] largeTriangle_xPoints  = new int [] {  0*unit, -4*unit,  4*unit  };
 public static final int [] largeTriangle_yPoints  = new int [] { -2*unit,  2*unit,  2*unit  };
 public static final int [] largeTriangle_xSnappingPoints  = new int [] {  0*unit, -1*unit, -2*unit, -3*unit, -4*unit, -2*unit,  0*unit,  2*unit,  4*unit,  3*unit,  2*unit,  1*unit  };
 public static final int [] largeTriangle_ySnappingPoints  = new int [] { -2*unit, -1*unit,  0*unit,  1*unit,  2*unit,  2*unit,  2*unit,  2*unit,  2*unit,  1*unit,  0*unit, -1*unit  };

 public static final int [][] largeTriangle_composingUnits = new int [][] {
  new int [] {  0*unit, 0*unit,   0  },
  new int [] {  0*unit, 0*unit,  90  },
  new int [] {  0*unit, 0*unit, 180  },
  new int [] {  0*unit, 0*unit, 270  },
  new int [] {  2*unit, 2*unit,  90  },
  new int [] {  2*unit, 2*unit, 180  },
  new int [] { -2*unit, 2*unit,  90  },
  new int [] { -2*unit, 2*unit, 180  }
  };

 // MEDIUM TRIANGLE
 public static final int [] mediumTriangle_xPoints  = new int [] { -1*unit,  3*unit, -1*unit  };
 public static final int [] mediumTriangle_yPoints  = new int [] { -1*unit, -1*unit,  3*unit  };
 public static final int [] mediumTriangle_xSnappingPoints  = new int [] { -1*unit, -1*unit, -1*unit,  0*unit,  1*unit,  2*unit,  3*unit,  1*unit  };
 public static final int [] mediumTriangle_ySnappingPoints  = new int [] { -1*unit,  1*unit,  3*unit,  2*unit,  1*unit,  0*unit, -1*unit, -1*unit  };
 public static final int [][] mediumTriangle_composingUnits = new int [][] {
  new int [] {  1*unit, -1*unit,   0  },
  new int [] {  1*unit, -1*unit, 270  },
  new int [] { -1*unit,  1*unit,   0  },
  new int [] { -1*unit,  1*unit,  90  }
  };

 // SMALL TRIANGLE
 public static final int [] smallTriangle_xPoints  = new int [] {  0*unit, -2*unit,  2*unit  };
 public static final int [] smallTriangle_yPoints  = new int [] { -1*unit,  1*unit,  1*unit  };
 public static final int [] smallTriangle_xSnappingPoints =  new int [] {   0*unit, -1*unit, -2*unit,  0*unit,  2*unit,  1*unit,  };
 public static final int [] smallTriangle_ySnappingPoints =  new int [] {  -1*unit,  0*unit,  1*unit,  1*unit,  1*unit,  0*unit,  };
 public static final int [][] smallTriangle_composingUnits = new int [][] {
  new int [] { 0*unit, 1*unit,  90  },
  new int [] { 0*unit, 1*unit, 180  }
  };

 // SQUARE
 public static final int [] square_xPoints  = new int [] {  0*unit, 2*unit, 0*unit, -2*unit  };
 public static final int [] square_yPoints  = new int [] { -2*unit, 0*unit, 2*unit,  0*unit  };
 public static final int [] square_xSnappingPoints  = new int [] {  0*unit, -1*unit, -2*unit, -1*unit,  0*unit,  1*unit,  2*unit,  1*unit  };
 public static final int [] square_ySnappingPoints  = new int [] { -2*unit, -1*unit,  0*unit,  1*unit,  2*unit,  1*unit,  0*unit, -1*unit  };
 public static final int [][] square_composingUnits = new int [][] {
  new int [] { 0*unit, 0*unit,   0  },
  new int [] { 0*unit, 0*unit,  90  },
  new int [] { 0*unit, 0*unit, 180  },
  new int [] { 0*unit, 0*unit, 270  }
  };

 // LOZANGE
 public static final int [] lozenge_xPoints  = new int [] {  1*unit, 1*unit, -1*unit, -1*unit  };
 public static final int [] lozenge_yPoints  = new int [] { -3*unit, 1*unit,  3*unit, -1*unit  };
 public static final int [] lozenge_xSnappingPoints  = new int [] {  0*unit, -1*unit, -1*unit, -1*unit,  0*unit,  1*unit,  1*unit,  1*unit  };
 public static final int [] lozenge_ySnappingPoints  = new int [] { -2*unit, -1*unit,  1*unit,  3*unit,  2*unit,  1*unit, -1*unit, -3*unit  };

 public static final int [][] lozenge_composingUnits = new int [][] {
  new int [] {  1*unit, -1*unit,  180  },
  new int [] {  1*unit, -1*unit,  270  },
  new int [] { -1*unit,  1*unit,    0  },
  new int [] { -1*unit,  1*unit,   90  }
  };

 }
