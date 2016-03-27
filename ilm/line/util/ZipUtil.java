/*
 * iTangram2 - Interactive/Internet Tangram: http://www.matematica.br/itangram
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br
 *
 * @author Tulio Faria <tuliofaria@gmail.com> / LOB
 *
 * @description Class that holds the ZIP utilities in Tangram.
 * 
 * @see 
 *  
 * @credits
 * This source is free and provided by iMath Project (University of São Paulo - Brazil). In order to contribute, please
 * contact the iMath coordinator Leônidas O. Brandão.
 *
 * O código fonte deste programa é livre e desenvolvido pelo projeto iMática (Universidade de São Paulo). Para contribuir,
 * por favor contate o coordenador do projeto iMatica, professor Leônidas O. Brandão.
 * 
 */


package ilm.line.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;


public class ZipUtil {

  private List files;

  public ZipUtil () {
    this.files = new ArrayList();
    }

  public void addFile(String entryName, String contents) {
    ZipItem zi = new ZipItem(entryName, contents);
    files.add(zi);
    }

  public void addFile(String entryName, File file) {
    ZipItem zi = new ZipItem(entryName, file);
    files.add(zi);
    }
  public void addFile(String entryName, InputStream is) {
    ZipItem zi = new ZipItem(entryName, is);
    files.add(zi);
    }

  public void saveToFile(String filepath) {
    InputStream in = new ByteArrayInputStream(getZipAsOutputStream().toByteArray());

    File arquivo = new File(filepath);
    try {
      FileOutputStream out = new FileOutputStream(arquivo);
      byte[] buffer = new byte[1024 * 4]; //4 Kb  
      int n = 0;
      while (-1 != (n = in.read(buffer))) {
          out.write(buffer, 0, n);
        }
      out.flush();

      out.close();
      in.close();
    } catch (Exception e) {
      System.err.println("ZipUtil.java: error: " + e);
      }
    }


  public ByteArrayOutputStream getZipAsOutputStream () {
    System.out.println("Model.java: createPackage(): ---");
    int totalSize = 0;
    String strFileContent;
    try {
      int level = 9;
      ByteArrayOutputStream fout = new ByteArrayOutputStream();
      ZipOutputStream zout = new ZipOutputStream(fout);
      zout.setLevel(level);

      for (int i = 0; i < files.size(); i++) {
        ZipItem zi = (ZipItem) files.get(i);
        System.out.println("Compressing file: " + zi.entryName);

        // se colocar o path antes do nome do arquivo, gera com
        // diretorio ex: "teste/"+(String) fileNames.get(i)
        ZipEntry ze = new ZipEntry((String) zi.entryName); // get the name of the file 'i'
        zout.putNextEntry(ze);

        if (zi.text) {
          strFileContent = zi.contents; // get the content of file 'i'
          zout.write(strFileContent.getBytes());
          }
	else
	if (zi.file != null) {
          FileInputStream fis = new FileInputStream(zi.file);
          byte[] buffer = new byte[1024 * 4]; //4 Kb  
          int n = 0;
          while (-1 != (n = fis.read(buffer))) {
            zout.write(buffer, 0, n);
            }
          }
	else
	if (zi.is != null) {
          byte[] buffer = new byte[1024 * 4]; //4 Kb  
          int n = 0;
          while (-1 != (n = zi.is.read(buffer))) {
            zout.write(buffer, 0, n);
            }
          }
        zout.closeEntry();
        } // for (int i = 0; i < files.size(); i++)

      System.out.println("Model.java: createPackage(): #=" + totalSize + " ---");
      zout.flush();
      zout.finish();

      zout.close();
      fout.close();

      return fout;

    } catch (IOException e1) {
      return null;
    } catch (Exception e1) {
      return null;
      }

    } // public ByteArrayOutputStream getZipAsOutputStream()


  ///
  private class ZipItem {

    protected String entryName;
    protected String contents;
    protected InputStream is;
    protected File file;
    protected boolean text;

    public ZipItem (String entryName, InputStream is) {
      this.entryName = entryName;
      this.is = is;
      this.text = false;
      }

    public ZipItem (String entryName, File file) {
      this.entryName = entryName;
      this.file = file;
      this.text = false;
      }

    public ZipItem (String entryName, String contents) {
      this.entryName = entryName;
      this.contents = contents;
      this.text = true;
      }

      } // private class ZipItem

  } // public class ZipUtil
