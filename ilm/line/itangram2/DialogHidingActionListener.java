/*
 * iTangram2 - Interactive/Internet Geometry: http://www.matematica.br/itangram/2/
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br / http://www.usp.br/line
 *
 * @author Leo^nidas O. Branda~o LInE-IME-USP / Adapted from Tangram by Serge
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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.*;

class DialogHidingActionListener implements ActionListener {

  private Dialog toHide;

  DialogHidingActionListener (Dialog toHide) {
    this.toHide = toHide;
    }

  public void actionPerformed (ActionEvent e) {
    toHide.setVisible(false);
    }

  }

