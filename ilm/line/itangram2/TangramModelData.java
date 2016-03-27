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

import java.io.*;
import java.util.*;

class TangramModelData {

 //------------------------------------------------------------------
 //  Constants
 //------------------------------------------------------------------
 // file in which the models are defined.
 private static final String RESOURCE_FILE = "modelset";

 //------------------------------------------------------------------
 //  Model Database
 // @see ilm/line/itangram2/TangramPanel.java
 //------------------------------------------------------------------
 public static String start_position  = "RVNk0JrUZDrGAUmUlO90o6E1xgXV"; // model in small window (left-top): student target

 // initial positon in working area
 public static String [] positions    = { "AHYFqMO00000mIdUqWBk6t40kTJ0", "aGi70MZcMTScLqccRlbrsJi7EkFN", "Z5QNPSyMlK3sE6mrox28Tcs79occ"  };
 public static String [] descriptions = { "square", "rectangle", "triangle"  };

 public int positionIndex = 0;

 //------------------------------------------------------------------
 //  Constructor
 //------------------------------------------------------------------
 public TangramModelData () {
  }

 public String getCurrentModel () {
   // String [] positions
  return positions[positionIndex];
  }

 public String getModelDescription () {
  return " " + (positionIndex+1) + ":  " + descriptions[positionIndex];
  }

 public String getPrevModel () {
  positionIndex--;
  if (positionIndex <0) positionIndex = positions.length - 1;
  return positions[positionIndex];
  }

 public String getNextModel () {
  positionIndex++;
  if (positionIndex >= positions.length) positionIndex = 0;
  return positions[positionIndex];
  }

 private final static String PROPERTIES_LOCATION     = "./Models/";
 private final static String KEY_FILE_TYPE           = "fileType";
 private final static String TANGRAM_MODEL_SET_TYPE  = "TangramModelSet";
 private final static String KEY_NUMBER_OF_MODELS    = "NumberOfModels";
 private final static String KEY_MODEL_PREFIX        = "Model";
 private final static String KEY_MODELCODE_POSTFIX   = "";
 private final static String KEY_MODEL_SEPARATOR     = " % ";

 // If HTML and not authoring => must redefine data of 'position' and 'descriptions'
 // @see ilm/line/itangram2/TangramModel.java: setCurrentModel(String strPosition, String strDescription)
 public static void setPosition (String strPosition) {
  positions[0] = strPosition;
  }
 public static void setDescription (String strDescription) {
  descriptions[0] = strDescription;
  }

 //**********************************************************************
 //  Load Models from Properties File
 //**********************************************************************
 public static void loadFromProperties (Tangram tangram) throws IOException, NumberFormatException {
  String debugModelName = "";
  try {
   //_ System.out.println("*** Loading the models ***");

   String pathRESOURCE_FILE = Tangram.path + "/" + RESOURCE_FILE; // path = "ilm.line.itangram2"
   ResourceBundle resources = null;

   if (!tangram.isApplication() && !tangram.isAuthor()) {
    if (Tangram.ISDEBUG)
     System.err.println("\n\nilm/line/itangram2/TangramModelData.java: loadFromProperties(): NAO autoria => NAO carrega as centenas de modelos de 'src/ilm/line/itangram2/modelset.properties'");
    return;
    }

   try {
    //CAUTION: withou this path => error 'java.util.MissingResourceException: Can't find bundle for base name modelset, locale en_US'
    resources = ResourceBundle.getBundle(pathRESOURCE_FILE); // path = "ilm.line.itangram2"
   } catch (Exception except) {
    if (ilm.line.itangram2.Tangram.ISDEBUG)
     System.err.println("ilm/line/itangram2/TangramModelData.java: loadFromProperties(): resources = ResourceBundle.getBundle(" + pathRESOURCE_FILE + ")");
    // System.err.println("[TangramModelData.java] loadFromProperties(): failed in resource file '" + pathRESOURCE_FILE + "'");
    System.err.println("Exception Number Convertion in resource file <" + pathRESOURCE_FILE + ">: " + except);
    except.printStackTrace();
    return;
    }

   // Test for File Type
   String fileType = resources.getString(KEY_FILE_TYPE);
   if (! TANGRAM_MODEL_SET_TYPE.equals(fileType)) {
    System.err.println("Bad file type in resource file: <" + RESOURCE_FILE + "> is not a " + TANGRAM_MODEL_SET_TYPE);
    return;
    }

   // Stuffing the data in new variables
   int numberOfModels = Integer.parseInt(resources.getString(KEY_NUMBER_OF_MODELS));
   System.out.println("numberOfModels = "+numberOfModels);

   String [] newPositions    = new String[numberOfModels];
   String [] newDescriptions = new String[numberOfModels];

   for (int cnt = 1; cnt <= numberOfModels; cnt++) {
    String modelstr = resources.getString(KEY_MODEL_PREFIX + cnt + KEY_MODELCODE_POSTFIX);
    int    sepIndex = modelstr.indexOf(KEY_MODEL_SEPARATOR);
    debugModelName = modelstr.substring(0, sepIndex);
    newPositions[cnt-1]    = debugModelName;
    newDescriptions[cnt-1] = modelstr.substring(sepIndex + KEY_MODEL_SEPARATOR.length());
    if (ilm.line.itangram2.Tangram.LISTPOSITIONS)
     System.out.println("New Position = " + newDescriptions[cnt-1]);
    }

   // Updating the data with the new data
   positions     = newPositions;
   descriptions  = newDescriptions;

   System.out.println("*** All models Loaded ***");

   } catch (NumberFormatException nfe) {
    if (ilm.line.itangram2.Tangram.ISDEBUG)
     System.err.println("ilm/line/itangram2/TangramModelData.java: loadFromProperties(): failed in define position: last model read '" + debugModelName + "'");
    System.err.println("Exception Number Convertion in resource file <" + RESOURCE_FILE + ">: " + nfe);
    throw(nfe);
    }
  }


 }
