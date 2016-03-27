/*
 * iTangram2 - Interactive/Internet Tangram: http://www.matematica.br/itangram
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br / http://www.usp.br/line
 *
 * @author Leo^nidas de Oliveira Branda~o
 *
 * @description Para tratamento multi-linguas
 * 
 * @see 
 *  
 * @credits
 * This source is free and provided by iMath Project (University of São Paulo - Brazil). In order to contribute, please
 * contact the iMath coordinator Leônidas O. Brandão.
 * The original code of this Tangram is from 'javapage@serger.biz'.
 *
 * O código fonte deste programa é livre e desenvolvido pelo projeto iMática (Universidade de São Paulo). Para contribuir,
 * por favor contate o coordenador do projeto iMatica, professor Leônidas O. Brandão.
 * O código original deste Tangram é de 'javapage@serger.biz'.
 * 
 */


package ilm.line.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

// Singleton for Internacionalization
public class Bundle  {

  private static Bundle instanceBundle = null;
  
  private static ResourceBundle resourceBundle;

  private static String lingua = null, pais = null; // para configurar lingua em chamado do iTangram: java ilm.line.itangram2.Tangram lang=pt

  // Decompõe: 'lang=pt_BR' em String("pt","BR")
  private static boolean decompoeConfig (String str) {
    //D System.out.println("[B.decompoeConfig] inicio: str="+str);
    //D try {String str1=""; System.err.println(str1.charAt(3)); } catch(Exception e) {e.printStackTrace();}
    if (str==null)
       return false;
    StringTokenizer tokens = new StringTokenizer(str,"=");
    String item;
    int tam_item;

    if (tokens.hasMoreTokens()) {
       item = tokens.nextToken();
       //_/*//-*/ System.out.println("[Bundle.decompoeConfig] item="+item);
       if (item==null) return false;
       if (item.equals("lang") && tokens.hasMoreTokens()) {
          // pegou 'pt_BR'
          item = tokens.nextToken();
          tam_item = item.length();
          //_/*//-*/ System.out.println("[Bundle.decompoeConfig] item="+item+" #item="+tam_item);
          if (tam_item>2) {
             // é da forma: 'pt_BR'
             lingua = item.substring(0,2); //
             if (tam_item>4)
                pais = item.substring(3,tam_item).toUpperCase(); //
             //_/*//-*/ System.out.println("[Bundle.decompoeConfig] lingua="+lingua+" pais="+pais);
             return true;
             }
          else {
             // é da forma: 'pt'
             lingua = item.substring(0,2); //
             // System.out.println("[B.decompoeConfig] lingua="+lingua+" [pais="+pais+"]");
             return true;
             }
          }
       else
       if (item.equals("bg") && tokens.hasMoreTokens()) {
          // pegou 'pt_BR'
          item = tokens.nextToken();
          if (item!=null && item.equals("contrast1")) // Bundle.msg("contraste")
             ; // para colocar em modo contraste
          }
       else { // problema: veio 'lang='
          return false;
          }
       }
    return false;
    } // boolean decompoeConfig(String str)


  // iTangram aplicativo: define lingua, tem prioridade sobre outros métodos
  //                   java -jar iTangram2.jar ilm.line.itangram2 lang=es
  // Parâm. : 'lingua', 'pais' ou 'lang' (nesta ordem) -> param name='lang' value="pt" ou "en" ou "es" (default: "pt")
  public static void setConfig (String [] args) {
    // lang=pt; lang=en; lang=es
    int i = -1;
    if (lingua==null || lingua=="") // evita sobrescrever definicao
       lingua = "pt"; // default
    pais = "BR";
    boolean definedLang = false;

    //_/*//-*/ System.out.println("[Bundle.setConfig] #args=" + args.length);
    //_/*//-*/ System.out.println("[Bundle.setConfig] args=" + ilm.line.util.Util.listStrArray(args,false));
     
    if (args!=null && args.length>0) {
       String item;
       //_/*//-*/ System.out.println("[Bundle.setConfig] args=" + ilm.line.util.Util.listStrArray(args,false));
       for (i=0; i<args.length; i++) {
           item = args[i].trim(); // args[i].toLowerCase().trim(); // tokens.nextToken().toLowerCase();
           //_/*//-*/ System.out.println(" ("+i+","+item+") ");
           try {
             if (decompoeConfig(item)) {
                defineBundle(true);
                definedLang = true;
                //_/*//-*/ System.out.println(" decompoeConfig <- OK");
                }
             //_/*//-*/ else System.out.println(" decompoeConfig <- NAO");
           } catch (Exception e) { System.err.println("Erro: leitura de parametros para configuracao: "+e);
             e.printStackTrace(); }
           } // for (i=0; i<args.length; i++)
       }

    if (!definedLang) { // default LANG
       pais = "BR";
       lingua = "pt";
       defineBundle(true);
       }

    if (ilm.line.itangram2.Tangram.isApplication()) try {
      Locale loc = new Locale(lingua,pais);
      Locale.setDefault(loc);
      } catch (Exception e2) { e2.printStackTrace(); }
    //- System.out.println("\nsrc/ilm/line/util/Bundle.java: setConfig(...): lingua=" + lingua + ", pais=" + pais + ", resourceBundle=" + resourceBundle + ": i="+i);
    //- System.out.println(Bundle.msg(ilm.line.itangram2.TangramProperties.DIALOG_COMPARE_MESSAGE)+"\n");
    }

  //
  public static void defineBundle (boolean chamaDefine) {
    String msg_nome_default = "Messages", msg_nome,
           lingua_aux, pais_aux;
    lingua_aux = (lingua!=null && lingua.length()>0 && lingua.charAt(0)!='_') ? "_"+lingua : lingua;
    pais_aux = (pais!=null && pais.length()>0 && pais.charAt(0)!='_') ? "_"+pais : pais;

    String s_aux = instanceBundle!=null ? instanceBundle.msg("iTangram") : "Interactive Tangram in the Internet"; // iTangram
    System.out.println("\n .: iTangram : " + s_aux + " :."); // iTangram
    System.out.println(  "    Version: " + ilm.line.itangram2.Tangram.VERSION + "\n"); // iTangram


    // Com linha abaixo => aplicativo tenta entrar no prim. 'ResourceBundle=ResouceBundle...' com 'Messages_pt_BR_pt_BR"
    msg_nome = "ilm.line.util/Messages" + lingua_aux.toLowerCase() + pais_aux.toUpperCase();

    Locale locale;
    if (lingua!=null && lingua.equalsIgnoreCase("pt")) locale = new Locale("pt", "BR");
    else
    if (lingua!=null && lingua.equalsIgnoreCase("en")) locale = new Locale("en", "US");
    else locale = new Locale("pt", "BR");
    resourceBundle = ResourceBundle.getBundle(msg_nome, locale); // ./Messages_en_US.properties
    if (resourceBundle!=null) {
       return;
       }

    // 'Messages*.properties'
    try { //try1
        // 'Messages_lingua_pais.properties'
        try { //try2
          resourceBundle = ResourceBundle.getBundle(msg_nome);
          //_/*//-*/ System.out.println("1: msg_nome="+msg_nome);
        } catch (Exception e_lingua_pais) { // (java.util.MissingResourceException mre)
          // Tente agora só com lingua
          msg_nome = "Messages"+lingua_aux.toLowerCase();
          // 'Messages_lingua_pais.properties'
          try { //try3
            resourceBundle = ResourceBundle.getBundle(msg_nome);
            //_/*//-*/ System.out.println("2: msg_nome="+msg_nome);
          } catch (Exception e_lingua) { // (java.util.MissingResourceException mre)
            // msgGeomInteratInternet = Geometria Interativa na Internet

            try { //try4
              // usualmente entra aqui: ao fazer 'java ilm.line.itangram2.Tangram ...'
              resourceBundle = ResourceBundle.getBundle("Messages"); // ./Messages_en_US.properties
            } catch (Exception e) {
              System.err.println(" Tenta: tentaResourceURL:"+" msg_nome="+msg_nome+": "+e);
              // tentaResourceURL(msg_nome);
              e.printStackTrace();
              } //try-catch4

           } //try-catch3

        } //try-catch2

    } catch (java.util.MissingResourceException mre) {
      System.err.println("Erro: ResourceBundle: " + mre);
      }

    instanceBundle = new Bundle(lingua, pais);

    } // void defineBundle(boolean chamaDefine)


  private Bundle (String language, String country) {
    Locale currentLocale = new Locale(language, country);
    try {
      resourceBundle = ResourceBundle.getBundle("Messages",currentLocale);
    } catch (MissingResourceException e) {
      System.err.println("Erro: falta o arquivo de mensagens para linguas! Definido: "+language+"_"+country+": "+e);
      System.err.println("Error: there is missing the message file! Definided: "+language+"_"+country+": "+e);
      //e.printStackTrace();
      }
    }
  
  public static Bundle getInstance () {
    if (instanceBundle == null)
       instanceBundle = new Bundle("en","us");
    return instanceBundle;
    }

  // not necessary with 'static msg(String)'
  public static void changeInstance (String language, String country)  {
    instanceBundle = new Bundle(language,country);
    }

  public static String msg (String key) {
    if (resourceBundle==null) return key; // para evitar erro de 'java.lang.NullPointerException' no caso de não ter criado Bundle
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      return key;
      }
    }

  public static String msg (String key, Object[] parametros) {
    try {
      String message = resourceBundle.getString(key);
      int pos = message.indexOf("?");
      int k=0;
      while (pos >=0) {
      	if (k< parametros.length) {
           message = message.substring(0,pos) + parametros[k++] + message.substring(pos+1);
           pos = message.indexOf("?");
           }
	else {
           break;
           }
        }
      return message;
    } catch(MissingResourceException e) {
      return key;
      }
    }

  }
