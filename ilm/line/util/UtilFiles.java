/*
 * iTangram2 - Interactive/Internet Tangram: http://www.matematica.br/itangram
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br
 *
 * @author Le�nidas de Oliveira Brand�o
 * 
 * @version First: 2005/06/05
 *
 * @description Utilities methods - usually in static form
 * 
 * @see 
 *  
 * @credits
 * This source is free and provided by iMath Project (University of S�o Paulo - Brazil). In order to contribute, please
 * contact the iMath coordinator Le�nidas O. Brand�o.
 *
 * O c�digo fonte deste programa � livre e desenvolvido pelo projeto iM�tica (Universidade de S�o Paulo). Para contribuir,
 * por favor contate o coordenador do projeto iMatica, professor Le�nidas O. Brand�o. 
 * 
 */


// FIRSTLINEMARK ilm.line.itangram2.Tangram 

package ilm.line.util;

import ilm.line.itangram2.Tangram;

import java.awt.Frame; // boolean browser(String url, Frame frame)
import java.awt.Image;
import java.awt.Toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import java.net.URLConnection;
import java.io.InputStream;
import java.io.BufferedReader; //
import java.io.InputStreamReader; //
import java.net.URL;

public class UtilFiles {

  public static final String addressITangram2 = "http://www.matematica.br/itangram2", // endere�o do iTangram2
                             versao = "versao.igeom"; // nome do arquivo com n�mero de vers�o iTangram2

  public static final String FIRSTLINEMARK = "# itangram2: http://www.matematica.br!"; // it must be the first line in iTangram2
  public static final String ENDOFLINEMARK = "!"; // each line in ITG2 file must end with "!"

  public static final String VERSION = ilm.line.itangram2.Tangram.VERSION;

  public static String INPUT_ENC = "ISO8859-1"; // "UTF-8"
  public static String OUTPUT_ENC = "ISO8859-1"; // "UTF-8"

  public  static String sep; // win => "\", c.c. "/"
  public  static String NETSCAPE_VM = "netscape";
  public  static String MICROSOFT_VM = "microsoft";
  public  static String SUN_VM = "sun";
  public  static String APPLE_VM = "apple";
  public  static String IBM_VM = "ibm";
  public  static String WINDOWS_PLATAFORMA = "windows";
  public  static String MAC_OS_PLATAFORMA = "mac os";
  public  static String MAC_OS_X_PLATAFORMA = "mac os x";
  public  static String SOLARIS_PLATAFORMA = "solaris";
  public  static String HPUX_PLATAFORMA = "hp-ux";
  public  static String LINUX_PLATAFORMA = "linux";
  public  static String DGUX_PLATAFORMA = "dg-ux";
  public  static String IRIX_PLATAFORMA = "irix";
  public  static String AIX_PLATAFORMA = "aix";
  public  static String OS2_PLATAFORMA = "os/2";
  public  static String NETSCAPE_FILE_ACCESS = "UniversalFileAccess";
  public  static String NETSCAPE_EXEC_ACCESS = "UniversalExecAccess";
  public  static String NETSCAPE_PROPERTY_READ = "UniversalPropertyRead";

  private static String plataforma;
  private static String vmVendedor;
  private static String vmVersao;
  private static String mrjVersao;

  private static boolean determinaSerPossivelCriarArquivos = false;
  private static boolean determinaSerPossivelExecutarArquivos = false;
  private static boolean determinaSerPossivelLerPropriedadesUtilFiles = false;
  private static boolean possivelCriarArquivos = false;
  private static boolean possivelExecutarArquivos = false;
  private static boolean possivelLerPropriedadesUtilFiles = false;
  private static boolean possivelSerSU = false;
  private static boolean pathChecked = false;
  private static boolean checkPathResult = false;


  //TODO These methods must be renamed...
  // existeModoNetscapeSeguranca()      -> isNetscapeSecurityModelAvailable()
  // ehPossivelLerPropriedadesUtilFiles() -> isAbleToReadSystemProperties()
  // ehPossivelCriarArquivos()          -> isAbleToCreateFiles()
  // ehPossivelExecutarArquivos()       -> isAbleToExecuteFiles()


  public static String [] strMsgErroBrowser = null;

  public UtilFiles () {
    }


  // ----------------------------------------
  // Image utilities

  // pega a classe de nome "str_classe"
  public static Class pegaClasse (String str_classe) { //
    try {
      Class classe = Class.forName(str_classe);
      //System.out.println("pegaClasse: "+str_classe+" -> "+classe);
      return classe;
    } catch (ClassNotFoundException classnotfoundexception) {
      System.err.println("UtilFiles.pegaClasse: classe n�o encontrada "+str_classe);
      throw new NoClassDefFoundError(classnotfoundexception.getMessage());
      }
    }

  public static Image pegaImagem (Class trataClasse, String str_imagem) {
    Image image = null;
    boolean erro = false;
    String strComponentImage = trataClasse.getClass().getName(); // truque para usar classe para pegar diret�rio

    try {
    if (!Tangram.isApplication()) {
       try {
         InputStream inputstream = (trataClasse != null ? trataClasse : (trataClasse = pegaClasse(strComponentImage))).getResourceAsStream(str_imagem);
         byte[] is = new byte[inputstream.available()];
         inputstream.read(is);
         image = Toolkit.getDefaultToolkit().createImage(is);
         inputstream.close();
      } catch (Exception e) {
         // IOException ioexception
         System.err.println("Erro: n�o foi poss�vel ler a imagem " + str_imagem + ": "+e);
         erro = true;
         //ioexception.printStackTrace();
         }
      } // if (!Tangram.isApplication())
    //
    if ((!Tangram.isApplication() && erro) || Tangram.isApplication()) { // java.net.URL getResource(java.lang.String)
    try {
      image = (Toolkit.getDefaultToolkit().getImage(
               (trataClasse != null ? trataClasse : (trataClasse = pegaClasse(strComponentImage))).getResource(str_imagem)));
      if (trataClasse.getResource(str_imagem)==null) {
         System.err.println("UtilFiles.java: Error while processing the image '"+str_imagem+"': "+trataClasse.getResource(str_imagem));
         }
    } catch (java.lang.NullPointerException npe) {
      System.out.println("UtilFiles.pegaImagem: erro para ler imagem "+str_imagem+" - "+image); //npe);
      npe.printStackTrace();
      return image;
      }
    }

    return image;
    } catch (java.lang.NullPointerException npe) {
      // npe.printStackTrace();
      return image;
      }

    } // Image pegaImagem(String str_imagem)


  public static Image getImage (Class trataClasse, String str_imagem) {
    Image image;
    try  {
      image = pegaImagem(trataClasse, str_imagem); // monta o hash com nomes das imagens
      return image;
    } catch (java.lang.NullPointerException e){//Exception e) { // java.lang.NullPointerException
      System.err.println("UtilFiles.java: Error: while trying to get image "+ str_imagem + ": "+e);
      return null;
      }
    }

  // ----------------------------------------
  // HashMap utilities

  // List HashMap
  public static void listHashMap (HashMap hashMap) {
    int count = 0;
    if (hashMap==null) {
       System.out.println("Error: Trying to read Tangram properties from empty file!");
       }
    System.out.println("listHashMap(): size is " + hashMap.size());
    Iterator listIterator = hashMap.values().iterator();
    while (listIterator.hasNext()) {
       String strItem = (String) listIterator.next();
       System.out.println(" " + count + ". " + strItem);
       count++;
       }
    System.out.println();
    }

  public static Properties getPropertiesFromString (String strFileContent) {
    String [] vectorLines;
    String [] vectorStrProperty; // = new String[2];
    HashMap hashMapProperties = new HashMap();                                                                                               
    Properties iProperties = new Properties();

//System.out.println("\nUtilFiles.java: getPropertiesFromString: " + strFileContent);
    StringTokenizer listOfTokens = new StringTokenizer(strFileContent, "" + '\n' + '\0');
    String strTokenLine = "", strKey, strValue;
    while (listOfTokens.hasMoreTokens()) {
      strTokenLine = listOfTokens.nextToken();
      vectorStrProperty = readProperty(strTokenLine);

      if (vectorStrProperty==null)
        continue; //Recover: if the line has '\n' ignore the rest and continue...

      strKey = vectorStrProperty[0];
      strValue = vectorStrProperty[1];
      hashMapProperties.put(strKey, strValue);
      iProperties.put(strKey, strValue);

//System.out.println(" - " + strKey + " - " + strValue);
       }

   // Try to get Properties if it is set. Otherwise, try to read the string as the only property 'position0'

   //ATTENTION: in iLM the file can be get only once, so it is necessary to read the file as String and we process it!
   // defaultProperties.load(inStream); - do not work in 2 actions of read
   if (iProperties.getProperty("position0")==null) { // try format: aGi70MZcMTScLqccRlbrsJi7EkFN (only with position0)
     hashMapProperties.put("position0", strFileContent.trim());
     iProperties.put("position0", strFileContent.trim());
     }

//System.out.println("-----------");

    return iProperties;
    }

  // @see: ilm/line/itangram2.java: loadContentFile(String strFileURL)
  public static Properties getDefaultProperties (Object classT, String strFileURL) { // throws InvalidParameterException, FileNotFoundException, IOException {
    Properties defaultProperties = null;
    java.io.InputStream inStream = null;
    java.net.URL endURL = null;
//System.err.println("\nsrc/ilm/line/util/UtilFiles.java: getDefaultProperties(...): " + strFileURL); //D
    try {
      defaultProperties = new Properties();
      try {
         endURL = new java.net.URL(strFileURL);
System.err.println("src/ilm/line/util/UtilFiles.java: getDefaultProperties(...): " + endURL); //D
      } catch (Exception except) {
         System.err.println("src/ilm/line/util/UtilFiles.java: getDefaultProperties(" + strFileURL + "): failed! Trying again");
         strFileURL = ((java.applet.Applet)classT).getCodeBase().toString() + strFileURL.trim();
         endURL = new java.net.URL(strFileURL);
         }

      // Try to get Properties if it is set. Otherwise, try to read the string as the only property 'position0'

      //ATTENTION: in iLM the file can be get only once, so it is necessary to read the file as String and we process it!
      //R inStream = endURL.openStream(); - NAO pode, pois a seguranca do iTarefa implicaria 2 acessso, segundo eh bloqueado!
      //R if (inStream==null) { System.err.println("src/ilm/line/util/UtilFiles.java: getDefaultProperties(" + strFileURL + "): ERROR! inStream = null");
      //R  return null; }
      //R defaultProperties.load(inStream); - do not work in 2 actions of read

      String strFileContent = readFromURL(endURL);
//D System.out.println("\nUtilFiles.java: getDefaultProperties: strFileContent=" + strFileContent);
      defaultProperties = UtilFiles.getPropertiesFromString(strFileContent);

      // Recover ITG2: until version 0.4.0 applet send only the 'position0'

      //D String position0 = defaultProperties.getProperty("position0");
      //D System.err.println("src/ilm/line/util/UtilFiles.java: getDefaultProperties(" + strFileURL + "): position0=" + position0); //D
      //DEBUG try { String str__=""; System.out.print(str__.charAt(3)); } catch (Exception except) { except.printStackTrace(); }

    } catch (Exception except) {
      except.printStackTrace();
      }
    //DEBUG: System.out.println("\nsrc/ilm/line/util/UtilFiles.java: getDefaultProperties(" + strFileURL + "): list");
    //DEBUG: defaultProperties.list(System.out);
    return defaultProperties;
    } // public static Properties getDefaultProperties(Object classT, String strFileURL)
   
                                               
  //resourceBundle = ResourceBundle.getBundle("igraf.basico.resource/StringsTable", l);
  // private static Properties getPropertiesFromString (String strContent)
  public static void getPropertiesFromString (HashMap hashMap) {
    int count = 0;
    if (hashMap==null) {
       System.out.println("Error: Trying to read Tangram positioning from empty file!");
       }
    String strTitle, strAuthor, strDate, strModel, strPosition0;
    strTitle = (String) hashMap.get("title");
    strAuthor = (String) hashMap.get("author");
    strDate = (String) hashMap.get("date");
    strModel = (String) hashMap.get("model");
    strPosition0 = (String) hashMap.get("position0");

    // System.out.println("src/ilm/line/util/UtilFiles.java: getPropertiesFromString(...): title=" + strTitle + ", author=" + strAuthor + ", date=" + strDate + ", model=" + strModel + ", position0=" + strPosition0 + "");
    // listHashMap(hashMap);
    }



  // ----------------------------------------
  // Gravar arquivo

  // ilm/line/itangram2/Tangram.java:
  public static void gravar (String nome, String arquivo_str, String msg_org) {
    try { // para n�o dar problema de acento ISO -> UTF...
        // java -Dfile.encoding=iso-8859-1 ilm.line.itangram2.Tangram2 model.itg2
        FileOutputStream fos = new FileOutputStream(nome);
        Writer out = new OutputStreamWriter(fos, OUTPUT_ENC); //
        out.write(arquivo_str);
        out.close();
     } catch (Exception e) {
        System.err.println("Erro: ao tentar escrever arquivo "+nome+" ("+msg_org+"): "+e); // e.printStackTrace();
        }
     }
  // ----------------------------------------

  public static String [] readProperty (String strLine) {
    if (strLine==null || strLine=="")
       return null;
    int pos = strLine.indexOf("=");
    if (pos<0)
       return null;
    String strKey = strLine.substring(0, pos-1).trim();
    String strValue = strLine.substring(pos+1, strLine.length()).trim();
    return new String [] { strKey, strValue };
    }


  // ----------------------------------------
  // Read from file system
  public static String readFromFileSystem (ilm.line.itangram2.Tangram iTangram, String strFileName) {
    String [] property; // = new String[2];
    String str_arq = "", strKey, strValue, linha;
    int cont_linhas = 0;
    Properties iProperties = null;
    try {
      BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(strFileName), INPUT_ENC));
      HashMap hashMapProperties = new HashMap();
      iProperties = new Properties();
      while (( (linha = buffer.readLine())!=null)) { //
        if (cont_linhas==0) { // first line
           if (!linha.equals(FIRSTLINEMARK)) {
              System.err.println("Error: in file read: sorry it is not a iTangram file (or its content was changed)");
              return null;
              }
           }
        else {
           property = readProperty(linha);
           if (property==null)
            continue; //Recover: if the line has '\n' ignore the rest and continue...

           strKey = property[0];
           strValue = property[1];
           hashMapProperties.put(strKey, strValue);
           iProperties.put(strKey, strValue);
           str_arq += linha + "\n";
           }
        if (ilm.line.itangram2.Tangram.ISDEBUG) System.out.println(" - "+linha); //
        cont_linhas++;
        }
      buffer.close();
    } catch (Exception e) {
      System.err.println("Erro: na leitura do arquivo " + strFileName + ": " + e.toString());
      if (ilm.line.itangram2.Tangram.ISDEBUG) e.printStackTrace(); //DEBUG
      return null;
      }
    iTangram.setProperties(iProperties);
    return str_arq;
    } // public static String readFromFileSystem(ilm.line.itangram2.Tan   try { tangrammodel.loadModelPosition(strContentAux); } catch (Exception except) { except.printStackTrace(); }  gram iTangram, String strFileName)


  // ----------------------------------------

  // ----------------------------------------
  // ler arquivo de URL
  //DEBUG: iLM: public static String staticDebugURL = ""
  public static String readFromURL (java.net.URL endURL) {
   if (endURL==null) {
      //D System.out.println("src/ilm/line/util/UtilFiles.java: readFromURL(java.net.URL): endURL=" + endURL + " (ERROR)");
      return "";
      }
   String strURL = endURL.toString();
   java.io.InputStream inputStream = null;
   java.lang.StringBuffer stringbuffer = null;

   try { // read InputStreamReader
     //D System.out.println("src/ilm/line/util/UtilFiles.java: readFromURL(Applet,String): 4: inputStream: " + strURL);
     inputStream = endURL.openStream();
     //DEBUG: iLM: System.out.println("UtilFiles.java: readFromURL: endURL="+endURL+", inputStream="+inputStream);
     java.io.InputStreamReader inputstreamreader = new java.io.InputStreamReader(inputStream, INPUT_ENC);
     //DEBUG: iLM: System.out.println("UtilFiles.java: readFromURL: inputstreamreader="+inputstreamreader);
     java.io.StringWriter stringwriter = new java.io.StringWriter();
     int i = 8192;
     char [] cs = new char[i];
     int i_11_, count = 0;
     try {
       for (;;) {
           count++; //Debug
           i_11_ = inputstreamreader.read(cs, 0, i);
           if (i_11_ == -1)
              break;
           stringwriter.write(cs, 0, i_11_);
           }
       stringwriter.close();
       inputStream.close();
     } catch (java.io.IOException ioexception) {
       System.err.println("Erro: leitura URL 2: "+strURL+": " + ioexception);
       //DEBUG: iLM: staticDebugURL += "\n3: ERROR " + ioexception;
       }
     String strResponse = stringwriter.toString();
     //DEBUG: iLM: staticDebugURL += "\n4: count="+count+":\nSize: " + strResponse.length();
     int sizeOf = (strResponse==null ? 0 :  strResponse.length());
     System.out.println("System utilities: read from URL: bytes read = " + sizeOf + " (" + count + ")");
     //T if (strResponse==null || strResponse=="" || strResponse.length()==0) {
     //T    applet.getAppletContext().showDocument(endURL);
     //T    System.out.println("UtilFiles.java: readFromURL: showDocument("+endURL+"): "+strResponse);
     //T    }
     //T else System.out.println("UtilFiles.java: readFromURL: #+strResponse="+strResponse.length());

     return strResponse;

   } catch (java.io.IOException ioe) {
     System.out.println("Erro: leitura URL: "+strURL+": "+ioe.toString());
     //DEBUG: iLM: ioe.printStackTrace();
     //DEBUG: iLM: staticDebugURL += "\n4: ERROR " + ioe;
     }
   return "";
  } // public static String readFromURL(java.net.URL endURL)


  public static String readFileContent (ilm.line.itangram2.Tangram applet, String strURL) {
   // Permite receber URL
   java.net.URL endURL = null;
   //D System.out.println("\nsrc/ilm/line/util/UtilFiles.java: readFileContent(Applet,String)");

   if (ilm.line.itangram2.Tangram.isApplication()) try {
      String strContent = readFromFileSystem(applet, strURL);
      //D System.out.println("src/ilm/line/util/UtilFiles.java: readFileContent(Applet,String): 3: " + strContent);
      return strContent;
   } catch (Exception e) {
     System.err.println("src/ilm/line/util/UtilFiles.java: Error: in " + strURL);
     e.printStackTrace();
     return "";
     }

   try { //

      if (strURL==null)
         return "";

      int sizeOf = strURL.length();
      if (sizeOf>FIRSTLINEMARK.length())
         sizeOf = FIRSTLINEMARK.length();
      if (ilm.line.itangram2.Tangram.ISDEBUG) System.out.println("src/ilm/line/util/UtilFiles.java: readFileContent(Applet,String): 1: " + strURL);

      if (strURL.substring(0,sizeOf).equals(FIRSTLINEMARK)) { // first iTangram2 protocol: text inside content file
         System.out.println("This is a tag with file content! (" + strURL.substring(0,sizeOf) + "...)");
         return strURL;
         }

     endURL = new java.net.URL(strURL); // se for URL
     if (ilm.line.itangram2.Tangram.ISDEBUG) System.out.println("src/ilm/line/util/UtilFiles.java: readFileContent(Applet,String): 2: " + endURL);

     //DEBUG: iLM: staticDebugURL = "\n1: " + endURL;
   } catch (java.net.MalformedURLException e) {
     try { // se falhou, tente completar com endere�o base do applet e completar com nome de arquivo
       strURL = applet.getCodeBase().toString() + strURL.trim(); //
       System.out.println("Is it a valid URL? ("+strURL+")");
       endURL = new java.net.URL(strURL); // se for URL
       if (ilm.line.itangram2.Tangram.ISDEBUG) System.out.println("src/ilm/line/util/UtilFiles.java: readFileContent(Applet,String): 3: " + endURL);

       //DEBUG: iLM: staticDebugURL += "2: " + endURL;
     } catch (java.net.MalformedURLException ue) {
       System.err.println("Error: in utilities: reading URL 1: applet="+applet+", "+strURL+": "+ue);
       //DEBUG: iLM: staticDebugURL += "\n2: ERROR " + ue;
       return "";
       }
     }

   return readFromURL(endURL);
   }


  // ler arquivo
  public static String [] ler_linhas (String nome, String msg_org) {
    return ler_geral(nome,msg_org,true);
    }

  public static String ler (String nome, String msg_org) {
    String [] str = ler_geral(nome,msg_org,false);
    return str[0];
    }

  public static String []  ler_geral (String nome, String msg_org, boolean devolve_linhas) {
    String str_arq = "", linha;
    Vector vet_linhas = null;
    String [] str_linhas = null;
    int cont_linhas = 0;
    if (devolve_linhas) vet_linhas = new Vector();
    try {
      BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(nome), INPUT_ENC));
      while (( (linha = buffer.readLine())!=null)) { //
        cont_linhas++;
        if (str_arq!="") str_arq += "\n";
        if (devolve_linhas)
           vet_linhas.addElement(linha);
        else
          str_arq += linha;
        // System.out.println(" - "+linha); //
        }
      buffer.close();
      }
    catch (Exception e) {
      System.err.println("Erro: na leitura do arquivo " + nome + " " + msg_org + ": " + e.toString());
      return null;
      }
    if (devolve_linhas) try {
       str_linhas = new String[cont_linhas];
       for (int i=0; i<cont_linhas; i++)
           str_linhas[i] = (String)vet_linhas.elementAt(i);
        return str_linhas;
       } catch (Exception e) { e.printStackTrace(); return str_linhas; }
    str_linhas = new String[1];
    str_linhas[0] = str_arq;
    return str_linhas;
    }
  // ----------------------------------------


  // Dados 3 String devolve vetor de String
  public static String [] montaVetStrings (String msg1, String msg2, String msg3) {
    String [] vetStr;
    Vector vec = new Vector();
    int cont = 0;
    if (msg1!=null && msg1!="") vec.add(msg1); //cont++;
    if (msg2!=null && msg2!="") vec.add(msg2); //cont++;
    if (msg3!=null && msg3!="") vec.add(msg3); //cont++;
    cont = vec.size();
    if (cont==0)
       return null;
    vetStr = new String[cont];
    for (int i=0; i<cont; i++) {
       vetStr[i] = (String)vec.elementAt(i);
       // System.out.println(" - vet["+i+"] = "+vetStr[i]);
       }
    return vetStr;
    }
   

  public static String sep () {
    return "/";
    }


  // Elimina multiplos "!" para deixar toda linha terminando com "}!" ou com "}!\n"
  public static String filteredContentGeoHTML (String contentGEOorHTML) {
    // System.out.println("UtilFiles.filteredContentGeoHTML: ");
    if (contentGEOorHTML==null || contentGEOorHTML.length()==0)
       return "";
    int ind0 = 0, ind = -1;
    String contentFiltered = null, lineContent = "", contentFilteredAux;
    int length = 0, i = 0;

    // Clear the line: left just one "!" as "final mark"
    String clearedContentGEOorHTML = "", linha;
    boolean hasChanged = false;
    int lengthContent = contentGEOorHTML.length();
    try {
    ind = contentGEOorHTML.indexOf(ENDOFLINEMARK+ENDOFLINEMARK); // there are more than one "!"...
    int i_aux = -1;
    while (ind>0) {
       // String.substring(int beginIndex, int endIndex)
       linha = contentGEOorHTML.substring(ind0, ind+1); // first pair of final marks "!!" - take note the first one
       int countFinalMark = 0; //
       int lengthLine = linha.length(), position = lengthLine-1;
       i_aux = 1;
       while (position<lengthLine && (linha.charAt(position)=='!' || linha.charAt(position)=='\n' || linha.charAt(position)=='\0')) {
          // count the number of '!' or '\n'
          i_aux++;
          position--;
          countFinalMark++;
          }
       if (countFinalMark>1) try {
          linha = contentGEOorHTML.substring(ind0, lengthLine-countFinalMark); // + ENDOFLINEMARK; // used '!' as ENDOFLINEMARK
          } catch (Exception e) { System.err.println("    ind0="+ind0+", lengthLine-countFinalMark="+(lengthLine-countFinalMark)); }
          //_ System.out.println(" :-: "+linha+" - "+ lengthLine + " ("+countFinalMark+")");
       clearedContentGEOorHTML += linha + "\n";
       ind0 = ind + countFinalMark + 2; // "+2" means 'jump the '\0' + \n'
       ind = contentGEOorHTML.indexOf(ENDOFLINEMARK+ENDOFLINEMARK, ind0+1); // get the next
       hasChanged = true;
       } // while (ind>0)
    } catch (Exception e) { System.err.println("    ind0="+ind0+", ind="+ind); e.printStackTrace(); }

    if (hasChanged)
       contentGEOorHTML = clearedContentGEOorHTML;
    contentGEOorHTML = contentGEOorHTML.trim();

    ind0 = 0;
    ind = contentGEOorHTML.indexOf("}"+ENDOFLINEMARK) + 1; // pega at� o '}'
    if (ind<1) {
       ind = contentGEOorHTML.indexOf("}\n") + 1; // try without final mark
       if (ind<1) {
          // the content could be an exercise => hexa code, with no line with '{...}'
          return contentGEOorHTML; // return everything
          }
       }

    contentFiltered = null;
    while (ind>0) try {
       // begining index, count char
       lineContent = contentGEOorHTML.substring(ind0, ind+1).trim(); // erase the final mark '!' = ENDOFLINEMARK;
       ind0 = ind + 2; // get the next character in initial content
       ind = contentGEOorHTML.indexOf("}"+ENDOFLINEMARK, ind0);
       if (ind<1) { // try without final mark
          ind = contentGEOorHTML.indexOf("}"+"\n",ind0);
          }
       if (contentFiltered==null)
          contentFiltered = lineContent + "\n";
       else
          contentFiltered += lineContent + ENDOFLINEMARK + "\n";
     } // while (ind>0) try
       catch (Exception e) {
       e.printStackTrace(); // System.out.println("UtilFiles.filteredContentGeoHTML: ind="+ind+"\n-------------------");
       System.err.println("UtilFiles.filteredContentGeoHTML: #lineContent="+length+", ind0="+ind0+", ind="+ind+", i="+i+": "+e.toString());
       System.err.println("Content: ---\n"+contentFiltered+"---");
       return contentGEOorHTML;
       }
    return contentFiltered;
    } // static String filteredContentGeoHTML(String contentGEOorHTML)



  // Devolve o nome do arquivo separado de sua extensao
  // Se digitou "exerc6-2b.teste.geo" devolve "exerc6-2b.teste" e "geo"
  public static String [] decompoeNomeArquivo (String nome) {
    String vetNomes []  = new String[2], 
           sep, token = "", nome1 = "";
    boolean lista = false;
    StringTokenizer st = new StringTokenizer(nome, ".");
    int numTokens = st.countTokens();
    if (numTokens==0) {
       vetNomes[0] = nome;
       vetNomes[1] = "";
       return vetNomes; // nao tem extensao
       }

    nome1 = st.nextToken();
    for (int i=1; i<numTokens-1; i++) {
       // st.hasMoreTokens())
       token = st.nextToken();
       nome1 += "." +token;
       }
    vetNomes[0] = nome1;
    vetNomes[1] = numTokens>1 ? st.nextToken() : "";

    // System.out.println("[UtilFiles.decompoeNomeArquivo(String)] vetNomes[0]="+vetNomes[0]+" vetNomes[1]="+vetNomes[1]);
    return vetNomes;
    }


  // Devolve apenas o �ltimo nome do arquivo.
  // Se digitou "java  ilm.line.itangram2.Tangram2 ../temp/exerc6-2b.itg2", limpaNomeArquivo("../temp/exerc6-2b.itg2") devolve "exerc6-2b.itg2"
  public static String limpaNomeArquivo (String nome) {
    String // vetNomes []  = new String[2], 
           sep, token = "";
    boolean lista = false;
    sep = "/";
    if (lista) System.out.println("UtilFiles.limpaNomeArquivo(String nome) nome="+nome+", sep=<"+sep+">");
    StringTokenizer st = new StringTokenizer(nome, sep);
    while (st.hasMoreTokens()) {
       if (lista) System.out.println(" "+token);
       token = st.nextToken();
       }
    //    int tam = nome.length() - token.length();
    //    vetNomes[0] = token;
    //    vetNomes[1] = nome.substring(0, tam);
    //System.out.println("[UtilFiles.limpaNomeArquivo(String)] vetNomes[0]="+vetNomes[0]+" vetNomes[1]="+vetNomes[1]);
    //    return vetNomes;
    return token;
    }


  // Verifica se "nome" descreve um arquivo v�lido, "leg�vel"
  public static boolean isFile (String nome) {
    File arq;
    try {
      arq = new File(nome);
      return arq.isFile();
    } catch (Exception e) {
      return false;
      }
    }


  // Return File if it exists
  public static File getFile (String strFileName) {
    File file;
    try {
      file = new File(strFileName);
      return file;
    } catch (Exception e) {
      System.err.println("Error: couldn't find the file '" + strFileName + "': " + e.toString());
      return null;
      }
    }


  // Devolve apenas o "path" do arquivo
  // Se "/home/leo/projetos/novo/./../temp/exerc6-2b.geo/exerc6-2b.itg2"
  // devolve "/home/leo/projetos/novo/"
  // Chamado em: 
  public static String [] limpaNomeDiretorioArquivo (String nomeC) { 
    boolean lista = false; //true; // 
    String  vetNomes []  = new String[2], 
            sep, token = "", tokenAnt = "", dir = "", nomeArq;
    sep = "/";
    UtilFiles.sep = sep;
    if (lista) System.out.println("UtilFiles.limpaNomeDiretorioArquivo(String nome) nomeC="+nomeC+", sep=<"+sep+">");
    //HH nomeArq = limpaNomeArquivo(nomeC);
    nomeArq = vetNomes[0] = limpaNomeArquivo(nomeC); // pega apenas o nome "final" do arquivo, sem caminho
    StringTokenizer st = new StringTokenizer(nomeC, sep);
    while (st.hasMoreTokens()) {
       if (lista) System.out.print(" token="+token);
       if (!token.equals("")) {

       if (token.equals(".")) {
    // tokenAnt = token;
           if (lista) System.out.println(" 1: dir="+dir);
    }
       else
       if (token.equals("..")) {  //&& (dir.length()-tokenAnt.length())>0) // elimine o token anterior
          if (dir.length()>1) {
             dir = dir.substring(0,dir.length()-tokenAnt.length()-1); // desconte o tamanho do token anterior somado ao tam. do separador
             if (lista) System.out.println("tokenAnt="+tokenAnt+" 2.a: dir="+dir);
             }
          else { dir = sep + token;
              if (lista) System.out.println("token==\"..\")="+(token.equals(".."))+"? 2.b: dir="+dir);
       }
   }
       else
       if (!token.equals(".") && !token.equals(nomeArq)) {
          dir += sep + token;
          tokenAnt = token;
          if (lista) System.out.println(" 3: dir="+dir);
   }
       else
          if (lista) System.out.println(" 4: dir="+dir);
       }

       token = st.nextToken();
       }
    // dir = dir.substring(0,dir.length()-1); // eliminar o "sep"
    vetNomes[1] = dir; // pega apenas o nome "final" do arquivo, sem caminho
    // return dir;
    return vetNomes; // devolve nome do arquivo [0] e diret�rio [1]
    }
 

  public static String dadosUsuario () {
    String dados_str = ""; //"[applet]"+MARCALNH+"\n"; // se for applet dar� erro de "sun.applet.AppletSecurityException: checkpropsaccess.key"
    String str1, str2, str3;
    try {
     str1 = System.getProperty("user.dir")+"; ";
    } catch (java.lang.Exception e) {
       str1 = "";
       }
    try {
     str2 = System.getProperty("user.home")+"; ";
    } catch (java.lang.Exception e) {
       str2 = "";
       }
    try {
     str3 = System.getProperty("java.home")+"; ";
    } catch (java.lang.Exception e) {
       str3 = "";
       }
    if (str1!="" || str2!="" || str3!="") { 
       str1  = "[ "+str1;
       str3 += " ]";
       }
    try {
     dados_str = "[" + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
      //InetAddress inetaddress; // se quiser pegar tamb�m a m�quina de quem gravou
      //try {
      //     inetaddress = InetAddress.getLocalHost();
      // } catch (java.net.UnknownHostException unknownhostexception) {
      //     inetaddress = null;
      // }
     dados_str +=  "; "+ System.getProperty("user.name") // + "@" + inetaddress);
               +   "]";
    } catch (java.lang.Exception e) {
       dados_str += "]";
       //System.out.println("GL: "+e);
       }
    return dados_str + str1 + str2 + str3;
    }

  public  static String homeUsuario () {
    String test = "";
    try {
        test = System.getProperty("user.home");
        possivelLerPropriedadesUtilFiles = true;
    } catch (Throwable e) {
        ;
        }
    return test;
    }


  public  static boolean ehPossivelLerPropriedadesUtilFiles () {
    if (!determinaSerPossivelLerPropriedadesUtilFiles) {
       try {
           String test = System.getProperty("user.home");
           possivelLerPropriedadesUtilFiles = true;
       } catch (Throwable e) {
           possivelLerPropriedadesUtilFiles = false;
           }
       determinaSerPossivelLerPropriedadesUtilFiles = true;
       }
    return possivelLerPropriedadesUtilFiles;
    }


  public  static boolean ehPossivelCriarArquivos () {
    if (!determinaSerPossivelCriarArquivos) {
       try {
          String test = System.getProperty("user.home");
          possivelCriarArquivos = true;
       } catch (Throwable e) {
          possivelCriarArquivos = false;
          }
       determinaSerPossivelCriarArquivos = true;
    }
    return possivelCriarArquivos;
    }



  // ----------------------------------------
  // User defined Properties: begin
  public static Properties getPropertiesFromFile (String strFileName) {
    // File file = getFile(strFileName);
    // if (file==null) {
    //   // System.err.println("Error: couldn't find the file '" + strFileName + "'!");
    //   return null;
    //   }

    Properties manifestFile = new Properties();
    try {
      manifestFile.load(new FileInputStream(strFileName));
    } catch (java.io.IOException fnfe) {
      System.err.println("Error: couldn't find the file '" + strFileName + "': " + fnfe.toString());
      return null;
      }

    //DEBUG: System.out.println("UtilFiles.getPropertiesFromFile: " + strFileName);

    // Get a list of (key,value) from the manifest.
    // Use an Enumeration to put them into a Vector?
    //V Vector filelistNames = new Vector();
    //V Vector filelistValues = new Vector();
    Enumeration names = manifestFile.propertyNames();
    int count = 0;
    while (names.hasMoreElements()) {
       String strItem = (String) names.nextElement();
       if (!strItem.startsWith("#")) {
          String strValue = manifestFile.getProperty(strItem);
          //V filelistNames.addElement(strItem);
          //V filelistValues.addElement(strValue);
          //DEBUG: System.out.println(" - " + count + ": " + strItem + " = " + strValue);
          count++;
          }
       else
       if (strItem.startsWith("# created:")) {
          String strDate = strItem.substring(10, strItem.length()).trim();
          manifestFile.put("created", strDate);
          //DEBUG: System.out.println(" + " + count + ": " + strItem + " = " + strDate);
          count++;
          }
       else {
   String strValue = manifestFile.getProperty(strItem);
          //DEBUG: System.out.println(" * " + count + ": " + strItem + " = " + strValue);
          count++;
          }
       }
    return manifestFile;
    }

  public static void setPropertiesFromFile (String strFileName, Properties manifestFile, String header) {
    try {
      manifestFile.save(new FileOutputStream(strFileName), header);
    } catch (java.io.IOException fnfe) {
      System.err.println("Error: couldn't save the file '" + strFileName + "': " + fnfe.toString());
      return;
      }
    // System.out.println("UtilFiles.setPropertiesFromFile: " + strFileName);
    }

  //  User defined Properties: end
  // ----------------------------------------

  public static int parseInt (String str) {
    System.out.println("[Numeros!parseInt] str="+str);
    //StringTokenizer stT = new StringTokenizer(sOriginal,MARCATXT); // "@\'");
    if (str==null || str=="") return Integer.MIN_VALUE; // para erro
    int val = 0;
    int sinal = 1, i = 0;
    char c;
    if (str.charAt(0)=='{') i++;
    while (i<str.length() && str.charAt(i)==' ') i++;
    if (str.charAt(i)=='-') {
       sinal = -1;
       i++;
       }
    while (i<str.length() && (c=str.charAt(i))>='0' && c<='9') {
      //System.out.print(" "+(c-'0'));
      val = 10*val + (c-'0');
      i++;
      }
    //System.out.println("\n[Numeros!parseInt] str="+str+" val="+val);
    return sinal * val;
    }

  // Comunica��o via HTTP

  // Lista 'getDate()', 'getContentType()', 'getExpiration()', 'getLastModified()', 'getContentLength()'
  public static void URLConn_Lista (URLConnection urlConn) {
    try {
      //UtilFiles.URL_lista(urlConn);
      System.out.println("  -> Data       : "+urlConn.getDate());
      System.out.println("  -> Tipo       : "+urlConn.getContentType());
      System.out.println("  -> Expira em  : "+urlConn.getExpiration());
      System.out.println("  -> �lt. Mod.  : "+urlConn.getLastModified());
      System.out.println("  -> Comprimento:"+urlConn.getContentLength());
      }
    catch (Exception e) {
      System.out.println("[UtilFiles!URLConn_lista] Erro: "+e);
      }
    }


  // URL n�o pode ter par�metro para GET
  // P.e., "http://143.107.44.107/~leo/i/Resposta.php&id=2931465" d� erro
  public static String URLConn_Leia (URLConnection urlConn) {
    String str = ""; int c;
    InputStream input = null;
    try {
      try {
         input = urlConn.getInputStream();
         }
      catch (java.io.FileNotFoundException e) {
         System.out.println("[UtilFiles!URLConn_Leia] Erro na abertura de urlConn: "+urlConn.getURL());
         return "";
         }
      int i, j = 0;
      // o PHP tem que fornecer tudo em uma s� linha!
      i = urlConn.getContentLength();
      System.out.println("[UtilFiles!URLConn_Leia] urlConn.getContentLength()="+i);
      while (((c=input.read())!=-1) && (--i>0)) {
         System.out.println((char)c);
         str += c;
         }
      }
    catch (Exception e) {
      System.err.println("Erro: em sistema, ao tentar ler URL: "+e); // "[UtilFiles!URLConn_Leia] Erro: "
      }
    return str;
    } // String URLConn_Leia(URLConnection urlConn) 


  // Chamado em: 
  // Devolve: false => erro; true => OK
  // C�digo baseado em: http://forum.java.sun.com/thread.jspa?threadID=679673
  //                    consultado em 07/07/2008
  public static boolean browserAbrir (String url, Frame frame) {
    strMsgErroBrowser = null;
    String [] browsers = {"epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx"};
    // minimizes a janela que invocou o navegador:
    if (frame != null)
       // Java x: frame.setExtendedState(JFrame.ICONIFIED);
       frame.setState(Frame.ICONIFIED);
    String os = System.getProperty("os.name").toLowerCase();
    Runtime rt = Runtime.getRuntime();
    String [] cmd = null;
    try {
      if (os.indexOf( "win" ) >= 0) {
         cmd = new String[4];
         cmd[0] = "cmd.exe";
         cmd[1] = "/C";
         cmd[2] = "start";
         cmd[3] = url;
         rt.exec(cmd);
         }
      else
      if (os.indexOf( "mac" ) >= 0) {
         cmd = new String[1];
         cmd[0] = "open " + url;
         rt.exec(cmd[0]);
         }
      else {
         // prioritized 'guess' of users' preference
         StringBuffer cmdSB = new StringBuffer();
         for (int i=0; i<browsers.length; i++)
             cmdSB.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
         cmd = new String[] { "sh", "-c", cmdSB.toString() };
         rt.exec(cmd);
         // rt.exec("firefox http://www.matematica.br");
         // System.out.println(cmdSB.toString());
         }
    } catch (Exception e) { // (IOException e)
         String strEr = "The system failed to invoke your default web browser while attempting to access: " + url;
         String strCmd = "", ini = "";
         for (int i=0; i<cmd.length; i++) {
             strCmd += ini+browsers[i];
             ini = ", ";
             }
         strMsgErroBrowser = new String[] { strEr, "Erro: "+e, "Comando: "+cmd[0]+","+cmd[1]+" com browsers: "+strCmd};
         // WarningDialog (String [] comentarios)
         // JanelaDialogo criaTexto = new JanelaDialogo(strEr);
         // Java x: JOptionPane.showMessageDialog(frame, "msg erro" "Browser Error", JOptionPane.WARNING_MESSAGE);
         System.err.println("Erro: ao tentar abrir navegador: "+strCmd);
         e.printStackTrace();
         return false;
         }
    return true;
    } // boolean browserAbrir(String url, Frame frame)
 

  // Chamado em: 
  // Devolve: false => vers�o atual < versao oficial; true => OK
  public static boolean versaoITangram2 () {
    // public static final String addressITangram2 = "http://www.matematica.br/itangram", // endere�o do iTangram2
    //                            versao = ""
    String strVersaoAtual = ""; // pegar em 'addressITangram2' - http://www.matematica.br/igeom/versao.igeom
    URLConnection urlConn = null;
    BufferedReader input;
    URL url = null;
    try {
      url = new URL(addressITangram2+"/"+"versao.php");
      urlConn = url.openConnection();
      input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
      strVersaoAtual = input.readLine(); // tenta pegar a p�gina devolvida pelo PHP chamado em "IGeomApplet.str_param_end_post" (via "echo" ou "print")
      input.close();
    } catch (Exception e) {
      if (ilm.line.itangram2.Tangram.isApplication())
         System.err.println("Error: when I tryed to get new version of from "+addressITangram2+" ("+strVersaoAtual+"): "+e.toString());
      else
         System.err.println("Error: You could verify the last version of iTangram2 at "+addressITangram2+" (your version is " + ilm.line.itangram2.Tangram.VERSION);
      }

    return true;
    }


  }
