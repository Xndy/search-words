/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.search.words;

import java.util.ArrayList;
import java.util.List;
import org.search.words.models.PdfMarcador;
import org.search.words.models.UsuarioDetalle;
import org.search.words.service.SearchSubword;
import org.search.words.utils.Utils;
import org.search.words.utils.Variables;

/**
 *
 * @author Origami
 */
public class SearchWords {

    public static String ruta = "D://Documentos//Firma3.pdf";

    public static void main(String[] args) {
        buscarFirmas();
    }

    public static void buscarFirmas() {
        try {
            List<PdfMarcador> usuariosFirma = new ArrayList<>();
            System.out.println("buscarFirmas");

            List<PdfMarcador> marcadores = new SearchSubword().printSubwordsString(ruta, Variables.firmaCodigo);

            if (Utils.isNotEmpty(marcadores)) {

                for (PdfMarcador marcador : marcadores) {

                    UsuarioDetalle detalle = new UsuarioDetalle(marcador.getTexto());
                    marcador.setUsuarioDetalle(detalle);
                    usuariosFirma.add(marcador);
                    System.out.println("\nMarcador: " + marcador.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
