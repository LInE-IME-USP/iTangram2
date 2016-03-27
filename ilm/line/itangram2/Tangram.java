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

//**************************************************************************
//                            Tangram Class
//**************************************************************************
//  Class for the Tangram Application and Applet
//  Consists of 2 Panels
//  Panel 1 includes the Tangram Panel.
//  Panel 2 displays Tangram Controls and the model to reproduce.
//**************************************************************************

import ilm.line.util.Bundle;
import ilm.line.util.Util;
import ilm.line.util.UtilFiles;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
//import java.util.HashMap;
import java.util.Properties;

public class Tangram extends Applet implements ActionListener {

  //----------------------------------------------------------------------------
  //  static variables
  //----------------------------------------------------------------------------
  public static final boolean ISDEBUG = true; //DEBUG: set true whenever testing iTangram2
  public static final boolean LISTPOSITIONS = false; //DEBUG: print name of each positioning model (defined in 'ilm/line/itangram2/modelset.properties')
  public static final boolean EVALUATEBUTTON = true; // put evaluate button bellow Help button - @see TangramControls.java
  // { "Triangle rectangle", "Z5QNPSyMlK3sE6mrox28Tcs79occ", "eMEkDAiUm4iGNLdUyF001y41AYOV" }
  public static final String  DEFAULTMODEL     = "AHYFqMO00000mIdUqWBk6t40kTJ0"; // default model
  public static final String  DEFAULTNAME      = "Square"; // default short name
  public static final String  DEFAULTPOSITION0 = "eMEkDAiUm4iGNLdUyF001y41AYOV"; // default position of pieces in working area

  public static final String path = "ilm.line.itangram2"; //DEBUG: set true whenever testing iTangram2
  public static final String VERSION = "0.4.5";
  // 0.4.5: 24/03/2016 - fixed update in 'Tangram.itangramProperties' property "model" after to define new mode ("Set code") - mainly to work with iAssign online editor
  // 0.4.4: 22/03/2016 - more adjusts in positioning, size of working area and Model, new messages
  // 0.4.3: 29/10/2013 - more adjusts in positioning (removed first useless read that was broken the effect second)
  // 0.4.2: 29/10/2013 - more adjusts in positioning
  // 0.4.1: 29/10/2013 - adjusts in positioning (was sending just 'position0' - now recover it and send the complete itg2 file: 'Tangram.java' and 'TangramControls.java')
  // 0.3.2: 08/10/2013 - adjusts in positioning (migrated margin calculation from 'TangramModel.computeMargin()' to 'TangramPiece.getMargins(...)'
  // 0.3.1: 04/10/2013 - bug fix: previous or next model now change property 'Tangram.itangramProperties("model")'
  // 0.3.0: 26/09/2013 - new layout (all buttons now use TangramButton with image)
  // 0.2.2: 13/09/2013 - now the original positioning is returned whenever the user change the model (next or previous)
  // 0.2.1: 09/09/2013 - adjust to clean TangramModel (in 0.2.0 when change model => problem in model presentation)
  // 0.2.0: 09/09/2013 - new internal layout; new layout with buttons separated and model and working area with borders
  // 0.1.7: 06/09/2013 - windows changed, now if not authoring => { presents only the position; disabled Previus/Next }
  // 0.1.6: 03/09/2013 - new layout (new color in buttons)
  // 0.1.5: 03/09/2013 - take note of number of "click-get/move"; new layout with images
  // 0.1.4: 02/09/2013 - now "click-get/move"; read file from command line
  // 0.1.3: 29/08/2013 - now the positioning is register and the original model keep (the last do not changed) 
  // 0.1.2: 27/08/2013 - allow load remote file under iLM protocol 1.0 (MA_PARAM_Proposition)
  // 0.1.1: 22/08/2013 - some adjusts
  // 0.1:   21/08/2013 - first from original code from '?'

  //----------------------------------------------------------------------------
  //  iLM variables
  //----------------------------------------------------------------------------
  private String strILMlang = ""; // to getParameter("lang") - "pt_BR", "en_US"
  private String strILMparam_propURL = "";  // to getParameter("iLM_PARAM_Assignment") - it will define the content
  private String strILMparam_authoring = "";  // to "iLM_PARAM_Authoring/iLM_PARAM_ViewTeacher" - allow edit

  private String strContenFile = null;      // content file as String, under address in getParameter("iLM_PARAM_Assignment")

  //private HashMap hashMapContenFile = null; // content file as HashMap, under address in getParameter("iLM_PARAM_Assignment")
  private Properties itangramProperties = null; // content file as HashMap, under address in getParameter("iLM_PARAM_Assignment")

  private String strILMfeedback = ""; // to getParameter("iLM_PARAM_Feedback") - default: absence => iLM_PARAM_Feedback="true"

  //----------------------------------------------------------------------------
  //  Data
  //----------------------------------------------------------------------------
  private Frame            frame;
  private TangramPanel     tangrampanel;
  private TangramControls  tangramcontrols;
  private TangramModel     tangrammodel;
  private boolean          isApplication = false;
  private static boolean   staticIsApplication = false;
  public  static boolean   isApplication () { return staticIsApplication; }
  
  public Frame            getFrame()            { if (isApplication) return frame; else return new Frame("iTangram");  }
  public TangramPanel     getTangramPanel()     { return tangrampanel;  }
  public TangramControls  getTangramControls()  { return tangramcontrols;  }
  public TangramModel     getTangramModel()     { return tangrammodel;  }

  //----------------------------------------------------------------------------
  // Test: 'drag-and-drop' and 'point-and-click'; time of use
  // aditional information: 'drag-and-drop' and 'point-and-click'
  private int countDD=0, countPC=0; // number of operation with 'drag-and-drop' and 'point-and-click'
  public void setCountDD () { countDD++; } // TangramPanel.java
  public void setCountPC () { countPC++; } // TangramPanel.java
  public int getCountDD () { return countDD; } // TangramPanel.java
  public int getCountPC () { return countPC; } // TangramPanel.java
  String strDate0, strDate1;
  public static String getTime () {
    String strTimeNow = "";
    try {
      strTimeNow  = "" + java.text.DateFormat.getDateTimeInstance().format(new java.util.Date(System.currentTimeMillis()));
    } catch (Exception e) {
      System.err.println("Error: in DateFormat.getDateTimeInstance: " + e.toString());
      }
    return strTimeNow;
    }
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  //  iMA methods/variables
  // @see TangramPanel.java: mouseReleased(MouseEvent e): after a movement, test if it correct answer => presents congratulations
  //----------------------------------------------------------------------------
  private boolean showFeedback = true; //TODO: iLM: if 'iLM_PARAM_Feedback=false' in 'loadParameters()' => redefine 'showFeedback=false'
  private boolean allowEdit = false; // iLM: allow teacher to edit exercise <=> true
  public boolean isAuthor () { return allowEdit; }


  //------------------------------------------------------------------
  // iLM essential method: get the iTangram content
  //------------------------------------------------------------------
  public String getAnswer () {
    boolean hasProperties = itangramProperties!=null; //TODO: for now, not null => came from file... (manual edition!)
    String strModel = hasProperties ? (String)itangramProperties.get("model") : DEFAULTPOSITION0;
    String
      strPosition0 = tangrampanel.getTangramPosition().writeToString(), // this can not be empty!
      strTitle = hasProperties ? (String)itangramProperties.get("title") : "put here the exercise title",
      strShortname = hasProperties ? (String)itangramProperties.get("shortname") : "put here a short name",
      strAuthor = hasProperties ? (String)itangramProperties.get("author") : "put here your name";

    strDate1 = " end=" + getTime() + " ]";
    // System.out.println("Tangram.java: getAnswer(): [ " + strDate1 + "");

    if (strTitle==null)
      strTitle = "Exercise iTangram";
    if (strAuthor==null)
      strTitle = "iTangram - LInE";

    String strContent = "";
    // if (allowEdit)
    strContent = "# itangram2: http://www.matematica.br!\n" +
           "title = " + strTitle + "\n" +
           "shortname = " + strShortname + "\n" +
           "author = " + strAuthor + "\n" +
           "date = " + Util.getSystemData() + "\n" + // itangramProperties.get("date")
           "usageTime = " + strDate0 + ";" + strDate1 + "\n" +
           "usageMouse = [ countPC=" + countPC + "; countDD=" + countDD + " ]\n" + 
           "model = " + strModel + "\n" +
           "position0 = " + strPosition0 + "";
    // else
    //  strContent = strPosition0 + "";

    // System.out.println("Tangram.java: getAnswer(): " + strContent);
    if (ISDEBUG) System.out.println("Tangram.java: getAnswer():\n" + strContent);
    return strContent;
    } // public String getAnswer()


  //------------------------------------------------------------------
  // iLM essential method: get the iTangram evaluation
  //------------------------------------------------------------------
  public double getEvaluation () {
    if (comparePositionWithModel(false)) // compare positioning with the "answer model"; do not show the single message "correct/wrong"
      return 1; // correct => 1
    return 0; // incorrect => 0
    }


  public String getPosition () {
    boolean hasProperties = itangramProperties!=null; //TODO: for now, not null => came from file... (manual edition!)
    String strPosition0 = tangrampanel.getTangramPosition().writeToString(); // this can not be empty!
    // String strPosition0 = (String) itangramProperties.get("position0"); // (String) tangram.itangramProperties.get("model"); // a  
System.out.println("Tangram.java: getPosition(): " + strPosition0);
    return strPosition0;
    }


  //----------------------------------------------------------------------------
  //  Constructor
  //----------------------------------------------------------------------------
  public Tangram () {
    super();
    //System.out.println("New Object Created: Tangram");
    String [] args = { "lang=pt_BR" };
    Bundle.setConfig(args);
    }

  public Tangram (String[] args) {
    super();
    //System.out.println("New Object Created: Tangram");
    Bundle.setConfig(args);
    }
  
  //----------------------------------------------------------------------------
  //  Compare With Model
  //----------------------------------------------------------------------------
  public boolean comparePositionWithModel (boolean fromTangramPanel) {
    TangramPosition modlPos = getTangramModel().getTangramPosition(); // in 'TangramModel.java': TangramPosition modelPosition
    TangramPosition currPos = getTangramPanel().getTangramPosition();
    currPos.updateComposingUnits();
    boolean result = (modlPos.equals(currPos));
    if (result && showFeedback) { // iMA: with external button to "evaluate/send" do not show dialog

      //DEBUG try { String str__=""; System.out.print(str__.charAt(3)); } catch (Exception except) { except.printStackTrace(); }

      if (compareDialog == null) {
        compareDialog = new Dialog(getFrame(), TangramProperties.DIALOG_COMPARE_TITLE, true);
        Button hideCompareButton = new Button("OK");

        compareCongratulation.setBackground(TangramProperties.HELP_PANEL_BACKGROUND_COLOR);
        hideCompareButton.setBackground(TangramProperties.HELP_TEXT_BACKGROUND_COLOR);

        hideCompareButton.addActionListener(new DialogHidingActionListener(compareDialog));
        compareDialog.addWindowListener(new DialogHidingWindowListener(compareDialog));
        compareDialog.add("North", compareCongratulation);
        //D compareDialog.add("Center", compareTextField); //D present to the learner the just found matching position?
        compareDialog.add("South",  hideCompareButton);
        }
      String description = TangramProperties.CAPTION_CONTROL_MODEL_LABEL + getTangramControls().getCurrentPositionDescription();
      compareDialog.setTitle(description);
      //D compareTextField.setText(getTangramPanel().getTangramPosition().writeToString());

      if (fromTangramPanel)
        showCompareDialog(); // came from 'TangramPanel.mouseReleased(...)' => present the window with the congratulations

      } // if (result && showFeedback)
    // System.out.println("Tangram.java: comparePositionWithModel(boolean): Compare Result = " + result);
    return result;
    } // public boolean comparePositionWithModel(boolean fromTangramPanel)


  //---  Compare Dialog  -------------------------------
  private Dialog    compareDialog    = null;
  private Label     compareCongratulation = null; // when learner find out the correct model
  private TextField compareTextField = new TextField(); //D present to the learner the just found matching position?

  //------------------------------------------------------------------------
  //  Compare Pop-up Dialog
  //------------------------------------------------------------------------
  private void showCompareDialog () {
    compareDialog.pack();
    centerOnFrame(compareDialog);
    compareDialog.setTitle(Bundle.msg(TangramProperties.DIALOG_COMPARE_TITLE));
    compareDialog.show();
    }

  //------------------------------------------------------------------------
  //  Helper Method:  Center a Dialog on this Frame.
  //------------------------------------------------------------------------
  public void centerOnFrame (Component toCenter) {
    Point location = getFrame().getLocation();
    int xcoord = location.x;
    int ycoord = location.y;
    Dimension size = getFrame().getSize();
    xcoord += size.width/2;
    ycoord += size.height/2;
    size = toCenter.getSize();
    xcoord -= size.width/2;
    ycoord -= size.height/2;
    toCenter.setLocation(new Point(xcoord, ycoord));
    }
  
  //----------------------------------------------------------------------------
  //  Main Method
  //----------------------------------------------------------------------------
  public static void main (String[] args) {

    staticIsApplication = true;
    boolean tryOpen = false;
    if (args==null || args.length==0)
      args = new String [] { "lang=pt_BR" };
    else
      tryOpen = true;

    Tangram tangram = new Tangram(args);
    tangram.isApplication = true;
    // Allow teacher to edit exercise / view ITG2 code
    // also defined in Applet when: strILMparam_authoring=this.getParameter("iLM_PARAM_Authoring")="true"
    tangram.allowEdit = true;
    tangram.buildTangramGUI(); // load

    // Try to load content from file
    if (tryOpen) {
      String str_temp;
      for (int i=0; i<args.length; i++) {
        str_temp = args[i];
        if (UtilFiles.isFile(str_temp)) {
          System.out.println("Try to open file: "+str_temp);
          // It will define 'tangram.setProperties(iProperties);'
          String strContent = UtilFiles.readFileContent(tangram, str_temp); // (java.applet.Applet applet, String strURL)
          if (strContent!=null && strContent!="") {
            // System.out.println(strContent);
            break;
            }
          }
        } // for
      }

    if (tangram.itangramProperties==null) { // do not leave in 'TangramModel.java' (TangramPosition modelPosition) and TangramPosition.java: 'int [][] composingUnits' empty
      String strContentAux = DEFAULTMODEL; // model 3: triangle
      String strName = DEFAULTNAME;
      String strStartPosition = DEFAULTPOSITION0; // initial positon in working area: shuffled
      tangram.itangramProperties = new Properties();
      tangram.itangramProperties.put("model", strContentAux);
      tangram.itangramProperties.put("shortname", strName);
      tangram.itangramProperties.put("position0", strStartPosition);
      strContentAux = (String) tangram.itangramProperties.get("model"); // answer model
      strName = (String) tangram.itangramProperties.get("shortname"); // short name of the exercise model
      strStartPosition = (String) tangram.itangramProperties.get("position0"); // starting position of pieces in working area	 
      }
       
    //tangram.itangramProperties.list(System.out);

    String strPosition0 = null, strModel = null;
    if (tangram.itangramProperties!=null) {
      strPosition0 = (String) tangram.itangramProperties.get("position0");
      strModel = (String) tangram.itangramProperties.get("model");
      if (strPosition0!=null && strPosition0!="") { // update start position to all pieces
        // Panel "Working area": position TangramPieces
        TangramModelData.start_position = strPosition0; // initial positon in working area: shuffled 
        }

      if (strModel!=null && strModel!="") { // update model
        // Panel "Model panel": position TangramPieces
        try {
          tangram.tangrampanel.positionPieces(false); // false => from outside of 'TangramPanel.java' //-------------------------------

	  tangram.tangrammodel.loadModelPosition(strModel);  //-------------------------------
	} catch (Exception except) {
	  System.err.println("Tangram.java: main(): error load model position");
	  except.printStackTrace();
	  }
        }
      }

    // The model must be load anyaway (with ou without the user's model)
    // tangram.tangramcontrols.loadCurrentModel(); // load current model

    tangram.frame.setVisible(true);
    } // public static void main(String[] args)


  private void buildTangramGUI () {

    // Test: 'drag-and-drop' and 'point-and-click'; time of use
    strDate0 = "[ start=" + getTime();
    // System.out.println("Tangram.java: buildTangramGUI(): " + strDate0 + " ]");

    Panel auxPanelGeneral = new Panel(new BorderLayout());

    compareCongratulation = new Label(Bundle.msg(TangramProperties.DIALOG_COMPARE_MESSAGE)); // Congratulations!  You solved this puzzle.

    if (this.isApplication) {
      frame = new Frame();
      frame.setTitle(Bundle.msg(TangramProperties.TANGRAM_FRAME_TITLE));
      }
    else frame = null;

    tangrammodel    = new TangramModel(this);
    tangramcontrols = new TangramControls(this, tangrammodel, frame);
    tangrampanel    = new TangramPanel(this);

    //D System.out.println("\n\nTangram.java: buildTangramGUI(): tangrammodel=" + tangrammodel);

    //                                                              +------------------------------------+
    auxPanelGeneral.add("West",   tangramcontrols); // left panel : | logo help aval | < working area   > |
    auxPanelGeneral.add("Center", tangrampanel);    // right panel: | <model>        | < (TangramPanel) > |
    //                                                              | <Prev> <Next>  | <                > |
    //                                                              | <See> <Set>    | <                > |
    //                                                              +-------------------------------------+

    if (this.isApplication) {
      frame.add(auxPanelGeneral);

      frame.pack();
      frame.setSize(TangramProperties.ITANGRAM_WIDTH, TangramProperties.ITANGRAM_HEIGTH); // 800, 600);

      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          frame.setVisible(false);
          }
        });
      }
    else // is applet
      this.add(auxPanelGeneral);

    } // private void buildTangramGUI()


  //----------------------------------------------------------------------------
  //  Load content file
  //----------------------------------------------------------------------------
  public Properties loadContentFile (String strFileURL) {
    boolean successRead = false;

    System.out.println(this.getClass().getName() + ": loadContentFile(" + strFileURL + "): ");

    //HashMap hashMapContenFile = null;
    // this.strContenFile = UtilFiles.readFileContent(this, strFileURL);
    // System.out.println(this.getClass().getName() + ": loadContentFile(" + strFileURL + "): " + this.strContenFile);

    // content file as HashMap, under address in getParameter("iLM_PARAM_Assignment")
    this.itangramProperties = UtilFiles.getDefaultProperties(this, strFileURL);
    // System.err.println("src/ilm/line/itangram2/Tangram.java: loadContentFile(" + strFileURL + "): position0=" + this.itangramProperties.get("position0")); //D
    // UtilFiles.listHashMap(this.hashMapContenFile);

    return this.itangramProperties;
    } // public Properties loadContentFile (String strFileURL)


  // @see TangramControls.java: loadCurrentModel(): get property to proper set in TangramData
  public String getProperties (String strKey) {
    String strProperty = null;
    if (this.itangramProperties == null) {
      this.itangramProperties = new Properties();
      //DEBUG try { String str__=""; System.out.print(str__.charAt(3)); } catch (Exception except) { except.printStackTrace(); }
      //D System.out.println(this.getClass().getName() + ": getProperties(" + strKey + "): " + strProperty);
      }

    strProperty = (String) this.itangramProperties.get(strKey);
    //D System.out.println(this.getClass().getName() + ": getProperties(" + strKey + "): " + strProperty);
    return strProperty;
    }

  // @see TangramControls.java: loadPrevModel(), loadNextModel(): set property 'model' in TangramData
  public void setProperties (String strKey, String strValue) {
    this.itangramProperties.setProperty(strKey, strValue);
    }

  public void setProperties (Properties properties) {
    //D System.out.println(this.getClass().getName() + ": loadContentFile(HashMap): ");
    // content file as HashMap, under address in getParameter("iLM_PARAM_Assignment")
    this.itangramProperties = properties;
    }


  //----------------------------------------------------------------------------
  //  Load parameters
  //----------------------------------------------------------------------------
  public void loadParameters () {
    boolean successRead = false;
    strILMlang = this.getParameter("lang"); // "pt_BR", "en_US"

    // it will define the content
    strILMparam_propURL = this.getParameter("iLM_PARAM_Assignment"); // iLM protocol 2.0
    if (strILMparam_propURL==null || strILMparam_propURL=="") { // try older protocol
      strILMparam_propURL = this.getParameter("MA_PARAM_Proposition"); // iLM protocol 1.0
      }

    if (strILMparam_propURL!=null && !strILMparam_propURL.equals("false")) {
      // try to read content file
      successRead = (loadContentFile(strILMparam_propURL)!=null); // false => error in read process
      }

    strILMfeedback = this.getParameter("iLM_PARAM_Feedback"); // default: absence => iLM_PARAM_Feedback="true"
    if (strILMfeedback!=null && strILMfeedback.equals("false"))
      showFeedback = false; // default is "show evaluation result"

    // Allow teacher to edit exercise / view ITG2 code
    strILMparam_authoring = this.getParameter("iLM_PARAM_Authoring"); // iLM 2.0
    if (strILMparam_authoring==null || strILMparam_authoring=="")
      strILMparam_authoring = this.getParameter("iLM_PARAM_ViewTeacher"); // iLM 1.0
    if (strILMparam_authoring!=null && strILMparam_authoring.equals("true"))
      allowEdit = true;

    } // public void loadParameters()


  //----------------------------------------------------------------------------
  //  Init Method
  //----------------------------------------------------------------------------
  public void init () {

    //d If you desire a new frame over the user browser
    //d Button appletButton = new Button("Pop Up");
    //d appletButton.addActionListener(this);
    setLayout(new BorderLayout());
    //d add(appletButton, BorderLayout.CENTER);

    loadParameters();

    buildTangramGUI();

    // iLM: get content
    //TODO: passar para pegar o "conteudo de 'strILMparam_propURL = getParameter(iLM_PARAM_Assignment)'" e define modelo em 'TangramModelData.java: String start_position'
    //TODO: habilitar outros modelos com 'TangramModelData.java: String [] positions'
    //TODO: definir de arquivo 'TangramModelData.start_position' e 'TangramModelData.descriptions'
    // System.out.println("Tangram.java: strILMparam_propURL=" + strILMparam_propURL + ", this.strContenFile='" + this.strContenFile + "'");

    if (strILMparam_propURL!="") {
      String strContentAux = DEFAULTMODEL; // model 3: triangle
      String strName = DEFAULTNAME;
      String strStartPosition = DEFAULTPOSITION0; // initial positon in working area: shuffled
      if (this.itangramProperties!=null) {
        strContentAux = (String) this.itangramProperties.get("model"); // answer model
        strName = (String) this.itangramProperties.get("shortname"); // short name of the exercise model
        strStartPosition = (String) this.itangramProperties.get("position0"); // starting position of pieces in working area
        }
      if (ISDEBUG) System.out.println("Tangram.java: shortname=" + strName + ", model=|" + strContentAux + "|, position0=|" + strStartPosition + "|");

      // AHYFqMO00000mIdUqWBk6t40kTJ0 square; aGi70MZcMTScLqccRlbrsJi7EkFN rectangle; Z5QNPSyMlK3sE6mrox28Tcs79occ triangle
      //_model strContentAux = "AHYFqMO00000mIdUqWBk6t40kTJ0";
      //_model strContentAux = "aGi70MZcMTScLqccRlbrsJi7EkFN";
      //_model strContentAux = "Z5QNPSyMlK3sE6mrox28Tcs79occ";
      TangramModelData.start_position = strStartPosition; // initial positon in working area: shuffled //TODO:
      // answer model triangle: Z5QNPSyMlK3sE6mrox28Tcs79occ

      TangramModelData.positions = new String [] { strContentAux } ; // model in small window (left-top): student target
      TangramModelData.descriptions = new String [] { strName } ; // initial positon in working area //TODO:
      tangrampanel.positionPieces(true); // true => from outside of 'TangramPanel.java'
      try {
	tangrammodel.loadModelPosition(strContentAux);
      } catch (Exception except) {
	System.err.println("Tangram.java: init(): error load model position");
	except.printStackTrace();
        }

      // The model must be load anyaway (with ou without the user's model)
      tangramcontrols.loadCurrentModel(); // load current model

      System.out.println("Tangram.java: strILMparam_propURL=" + strILMparam_propURL + ", strContentAux='" + strContentAux + "'");

      }

    } // public void init()


  //----------------------------------------------------------------------------
  //      ACTION LISTENER IMPLEMENTATTION
  //----------------------------------------------------------------------------
  public void actionPerformed (ActionEvent e) {
    if (frame==null)
      return;
    frame.setVisible(true);
    if (isApplication)
      System.exit(0);
    }


  private void updateLanguage () {
    if (frame==null)
      return;
    frame.setTitle(Bundle.msg(TangramProperties.TANGRAM_FRAME_TITLE));
    }


  } // public class Tangram extends Applet implements ActionListener

