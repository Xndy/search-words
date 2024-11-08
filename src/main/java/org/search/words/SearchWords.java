/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.search.words;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static String ruta = "D://Documents//Catalogo electrónico orden 29.pdf";

    public static void main(String[] args) {
        //   buscarFirmas();
        buscarFirmas("Persona que autoriza", 2);
    }

    public static void buscarFirmas() {
        try {
            List<PdfMarcador> usuariosFirma = new ArrayList<>();
            System.out.println("buscarFirmas");

            List<PdfMarcador> marcadores = new SearchSubword().printSubwordsString(ruta, Variables.firmaCodigo, false);

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

    public static void buscarFirmas(String palabra, Integer pagina) {
        try {
            List<PdfMarcador> usuariosFirma = new ArrayList<>();
            if (palabra.equals(Variables.firmaCodigo)) {
                usuariosFirma = new ArrayList<>();
            }
            if (Utils.isEmpty(usuariosFirma)) {
                usuariosFirma = new ArrayList<>();
            }
            System.out.println("buscarFirmas");
            List<PdfMarcador> marcadores;
            if (pagina == null) {
                marcadores = new SearchSubword().printSubwordsString(ruta, palabra, palabra.equals(Variables.firmaCodigo));
            } else {
                List<PdfMarcador> temps = new ArrayList<>();
                PdfMarcador pdf = new PdfMarcador();
                pdf.setNumeroPagina(pagina);
                pdf.setTexto(palabra);
                temps.add(pdf);
                marcadores = new SearchSubword().printSubwordsString(ruta, temps);
            }

            if (Utils.isNotEmpty(marcadores)) {

                for (PdfMarcador marcador : marcadores) {
                    System.out.println("marcador: " + marcador.getTexto());
                    if (palabra.equals(Variables.firmaCodigo)) {
                        UsuarioDetalle detalle = new UsuarioDetalle(marcador.getTexto());
                        marcador.setUsuarioDetalle(detalle);
                    }
                    usuariosFirma.add(marcador);
                    System.out.println("\nMarcador: " + marcador.toString());
                }
                verPosicion(marcadores.get(0));
            } else {
                if (!palabra.equals(Variables.firmaCodigo)) {
                    System.out.println("No se encontraron coincidencias");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verPosicion(PdfMarcador marcador) {
        Integer x = marcador.getMovimientoX();
        Integer y = marcador.getMovimientoY();
        Integer pagina = marcador.getNumeroPagina();
        try {
            String docextraido = SearchSubword.extractPageToNewPDF(ruta, pagina);
            String fileName = "extracted_page_" + System.currentTimeMillis() + ".pdf";

            // Obtener la ruta del directorio del archivo de entrada
            File inputFile = new File(ruta);
            String nuevaruta = inputFile.getParent() + File.separator + fileName;
            SearchSubword.escribirSobrePDF(docextraido,  "XXXXXXX", x, y - 40);
            Utils.abrirDocumento(docextraido);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
