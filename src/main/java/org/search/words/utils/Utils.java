/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.search.words.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author Origami
 */
public class Utils {

    public static boolean isEmpty(Collection l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmpty(Collection l) {
        return !Utils.isEmpty(l);
    }

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !Utils.isEmptyString(l);
    }
    
      public static void abrirDocumento(String documento) throws IOException {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            String cmd = "rundll32 url.dll,FileProtocolHandler " + documento;
            Runtime.getRuntime().exec(cmd);
        } else {
            File doc = new File(documento);
            Desktop.getDesktop().open(doc);
        }
    }
    
}
