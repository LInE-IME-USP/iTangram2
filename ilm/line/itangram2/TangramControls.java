/*
 * iTangram2 - Interactive/Internet Tangram: http://www.matematica.br/itangram
 *
 * Free interactive solutions to teach and learn
 *
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br / http://www.usp.br/line
 *
 * @author iTangram by Leo^nidas/LInE-IME-USP / adapted from Tangram by Serge
 *
 * @description
 *
 * @see
 *
 * @credits
 * This source is free and provided by iMath Project (University of São Paulo - Brazil). In order to contribute, please
 * contact the iMath coordinator Leônidas O. Brandão.
 * The original code came from 'javapage@serger.biz'.
 *
 * O código fonte deste programa é livre e desenvolvido pelo projeto iMática (Universidade de São Paulo). Para contribuir,
 * por favor contate o coordenador do projeto iMatica, professor Leônidas O. Brandão.
 * O código original deste sistema é de 'javapage@serger.biz'.
 *
 */

package ilm.line.itangram2;

//------------------------------------------------------
// Note: Unlike Sun's VM, MS VM does not support
//      int       java.awt.Component.getWidth()
//      int       java.awt.Component.getHeight()
// Use this instead:
//      Dimension java.awt.Component.getSize()
//      int       java.awt.Dimension.width
//      int       java.awt.Dimension.height
//------------------------------------------------------
//------------------------------------------------------
// Note: Unlike Sun's VM, MS VM does not support
//      int       java.awt.Component.getX()
//      int       java.awt.Component.getY()
// Use this instead:
//      Point     java.awt.Component.getLocation()
//      int       java.awt.Point.x
//      int       java.awt.Point.y
//------------------------------------------------------

import ilm.line.util.Bundle;
import ilm.line.util.HttpUtil;
import ilm.line.util.ZipUtil;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.*;


public class TangramControls extends Panel {

  // Buttons positions
  private static final int POS1 = 0, POS2 = 96, POS3 = 132, POS4 = 168;

  //TODO: passar a usar URL via param de applet!
  // Sent content by HTTP
  private static final boolean TRYTOSEND = false; // true -> testar envio para 'strAddressInServer'
  private static final String  // in 'actionPerformed(ActionEvent e): if (source == evaluateButton)'
    strAddressToSendName = "answer",
    strAddressToSendTXT = "answer.txt",
    strAddressToSendZIP = "answer.zip",
    strAddressInServer = "http://localhost/itangram2/php/";

  // Static strings to buttons - defined in ilm.line.itangram2.TangramProperties
  private static final String
    CAPTION_CONTROL_PREV_BUTTON      = TangramProperties.CAPTION_CONTROL_PREV_BUTTON,
    IMG_PREV                         = TangramProperties.IMG_PREV,
    CAPTION_CONTROL_NEXT_BUTTON      = TangramProperties.CAPTION_CONTROL_NEXT_BUTTON,
    IMG_NEXT                         = TangramProperties.IMG_NEXT,
    IMG_GETCODE                      = TangramProperties.IMG_GETCODE,
    IMG_ENTERCODE                    = TangramProperties.IMG_ENTERCODE,

    CAPTION_CONTROL_GETCODE_BUTTON   = TangramProperties.CAPTION_CONTROL_GETCODE_BUTTON,   // Ver
    CAPTION_CONTROL_ENTERCODE_BUTTON = TangramProperties.CAPTION_CONTROL_ENTERCODE_BUTTON, // Definir

    EXPLAIN_CONTROL_GETCODE          = TangramProperties.EXPLAIN_CONTROL_GETCODE,          // Ver o codigo de posicionamento atual
    EXPLAIN_CONTROL_ENTERCODE        = TangramProperties.EXPLAIN_CONTROL_ENTERCODE;        // Usar o posicionamento de trabalho para definir o modelo

  static private String strNewCode = ""; // for 'actionPerformed(ActionEvent)': auxilliary to present (in error) the new code for the Model
   
  //----------------------------------------------------------------------------
  //  Data
  //----------------------------------------------------------------------------
  private Tangram tangram;
  private Tangram staticTangram; // for 'actionPerformed(ActionEvent)'

  //---  Help Dialog  ----------------------------------
  private Dialog   helpDialog;
  private TextArea helpTextArea = new TextArea("", 40, 10, TextArea.SCROLLBARS_VERTICAL_ONLY);

  //---  Get Code Dialog  -------------------------------
  private Dialog   getCodeDialog;
  private TextArea getCodeTextField = null; // get position of pieces in working area - wait to see if it is author => more lines

  //---  Enter Code Dialog  -------------------------------
  private Dialog    enterCodeDialog;
  private TextField enterCodeTextField = new TextField(35); // enter position to model

  //---  Rotation Increment  ---------------------------
  private Checkbox rotationCheck;
  public  boolean  getRotationCheckState () { return rotationCheck.getState();  }

  //---  Model Buttons  --------------------------------
  // private Button evaluateButton; //DEBUG?
  // private Button helpButton;
  private TangramButton evaluateButton; //DEBUG?
  private TangramButton helpButton;
  private TangramImage logoTop;

  /* private Button prevButton;
  private Button nextButton;
  private Button getCodeButton;
  private Button enterCodeButton; */
  private TangramButton prevButton;
  private TangramButton nextButton;
  private TangramButton getCodeButton;
  private TangramButton enterCodeButton;

  private Label labelPrevButton;
  private Label labelNextButton;
  private Label labelGetCodeButton;
  private Label labelEnterCodeButton;

  //---  Model Label  ----------------------------------
  private String  currentPositionDescription;
  public  String  getCurrentPositionDescription () { return currentPositionDescription;  }
  private Label   modelLabel;
  private Label   labelButtons1, labelButtons2;

  //---  Learner answer --------------------------------
  public String getCodePositioning (boolean showDialog) {
    // Automatic evaluation: DIALOG_COMPARE_MESSAGE
    // @see: TangramPanel.java: mouseReleased(MouseEvent): after any movement, compare call 'Tangram.comparePositionWithModel()'
    // @see: Tangram.java: comparePositionWithModel():
    // model 1: 8e6kcHdUSe1VSOTFgLO0WUJ0iAiU
    // String posStr = tangram.getAnswer(); // getTangramPanel().getTangramPosition().writeToString()
    String posStr;
    if (tangram.isAuthor())
      posStr = tangram.getAnswer(); // getTangramPanel().getTangramPosition().writeToString()
    else
      posStr = tangram.getPosition();

    getCodeTextField.setText(posStr); // text area: help

    tangram.centerOnFrame(getCodeDialog);
    getCodeTextField.selectAll();
    getCodeTextField.requestFocus();
    getCodeDialog.setVisible(true);
    System.out.println("TangramControls.java: getCodePositioning(...): positioning - copy this code: " + posStr);
    return posStr;
    }


  private static void transfer () {
    // DO NOT WORK on applets!!
    java.awt.datatransfer.Transferable trans =java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
    Object obj = null;
    try {
      obj = trans.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
      String strContents = String.valueOf(obj);
      System.out.println("TangramControls.java: java.awt.datatransfer.Transferable = " + strContents);
    } catch (Exception except) {
      System.err.println("TangramControls.java: error java.awt.datatransfer: " + except);
      }
    }

//http://www.coderanch.com/t/344638/GUI/java/Granting-AWTPermission
//You need to sign the applet. See here: HowCanAnAppletReadFilesOnTheLocalFileSystem
//permission java.awt.AWTPermission "accessClipboard";
//permission java.awt.AWTPermission "accessEventQueue";
//permission java.awt.AWTPermission "showWindowWithoutWarningBanner";
//
//add these in java.policy file

// --- tinha digitado
// model = Z5QNPSyMlK3sE6mrox28Tcs79occ
// perdeu conteudo... - property pegou outra coisa?
     

// --- gravar
// TangramPanel.java: translatePiece: Square
// Model changed! From: |eMEkDAiUm4iGNLdUyF001y41AYOV|, To: |Z5QNPSyMlK3sEGmrox28Tcs79occ|
// src/ilm/line/itangram2/TangramPosition.java: loadFromString model: (xmin,ymin)=(-96,-107)
// Tangram.java: getAnswer():
// # itangram2: http://www.matematica.br!
// title = put here the exercise title
// shortname = put here a short name
// author = put here your name
// date = Mar 24, 2016 12:58:58 PM
// usageTime = [ start=Mar 24, 2016 12:56:34 PM; end=Mar 24, 2016 12:58:58 PM ]
// usageMouse = [ countPC=7; countDD=0 ]
// model = eMEkDAiUm4iGNLdUyF001y41AYOV
// position0 = UVAkPXgUabyUvKdUtB907I30cTJV

// --- ver:
// ilm/line/itangram2/TangramModelData.java: loadFromProperties(): NAO autoria => NAO carrega as centenas de modelos de 'src/ilm/line/itangram2/modelset.properties'
// TangramControls.java: Dialog enterCodeDialog
// src/ilm/line/itangram2/TangramPosition.java: loadFromString position0: (xmin,ymin)=(-96,-72)
// Tangram.java: shortname=put here a short name, model=|eMEkDAiUm4iGNLdUyF001y41AYOV|, position0=|UVAkPXgUabyUvKdUtB907I30cTJV|



  //------------------------------------------------------------------------
  //---  Dialog with panel and text area                                 ---
  //------------------------------------------------------------------------
  private Dialog buildDialog (String msgTitle, String msgToLabel, TextComponent componenText) {
    Dialog newDialogText = new Dialog(tangram.getFrame(), msgTitle, true);
    Button hideGetCodeButton = new Button("OK");
    hideGetCodeButton.addActionListener(new DialogHidingActionListener(newDialogText));
    newDialogText.addWindowListener(new DialogHidingWindowListener(newDialogText));
    newDialogText.add("South",  hideGetCodeButton);
    newDialogText.add("Center", componenText); // TextField or TextArea
    newDialogText.add("North",  new Label(msgToLabel));

    newDialogText.setBackground(TangramProperties.HELP_PANEL_BACKGROUND_COLOR);
    componenText.setBackground(TangramProperties.HELP_TEXT_BACKGROUND_COLOR);
    hideGetCodeButton.setBackground(TangramProperties.HELP_TEXT_BACKGROUND_COLOR);
    componenText.setEditable(false);

    newDialogText.pack();

    //D System.out.println("TangramControls.java: buildDialog: mostra?");
    return newDialogText;
    }



  //------------------------------------------------------------------------
  // Treat mouse actions
  //------------------------------------------------------------------------
  protected void treatMouseClick (java.awt.event.MouseEvent me) { // From 'TangramButton'
    Object source = me.getSource();
    //D System.out.println("TangramControls.ActionListener: source=" + source);

    //---  Evaluate  -------------------------------------
    if (source == evaluateButton) { //DEBUG?

      System.out.println("\n-------------------------\nTangramControls.java: TangramControls: Tangram.EVALUATEBUTTON\n" + tangram.getAnswer() + "\n-------------------------\n"); //D

      evaluateButton.setSelected(true);

      // Compare 'model' with 'current'
      // + Tangram.java         :
      //   double getEvaluation(): if (comparePositionWithModel(false)) return 1; else return 0;
      //   boolean comparePositionWithModel(boolean fromTangramPanel):
      //      TangramPosition modlPos = getTangramModel().getTangramPosition(), currPos = getTangramPanel().getTangramPosition();
      //      currPos.updateComposingUnits(); boolean result = (modlPos.equals(currPos));
      // + TangramPosition.java :
      //   boolean equals(TangramPosition candidate): compare 'TangramPosition.composingUnits' for all 7 pieces and return true <=> equals
      boolean result = (tangram.getEvaluation()==1 ? true : false);

      String strEvaluationMsg = Bundle.msg("evalResultIs") + ": " + (result ? Bundle.msg("evalCorrect") : Bundle.msg("evalWrong")); // Result is: correct/wrong
      String strAnswerCode = tangram.getAnswer();
      if (ilm.line.itangram2.Tangram.ISDEBUG)
        System.out.println("TangramControls.java: treatMouseClick: value=" + result + ", file=" + strAnswerCode);

      String msgTitle = "iTangram2: " + Bundle.msg("evaluate");
      String msgToLabel = Bundle.msg("Evaluate") + " - " + strEvaluationMsg; // "Evaluate" + ...

      //------------------------------------------------------------------------
      //  Get window with iTangram code
      // System.out.println("TangramControls.java: *** tangram.isAuthor=" + tangram.isAuthor());
      TextArea componenText;
      if (tangram.isAuthor()) // allow edit => teacher can see all code as ITG2 file
        componenText = new TextArea(9,80);
      else
        componenText = new TextArea(3,80); // do not see
      //X componenText.setText(strAnswerCode);
      componenText.setText(strEvaluationMsg);
      componenText.setEditable(true); // to allow 'copy/paste'
      Dialog dialogAnswer = buildDialog(msgTitle, msgToLabel, componenText);

      dialogAnswer.setVisible(true);
      //X System.out.println("TangramControls.java: ActionListener: clicou no botao avaliar!\n" + strAnswerCode);
      System.out.println("TangramControls.java: ActionListener: clicou no botao avaliar!");

      evaluateButton.setSelected(false);

      //----------------------------------------------------------------------------
      // Sent content by HTTP
      if (!tangram.isApplication() && TRYTOSEND) { //
        HttpUtil http = new HttpUtil();
        http.addPostVariable("answer", strAnswerCode);
        try {
          http.makePostRequest(strAddressInServer + "saveAnswer.php");
          System.out.println("TangramControls.java: buttonListener: http.getResponse=" + http.getResponse());
        } catch (Exception ex) {
          ex.printStackTrace();
          }
        // fim do envio como texto

        // Enviar como arquivo texto dentro de um zip
        ZipUtil zip = new ZipUtil();
        zip.addFile(strAddressToSendTXT, strAnswerCode);

        HttpUtil httpUtil = new HttpUtil();
        // podemos enviar um arquivo em memoria (arquivo onde temos apenas byte[])
        httpUtil.addFile(strAddressToSendName, strAddressToSendZIP, zip.getZipAsOutputStream().toByteArray());
        try {
          httpUtil.makePostRequest(strAddressInServer + "saveAnswerZip.php");
          System.out.println("TangramControls.java: buttonListener: httpUtil.getResponse=" + httpUtil.getResponse());
        } catch (Exception ex) {
          ex.printStackTrace();
          }

        } // if (!tangram.isApplication() && TRYTOSEND)
       //----------------------------------------------------------------------------

     } // if (source == evaluateButton)
   //---  Help  -------------------------------------
   else if (source == helpButton) {
     tangram.centerOnFrame(helpDialog);
     helpButton.setSelected(true);
     helpDialog.setVisible(true);
     helpButton.setSelected(false);
     }
   //---  Previous  ---------------------------------
   else if (source == prevButton) {
     loadPrevModel();
     prevButton.myPaint(); // make repaint of 'TangramButton prevButton'
     }
   //---  Next  -------------------------------------
   else if (source == nextButton) {
     loadNextModel();
     nextButton.myPaint(); // make repaint of 'TangramButton nextButton'
     }
   //---  Get Code  ---------------------------------
   else if (source == getCodeButton) {
     getCodePositioning(true); // get code and show it to the learner (dialog window)
     getCodeButton.myPaint(); // make repaint of 'TangramButton getCodeButton'
     }
   //---  Enter Code  -------------------------------
   else if (source == enterCodeButton) {
     // System.out.println("TangramControls.java: treatMouseClick");
     // Open: Dialog enterCodeDialog
     // Update code: here 'tangrammodel.loadModelPosition(strNewCode);', calls 'TangramModel.java: loadModelPosition (String modelString)' change 'Tangram.java: Properties itangramProperties'

//DEBUG
enterCodeTextField.setText("Z5QNPSyMlK3sE6mrox28Tcs79occ"); // right triangle
System.out.println("TangramControls.java: Dialog enterCodeDialog: enterCodeTextField.setText=Z5QNPSyMlK3sE6mrox28Tcs79occ");
//     enterCodeTextField.setText("");

     tangram.centerOnFrame(enterCodeDialog);
     enterCodeTextField.requestFocus();
     enterCodeDialog.setVisible(true);
     enterCodeButton.myPaint(); // make repaint of 'TangramButton enterCodeButton'
     }
//Exception in thread "AWT-EventQueue-2" java.security.AccessControlException: access denied ("java.awt.AWTPermission" "accessClipboard")
//  at java.security.AccessControlContext.checkPermission(AccessControlContext.java:366)
//  at java.security.AccessController.checkPermission(AccessController.java:555)
//  at java.lang.SecurityManager.checkPermission(SecurityManager.java:549)
//  at java.lang.SecurityManager.checkSystemClipboardAccess(SecurityManager.java:1394)
//  at sun.awt.X11.XToolkit.getSystemSelection(XToolkit.java:1204)
//  at javax.swing.text.DefaultCaret.mouseClicked(DefaultCaret.java:442)
//  at java.awt.AWTEventMulticaster.mouseClicked(AWTEventMulticaster.java:270)
//  at java.awt.Component.processMouseEvent(Component.java:6508)
//  at javax.swing.JComponent.processMouseEvent(JComponent.java:3321)
// java awt " Dialog(" "java.security.AccessControlContext.checkPermission" "java.lang.SecurityManager.checkSystemClipboardAccess" applet

   } // protected void treatMouseClick(java.awt.event.MouseEvent me)


  //----------------------------------------------------------------------------
  // Load current model
  //----------------------------------------------------------------------------
  public void loadCurrentModel () {
    try {

      // TangramModel.java: loadCurrentModel(): loadModelPosition(modelData.getCurrentModel()); return modelData.getModelDescription();
      // Must define 'TangramModelData.positions[0]' and 'TangramModelData.descriptions[0]'
      String strModel = tangram.getProperties("model");
      String strDescription = tangram.getProperties("shortname");
      if (strModel==null)   //
        currentPositionDescription = tangram.getTangramModel().loadCurrentModel();
      else {
        tangram.getTangramModel().setCurrentModel(strModel, strDescription);
        currentPositionDescription = tangram.getTangramModel().loadCurrentModel();
        }

      modelLabel.setText(Bundle.msg(TangramProperties.CAPTION_CONTROL_MODEL_LABEL) + currentPositionDescription);
    } catch (Exception ex) {
      if (ilm.line.itangram2.Tangram.ISDEBUG)
        System.err.println("ilm/line/itangram2/TangramControls.java: error in loadCurrentModel(): currentPositionDescription=" + currentPositionDescription);
      System.err.println("Error: in current model load: " + ex);
      }
    }


  // Load previous model
  public void loadPrevModel () {
   try {

     // Load model
     currentPositionDescription = tangram.getTangramModel().loadPrevModel();
     modelLabel.setText(Bundle.msg(TangramProperties.CAPTION_CONTROL_MODEL_LABEL) + currentPositionDescription);

     // Reload positioning
     // @see 'ilm.line.itangram2.Tangram.buildTangramGUI()' load positions
     tangram.getTangramPanel().loadPosition0();

     String strModel = tangram.getTangramModel().getCurrentModel();
     tangram.setProperties("model", strModel);
     //D System.out.println("TangramControls.java: loadPrevModel(): " + strModel);

     } catch (Exception ex) {
       System.err.println("TangramControls.java: Error: Exception: " + ex);
       }
     } // public void loadPrevModel()


  //----------------------------------------------------------------------------
  // Load Model
  //----------------------------------------------------------------------------
  public void loadNextModel () {
    try {

      // Load model
      currentPositionDescription = tangram.getTangramModel().loadNextModel();
      modelLabel.setText(Bundle.msg(TangramProperties.CAPTION_CONTROL_MODEL_LABEL) + currentPositionDescription);

      // Reload positioning
      // @see 'ilm.line.itangram2.Tangram.buildTangramGUI()' load positions
      tangram.getTangramPanel().loadPosition0();

      String strModel = tangram.getTangramModel().getCurrentModel();
      tangram.setProperties("model", strModel);
      //D System.out.println("TangramControls.java: loadNextModel(): " + strModel);

    } catch (Exception ex) {
      System.err.println("Error in control: load model: " + ex);
      }
    }


  //----------------------------------------------------------------------------
  // Insets
  //----------------------------------------------------------------------------
  public Insets getInsets () {
    return TangramProperties.TANGRAM_CONTROL_PANEL_INSETS;
    }

  //----------------------------------------------------------------------------
  //  Size
  //----------------------------------------------------------------------------
  public Dimension getPreferredSize () {
    return new Dimension(TangramProperties.TANGRAM_CONTROL_PANEL_PREF_WIDTH, 10);
    }


  // Build a Label
  private static Label buildLabelSimple (String rootMessage) {
    String strMsg = Bundle.msg(rootMessage);
    Label label = new Label(strMsg);
    label.setFont(TangramProperties.FONT_DEFAULT);
    label.setBackground(TangramProperties.TANGRAM_PANEL_BACKGROUND_BUTTONS);
    return label;
    }

  // Build a Button: { img/button_prev.gif; img/button_next.gif; img/button_getcode.gif; img/button_entercode.gif } all wigh 30x30
  private static TangramButton buildSpecialButton (Tangram tangram, Frame frame, TangramControls tangramControls, String msgButton, String strImgName) {
    TangramButton button = new TangramButton(tangram, frame, tangramControls, Bundle.msg(msgButton), strImgName); // Bundle.msg(strImgName));
    button.setLocation(TangramButton.WIDTH,TangramButton.HEIGHT);
    button.setBackground(TangramProperties.COR_FUNDO_LOGO);
    button.setFont(TangramProperties.FONT_DEFAULT);
    return button;
    }

  // Build a Button
  private static Label buildLabel2Button (String msgButton) {
    Label label = new Label(Bundle.msg(msgButton));
    label.setBackground(TangramProperties.COR_FUNDO_LOGO);
    label.setFont(TangramProperties.FONT_DEFAULT);
    return label;
    }


  // Position Label or TangramButton
  private static void addLocSize (Container container, Component comp, int posX, int posY, int type) {
    container.add(comp);
    comp.setLocation(posX, posY);
    if (type==0) { // Panel buttonPanel1: prevButton, nextButton
      comp.setSize(TangramButton.WIDTH,TangramButton.HEIGHT+2);
      }
    else
    if (type==1) { // Panel buttonPanel1: Label labelPrevButton
      comp.setSize(3*TangramButton.WIDTH,TangramButton.HEIGHT+2);
      }
    else
    if (type==2) { // Panel checkPanel: rotationCheck
      //TODO: achar lagura paineis 'TangramModel' e 'TangramControls' para usar abaixo
      comp.setSize(8*TangramButton.WIDTH+20,TangramButton.HEIGHT-2);
      }
    else
    if (type==3) { // Panel buttonPanelPrevNext
      comp.setSize(260, 80);
      }
    else { // Panel buttonPanelGeneral
      comp.setSize(260, 80);
      }
    }


  private static Button buildButtonSimple (String msgButton) {
    Button button = new Button(msgButton);
    int size = msgButton == null ? 150 : 10 + msgButton.length() * 5;
    button.setBackground(TangramProperties.COR_FUNDO_LOGO);
    button.setFont(TangramProperties.FONT_DEFAULT);
    button.setSize(size, 50);
    // System.out.println("TangramControls.java: buildButtonSimple: " + msgButton + ": size=" + button.getSize());
    return button;
    }


  //----------------------------------------------------------------------------
  // Constructor
  //----------------------------------------------------------------------------
  public TangramControls (final Tangram tangram, TangramModel tangramModel, Frame frame) {
    super();
    this.tangram = tangram;
    staticTangram = tangram; // for 'actionPerformed(ActionEvent)'

    setBackground(TangramProperties.CONTROLS_PANEL_BACKGROUND_COLOR);
    setLayout(new BorderLayout());
    //this.setLayout(new GridLayout(0, 1, 0, 4)); // horizontal margin of 4 to separate <logo, help, eval> from model

    //------------------------------------------------------------------------
    //  Layout

    //---  Labels  ----------------------------------------
    modelLabel      = buildLabelSimple(Bundle.msg(TangramProperties.CAPTION_CONTROL_MODEL_LABEL)); // @see 'public void loadCurrentModel(), loadPrevModel(), loadNextModel()'
    labelButtons1   = buildLabelSimple(Bundle.msg(TangramProperties.CAPTION_CONTROL_BUTTON1)); // Model N: short name
    labelButtons2   = buildLabelSimple(Bundle.msg(TangramProperties.CAPTION_CONTROL_BUTTON2)); // Model to be built

    //---  Top image with logo ----------------------------
    logoTop = new TangramImage(tangram, TangramProperties.IMG_LOGOTOP); // image 'img/img_logo_bg.gif'

    //---  Buttons  ---------------------------------------
    helpButton      = new TangramButton(tangram, frame, this, TangramProperties.CAPTION_CONTROL_HELP_BUTTON, TangramProperties.IMG_HELP); // new Button(Bundle.msg(TangramProperties.CAPTION_CONTROL_HELP_BUTTON));
    if (Tangram.EVALUATEBUTTON) { // put the evaluate button
      evaluateButton  = new TangramButton(tangram, frame, this, TangramProperties.CAPTION_CONTROL_EVALUATE_BUTTON, TangramProperties.IMG_EVALUATE); // new Button(Bundle.msg(TangramProperties.CAPTION_CONTROL_EVALUATE_BUTTON));
      }


    // Version 0: with Button
    //v0 prevButton      = buildButtonSimple(Bundle.msg(CAPTION_CONTROL_PREV_BUTTON));
    //v0 nextButton      = buildButtonSimple(Bundle.msg(CAPTION_CONTROL_NEXT_BUTTON));
    //v0 getCodeButton   = buildButtonSimple(Bundle.msg(CAPTION_CONTROL_GETCODE_BUTTON));
    //v0 enterCodeButton = buildButtonSimple(Bundle.msg(CAPTION_CONTROL_ENTERCODE_BUTTON));
    // Version 1: with TangramButton
    // TangramProperties.CAPTION_CONTROL_HELP_BUTTON, TangramProperties.IMG_HELP
    // @see TangramProperties.java: TANGRAM_FRAME_TITLE, ... EXPLAIN_CONTROL_GETCODE, ... EXPLAIN_CONTROL_ENTERCODE...
    prevButton      = buildSpecialButton(tangram, frame, this, CAPTION_CONTROL_PREV_BUTTON, IMG_PREV); // img/button_prev.gif - 28x30
    nextButton      = buildSpecialButton(tangram, frame, this, CAPTION_CONTROL_NEXT_BUTTON, IMG_NEXT); // img/button_next.gif - 28x30

    getCodeButton   = buildSpecialButton(tangram, frame, this, EXPLAIN_CONTROL_GETCODE, IMG_GETCODE); // img/button_getcode.gif - 28x30
    // CAPTION_CONTROL_GETCODE_BUTTON     = Ver
    // EXPLAIN_CONTROL_GETCODE            = Ver o codigo posicionamento atual

    enterCodeButton = buildSpecialButton(tangram, frame, this, EXPLAIN_CONTROL_ENTERCODE, IMG_ENTERCODE); // img/button_entercode.gif - 28x30
    // CAPTION_CONTROL_ENTERCODE_BUTTON   = Definir
    // EXPLAIN_CONTROL_ENTERCODE          = Usar o posicionamento de trabalho para definir o modelo

    labelPrevButton      = buildLabel2Button(CAPTION_CONTROL_PREV_BUTTON);
    labelNextButton      = buildLabel2Button(CAPTION_CONTROL_NEXT_BUTTON);
    labelGetCodeButton   = buildLabel2Button(CAPTION_CONTROL_GETCODE_BUTTON);
    labelEnterCodeButton = buildLabel2Button(CAPTION_CONTROL_ENTERCODE_BUTTON); // Button label: Define

    if (tangram.isApplication() || tangram.isAuthor()) { // application or teacher-author => allow to change model
      }
    else { // it applet and the user is not a teacher => do not allow to change model
      prevButton.setEnabled(false);
      nextButton.setEnabled(false);
      }

    //---  Rotation Increment  ----------------------------
    rotationCheck    = new Checkbox(Bundle.msg(TangramProperties.CAPTION_CONTROL_ROTATIONS_CHECKBOX));
    rotationCheck.setFont(TangramProperties.FONT_DEFAULT);

    //---  Panel  -----------------------------------------
    Panel generalNorthPanel = new Panel(); // Panel north:  |<logo>  <help> <eval>|
                                           //               | <TangramPanel>      |
    Panel generalSouthPanel = new Panel(); // Panel south:  | <Prev> <Next>       |
                                           //               | <See>  <Set>        |

    Panel northPanel = new Panel();
    Panel northPanelButtons = new Panel(); // Panel wih 'img/img_logo_bg.gif': bg is the dark color of the image (9198a3 = 145,152,163)
    northPanelButtons.setBackground(TangramProperties.COR_FUNDO_LOGO);

    northPanel.setLayout(new GridLayout(0, 1, 0, 4)); // horizontal margin of 4 to separate <logo, help, eval> from model
    northPanelButtons.setLayout(null);
    northPanelButtons.setSize(2 * TangramProperties.LARGURA_BOTAO + 100, TangramProperties.ALTURA_BOTAO + 2);

    logoTop.setLocation(POS1, 0); logoTop.setSize(60, TangramProperties.ALTURA_BOTAO);
    northPanelButtons.add(logoTop);

    northPanelButtons.add(helpButton);
    helpButton.setLocation(POS2, 0); helpButton.setSize(TangramProperties.LARGURA_BOTAO+1, TangramProperties.ALTURA_BOTAO+1);

    if (Tangram.EVALUATEBUTTON) { // button to evaluate
      northPanelButtons.add(evaluateButton);
      evaluateButton.setLocation(POS3, 0);
      evaluateButton.setSize(TangramProperties.LARGURA_BOTAO+1, TangramProperties.ALTURA_BOTAO+1);
      }

    //---  North Panel  -----------------------------------
    northPanel.add(northPanelButtons); // | <logo>     <help> <eval> |
    northPanel.add(modelLabel);        // | Model N: model_name      |

    generalNorthPanel.setLayout(new BorderLayout());
    generalNorthPanel.add(northPanel);

    //---  Button Panel  ----------------------------------
    Panel buttonPanel1 = new Panel(null),  // [ Previous [<<]  [>>] Next ]
          buttonPanel2 = new Panel(null),  // [ See      [>|]  [|<] Set  ]
          checkPanel   = new Panel(null);  // [ <> small rotation        ]

    // 'addLocSize(...)' demands layout null to hard positioning
    addLocSize(buttonPanel1, labelPrevButton, 0,0, 1);    addLocSize(buttonPanel1, prevButton, POS2+2,1, 0);              // img/button_prev.gif
    addLocSize(buttonPanel1, nextButton, POS3+2,1, 0);    addLocSize(buttonPanel1, labelNextButton, POS4+2,1, 1);         // img/button_next.gif

    addLocSize(buttonPanel2, labelGetCodeButton, 0,0, 1); addLocSize(buttonPanel2, getCodeButton, POS2+2,1, 0);           // img/button_getcode.gif
    addLocSize(buttonPanel2, enterCodeButton, POS3+2,1, 0);  addLocSize(buttonPanel2, labelEnterCodeButton, POS4+2,0, 1); // img/button_entercode.gif

    addLocSize(checkPanel, rotationCheck, 0,0, 2);

    //TODO: achar lagura paineis 'TangramModel' e 'TangramControls' para usar abaixo
    buttonPanel1.setSize(8*TangramButton.WIDTH, TangramButton.HEIGHT+2);
    buttonPanel2.setSize(8*TangramButton.WIDTH, TangramButton.HEIGHT+2);
    // size of 'checkPanel' already set in 'addLocSize(checkPanel...)'


    // ---
    Panel buttonPanelGeneral = new Panel();
    Panel buttonPanelPrevNext = new Panel(); // Buttons' panel: Previous x Next
    Panel buttonPanelCode = new Panel();     // Buttons' panel: 'Get code' x 'Enter code'

    buttonPanelGeneral.setLayout(new GridLayout(0, 1, 0, 0));
    buttonPanelGeneral.setSize(260,300);// 8*TangramButton.WIDTH, 2*TangramButton.HEIGHT+4);

    buttonPanelPrevNext.setLayout(new GridLayout(0, 1, 0, 0));
    buttonPanelCode.setLayout(new GridLayout(0, 1, 0, 0));
    buttonPanelPrevNext.setBackground(TangramProperties.COR_FUNDO_LOGO); // background color of buttons panel must be the same as its panel basis
    buttonPanelCode.setBackground(TangramProperties.COR_FUNDO_LOGO);     // in order to highlight buttons borders

    buttonPanelPrevNext.add(labelButtons1); // label: 'Model N: short name'
    buttonPanelPrevNext.add(buttonPanel1);  // buttons about models: challenge to the learner
    buttonPanelCode.add(labelButtons2);     // label: positioning
    buttonPanelCode.add(buttonPanel2);      // buttons about pieces positioning: how to register working space

    buttonPanelGeneral.add(new Label(""));  // space to separate TangramModel from panel with buttons "Prev" x "Next"

                                                 // Model to be built
    buttonPanelGeneral.add(buttonPanelPrevNext); // Previous <= | => Next
    buttonPanelGeneral.add(buttonPanelCode);     // Positioning
    buttonPanelGeneral.add(checkPanel);          // Get codde   | Set code

    generalSouthPanel.setLayout(new BorderLayout());
    generalSouthPanel.add(buttonPanelGeneral);

    //- build panels -----------------------------
    this.add("North",  generalNorthPanel);
    this.add("Center", tangramModel);
    this.add("South",  generalSouthPanel);


    if (getCodeTextField==null) {
      if (tangram.isAuthor()) // allow edit => teacher can see all code as ITG2 file
       getCodeTextField = new TextArea(9, 80);
      else
       getCodeTextField = new TextArea(3, 80); // get position of pieces in working area
      }
    getCodeTextField.setEditable(true); // to allow 'copy/paste'

    //------------------------------------------------------------------------
    //  Get Code Pop-up Dialog
    String msgTitle = Bundle.msg(TangramProperties.DIALOG_GETCODE_TITLE);
    String msgToLabel = Bundle.msg(TangramProperties.DIALOG_GETCODE_MESSAGE);
    TextComponent componenText = getCodeTextField; // text area: help
    getCodeDialog = buildDialog(msgTitle, msgToLabel, componenText);

    //------------------------------------------------------------------------
    //  Enter Code: pop-up Dialog
    enterCodeDialog = new Dialog(tangram.getFrame(), Bundle.msg(TangramProperties.DIALOG_ENTERCODE_TITLE), true);
    // the starting point of this Dialog frame is on 'treatMouseClick(java.awt.event.MouseEvent me)'
 
    Button enterCodeOKButton     = buildButtonSimple("OK");
    Button enterCodeCancelButton = buildButtonSimple(Bundle.msg("cancel"));
    enterCodeCancelButton.addActionListener(new DialogHidingActionListener(enterCodeDialog));

    enterCodeOKButton.addActionListener(new ActionListener() {
     public void actionPerformed (ActionEvent e) {
      try {

        TangramModel tangrammodel = staticTangram.getTangramModel();
        strNewCode = enterCodeTextField.getText(); // is static

        System.out.println("TangramControls.java: Model changed! From: |" + tangrammodel.getCurrentModel() + "|, To: |" + strNewCode + "|");

        // Here is really changed string for model in 'TangramModel.loadModelPosition(String)'
        tangrammodel.loadModelPosition(strNewCode);

        enterCodeDialog.setVisible(false);

      } catch (Exception ex) {
        System.err.println("TangramControls.java: TangramControls(...): error, staticTangram=" + staticTangram + ", tangram.tangrammodel=" + staticTangram.getTangramModel() + ", code=" + strNewCode + ": " + ex);
        // ex.printStackTrace();
        enterCodeDialog.setVisible(false);
        Dialog errorDialog = new Dialog(tangram.getFrame(), Bundle.msg(TangramProperties.DIALOG_ENTERCODE_ERROR_TITLE), true);
        Button hideButton = buildButtonSimple("OK");
        Panel panelOK = new Panel();
        panelOK.setBackground(TangramProperties.COR_FUNDO_LOGO);
        hideButton.addActionListener(new DialogHidingActionListener(errorDialog));
        errorDialog.setLayout(new GridLayout(2,1));
        panelOK.add(hideButton);
        errorDialog.add(buildLabelSimple(TangramProperties.DIALOG_ENTERCODE_ERROR_MESSAGE));
        errorDialog.add(panelOK);

        errorDialog.pack();
        tangram.centerOnFrame(errorDialog);
        errorDialog.setVisible(true);
        }
      }
     });
    enterCodeDialog.addWindowListener(new DialogHidingWindowListener(enterCodeDialog));

    Panel codeButtonPanel = new Panel(new GridLayout());
    codeButtonPanel.add(enterCodeOKButton);
    codeButtonPanel.add(enterCodeCancelButton);
    enterCodeDialog.add("South",  codeButtonPanel);
    enterCodeDialog.add("Center", enterCodeTextField); // text field
    enterCodeDialog.add("North",  buildLabelSimple(TangramProperties.DIALOG_ENTERCODE_MESSAGE));
    enterCodeDialog.pack();


    //------------------------------------------------------------------------
    //  Help Pop-up Dialog
    helpDialog = new Dialog(tangram.getFrame(), Bundle.msg(TangramProperties.DIALOG_HELP_TITLE), true);
    Button hideHelpButton = buildButtonSimple("OK");
    helpDialog.setBackground(TangramProperties.HELP_PANEL_BACKGROUND_COLOR);
    helpTextArea.setBackground(TangramProperties.HELP_TEXT_BACKGROUND_COLOR);
    hideHelpButton.setBackground(TangramProperties.HELP_TEXT_BACKGROUND_COLOR);
    hideHelpButton.addActionListener(new DialogHidingActionListener(helpDialog));
    helpDialog.addWindowListener(new DialogHidingWindowListener(helpDialog));
    helpDialog.add("South",  hideHelpButton);
    helpDialog.add("Center", helpTextArea);
    helpDialog.pack();
    helpDialog.setSize(TangramProperties.HELP_DIALOG_WIDTH, TangramProperties.HELP_DIALOG_HEIGHT);

    helpTextArea.setText(Bundle.msg("tangramVersion") + ":" + Tangram.VERSION + "\n" + TangramProperties.buildHelpMessage()); // text area: help

    helpTextArea.setEditable(false);

    } // TangramControls(final Tangram tangram, TangramModel tangramModel, Frame frame) - end of constructor


  } // public class TangramControls extends Panel





