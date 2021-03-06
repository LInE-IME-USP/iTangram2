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

class TangramPositionCoder {

 private final static int coordMax = 780;
 private final static int alphaMax = 360;
 
 //----------------------------------------------------------------------------
 //  Encoding and Decoding Methods
 //----------------------------------------------------------------------------
 //  A Position consists of an array 7 positionned pieces.
 //  Each positionned pieces represents the position of a piece in the following order:  
 //         largeTriangle, largeTriangle, mediumTriangle,
 //         smallTriangle, smallTriangle, square, lozenge.
 //  A positionned piece consists of an array of 3 integers:
 //  Each int represents a parameter of the position of the piece in the following order:  
 //          x-offset, y-offset, angle of rotation.
 //----------------------------------------------------------------------------


 //------------------------------------------------------------------------
 //  Position
 //------------------------------------------------------------------------

 //------------------------------------------------------------------
 //  Constructor
 //------------------------------------------------------------------
 public TangramPositionCoder () {
  }

 //--- Encoding -----------------------------------------------------------
 public static String encodePosition (int [][] position) {
  String result = "";
  for (int cnt = 0; cnt < position.length; cnt++) {
   result += encodePiece(position[cnt]);
   }
  return result;
  }

 //--- Decoding -----------------------------------------------------------
 public static int [][] decodePosition (String str) {
  str = str.trim(); // clear String

  // Verify the Argument String
  if (str.length() != 28) {
   if (Tangram.ISDEBUG) System.err.println("TangramPositionCoder.java: ");
   System.err.println("Error: #code=" + str.length() + "!=28! (code='" + str + "')");
   return null;
   }

  for (int count = 0; count < str.length(); count++) {
   // 10 digits: 0=48 -> 9=57
   // 26 upper:  A=65 -> Z=90
   // 26 lower:  a=97 -> z=122
   char currChar = str.charAt(count);
   if (                   currChar < 48)  return null;
   if (currChar > 57  &&  currChar < 65)  return null;
   if (currChar > 90  &&  currChar < 97)  return null;
   if (currChar > 122                  )  return null;
   }
  
  int [][] result = new int [7][];
  for (int cnt = 0; cnt < 7; cnt++) {
   result[cnt] = decodePiece(str.substring(4*cnt, (4*cnt)+4)); // 62 basis
   }
  return result;
  }


 //------------------------------------------------------------------------
 //  Piece
 //------------------------------------------------------------------------
 
 //--- Encoding -----------------------------------------------------------
 private static String encodePiece (int[] piece) {
  // The angle must be multiple of 15 -> 24 possibilities.
  // Encoding must support coordinates up to 400.
  // Formula to encode one piece for coordMax = 400: 
  //   num = xcoord + ycoord * 400 + alpha * (400*400);
  //    ---> minimum number of codes is 24*400*400 = 3,840,000
  if (piece[0] >= coordMax) return null;
  if (piece[1] >= coordMax) return null;
  if (piece[2] >= alphaMax) return null;
  int alpha = piece[2]/15;
  int intresult = (piece[0] + piece[1] * coordMax + alpha * coordMax*coordMax);
  return intToString(intresult);
  }

 //--- Decoding -----------------------------------------------------------
 private static int [] decodePiece (String str) {
  int [] result = new int[3];
  int pieceInt = StringToInt(str);
  result[0] = pieceInt % coordMax;
  pieceInt /= coordMax;
  result[1] = pieceInt % coordMax;
  pieceInt /= coordMax;
  result[2] = 15 * pieceInt;
  return result;
  }
 
 
 //------------------------------------------------------------------------
 //  String
 //------------------------------------------------------------------------

 //--- Encoding -----------------------------------------------------------
 private static String intToString (int num) {
  // 62 different chars
  // 4 chars --> 62^4 = 14,776,336
  // max coord = (62^4 / 24)^.5 = 784
  String result = "";
  int remains = num;
  result += intToChar(remains % 62);
  remains = remains / 62;
  result += intToChar(remains % 62);
  remains = remains / 62;
  result += intToChar(remains % 62);
  remains = remains / 62;
  result += intToChar(remains % 62);
  return result;
  }

 //--- Decoding -----------------------------------------------------------
 private static int StringToInt (String str) {
  int result = 0;
  result += charToInt(str.charAt(0));
  result += charToInt(str.charAt(1)) * 62;
  result += charToInt(str.charAt(2)) * 62*62;
  result += charToInt(str.charAt(3)) * 62*62*62;
  return result;
  }
 
 
 //------------------------------------------------------------------------
 //  Char
 //------------------------------------------------------------------------
 
 //--- Encoding -----------------------------------------------------------
 private static char intToChar (int num) {
  // 10 digits: 0=48 -> 9=57
  // 26 upper:  A=65 -> Z=90
  // 26 lower:  a=97 -> z=122
  // 26+26+10 = 62 (from 0 to 61)
  if (num < 10) return (char)(num + 48);
  else if (num < 36) return (char)(num - 10 + 65);
  else if (num < 62) return (char)(num - 36 + 97);
  return  (char)0;
  }

 //--- Decoding -----------------------------------------------------------
 private static int charToInt (char chr) {
  if (chr >= '0' && chr <= '9') return (chr - 48);
  if (chr >= 'A' && chr <= 'Z') return (chr - 65 + 10);
  if (chr >= 'a' && chr <= 'z') return (chr - 97 + 36);
  return -1;
  }

 }


