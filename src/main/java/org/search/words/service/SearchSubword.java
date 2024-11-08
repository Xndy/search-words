/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.search.words.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;
import org.search.words.models.PdfMarcador;
import org.search.words.models.TextPositionSequence;
import org.search.words.utils.Utils;

/**
 * @author mkl
 */
public class SearchSubword {

    public List<PdfMarcador> printSubwordsString(String filePath, String searchTerm, Boolean split) throws IOException {
        PDDocument document = PDDocument.load(new File(filePath));
        List<PdfMarcador> marcadoresTemp = new ArrayList<>();
        System.out.printf("* Looking for '%s'\n", searchTerm);
        for (int page = 1; page <= document.getNumberOfPages(); page++) {
            List<TextPositionSequence> hits = findSubwords(document, page, searchTerm);
            System.out.println("size hits: " + hits.size());
            if (Utils.isNotEmpty(hits)) {
                for (TextPositionSequence hit : hits) {
                    if (hit.getWord().trim().startsWith(searchTerm)) {
                        if (split) {
                            String[] words = hit.getWord().split(" ");
                            for (int i = 0; i < words.length; i++) {
                                String w = words[i];
                                if (w.trim().length() > 2) {
                                    PdfMarcador pdf = new PdfMarcador();
                                    pdf.setNumeroPagina(page);
                                    pdf.setTexto(w);
                                    if (!marcadoresTemp.contains(pdf)) {
                                        marcadoresTemp.add(pdf);
                                    }
                                }
                            }
                        } else {
                            PdfMarcador pdf = new PdfMarcador();
                            pdf.setNumeroPagina(page);
                            pdf.setTexto(hit.getWord());
                            if (!marcadoresTemp.contains(pdf)) {
                                marcadoresTemp.add(pdf);
                            }
                        }

                    }
                }
            }
        }

        return buscarPosiciones(document, searchTerm, marcadoresTemp);

    }

    public List<PdfMarcador> printSubwordsString(String filePath, List<PdfMarcador> temps) {

        try {
            PDDocument document = PDDocument.load(new File(filePath));
            return buscarPosiciones(document, "", temps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<PdfMarcador> buscarPosiciones(PDDocument document, String searchTerm, List<PdfMarcador> temps) throws IOException {
        List<PdfMarcador> marcadores = new ArrayList<>();
        for (PdfMarcador m : temps) {
            List<Integer[]> posiciones = printSubwordsImprovedBypage(document, m.getTexto(), m.getNumeroPagina());

            System.out.println("\nusuario : " + m.getTexto() + " posiciones: " + posiciones.size());
            for (int i = 0; i <= posiciones.size() - 1; i++) {
                Integer[] tp = posiciones.get(i);

                PdfMarcador pdf = new PdfMarcador();
                pdf.setNumeroPagina(m.getNumeroPagina());
                pdf.setTexto(m.getTexto().replace(searchTerm, ""));
                pdf.setMovimientoX((int) tp[0]);
                pdf.setMovimientoY((int) tp[1] + 40); //Se suma 40 para que no quede justo en la posicion
                pdf.setVecesRepetidas(i + 1);

                marcadores.add(pdf);

            }
        }
        return marcadores;
    }

    public Integer[] printSubwordsByPage(PDDocument document, String searchTerm, Integer page, Integer intento) throws IOException {
        System.out.printf("* Looking for '%s'\n", searchTerm);
        final Integer[] result = new Integer[3];

        List<TextPositionSequence> hits = findSubwords(document, page, searchTerm);
        System.out.println("size hits: " + hits.size());
        if (Utils.isNotEmpty(hits)) {

            int contador = 1;
            for (TextPositionSequence hit : hits) {
                if (hit.getWord().contains(searchTerm)) {
                    if (contador == intento) {
                        result[0] = (int) hit.getX();
                        result[1] = (int) hit.getY();
                        result[2] = page;
                    }
                    break;
                }
                contador++;
            }
        }

        return result;
    }

    public Integer[] printSubwords(PDDocument document, String searchTerm) throws IOException {
        System.out.printf("* Looking for '%s'\n", searchTerm);
        final Integer[] result = new Integer[3];
        for (int page = 1; page <= document.getNumberOfPages(); page++) {
            List<TextPositionSequence> hits = findSubwords(document, page, searchTerm);
            System.out.println("size hits: " + hits.size());
            if (Utils.isNotEmpty(hits)) {
                Boolean existe = Boolean.FALSE;
                for (TextPositionSequence hit : hits) {

                    if (hit.getWord().equals(searchTerm)) {
                        result[0] = (int) hit.getX();
                        result[1] = (int) hit.getY();
                        result[2] = page;
                        existe = Boolean.TRUE;
                        break;
                    } else {
                        existe = Boolean.FALSE;
                    }

                }
                if (existe) {
                    break;
                }
            }
        }
        return result;
    }

    public List<Integer[]> printSubwordsImprovedBypage(PDDocument document, String searchTerm, Integer pagina) throws IOException {
        List<Integer[]> positions = new ArrayList<>();
        System.out.printf("\n* Buscando '%s' (mjorado)\n", searchTerm);

        List<TextPositionSequence> hits = findSubwordsImproved(document, pagina, searchTerm);
        for (TextPositionSequence hit : hits) {
            Integer[] result = new Integer[2];
            System.out.printf("\n hit (%s) ", hit.toString());
            if (hit.toString().startsWith(searchTerm)) {
                System.out.printf("\n valid (%s) ", hit.toString());

                TextPosition lastPosition = hit.textPositionAt(hit.length() - 1);
                System.out.printf("\n  Pagina %s at %s, %s con ancho %s y ultima letra '%s' at %s, %s\n",
                        pagina, hit.getX(), hit.getY(), hit.getWidth(),
                        lastPosition.getUnicode(), lastPosition.getXDirAdj(), lastPosition.getYDirAdj());
                System.out.printf("\n Ultima posicion: %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
                        lastPosition.toString(),
                        lastPosition.getX(),
                        lastPosition.getY(),
                        lastPosition.getEndX(),
                        lastPosition.getEndY(),
                        lastPosition.getHeight(),
                        lastPosition.getPageHeight(),
                        lastPosition.getPageWidth(),
                        lastPosition.getXScale(),
                        lastPosition.getYScale()
                );
                result[0] = (int) hit.getX();
                result[1] = (int) hit.getY();
                positions.add(result);
            }
        }
        return positions;
    }

    public void printSubwordsImproved(PDDocument document, String searchTerm) throws IOException {
        System.out.printf("* Buscando '%s' (mjorado)\n", searchTerm);
        for (int page = 1; page <= document.getNumberOfPages(); page++) {
            List<TextPositionSequence> hits = findSubwordsImproved(document, page, searchTerm);
            for (TextPositionSequence hit : hits) {
                System.out.printf("  hit (%s) ", hit.toString());
                if (hit.toString().startsWith(searchTerm)) {
                    System.out.printf("  valido (%s) ", hit.toString());
                    TextPosition lastPosition = hit.textPositionAt(hit.length() - 1);
                    System.out.printf("  Pagina %s at %s, %s con ancho %s y ultima letra '%s' at %s, %s\n",
                            page, hit.getX(), hit.getY(), hit.getWidth(),
                            lastPosition.getUnicode(), lastPosition.getXDirAdj(), lastPosition.getYDirAdj());
                    System.out.printf("Ultima posicion: %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
                            lastPosition.toString(),
                            lastPosition.getX(),
                            lastPosition.getY(),
                            lastPosition.getEndX(),
                            lastPosition.getEndY(),
                            lastPosition.getHeight(),
                            lastPosition.getPageHeight(),
                            lastPosition.getPageWidth(),
                            lastPosition.getXScale(),
                            lastPosition.getYScale()
                    );
                }

            }
        }
    }

    List<TextPositionSequence> findSubwords(PDDocument document, int page, String searchTerm) throws IOException {
        final List<TextPositionSequence> hits = new ArrayList<>();
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                System.out.printf("  --text %s\n", text);

                TextPositionSequence word = new TextPositionSequence(textPositions, searchTerm);
                String string = word.toString();
                System.out.printf("  --word %s\n", word.toString());
                int fromIndex = 0;
                int index;
                while ((index = string.indexOf(searchTerm, fromIndex)) > -1) {
                    hits.add(word.subSequence(index, index + string.length(), string));
                    fromIndex = index + 1;
                }
                super.writeString(text, textPositions);
            }
        };

        stripper.setSortByPosition(true);
        stripper.setStartPage(page);
        stripper.setEndPage(page);
        stripper.getText(document);
        return hits;
    }

    List<TextPositionSequence> findSubwordsImproved(PDDocument document, int page, String searchTerm) throws IOException {
        final List<TextPosition> allTextPositions = new ArrayList<>();
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                allTextPositions.addAll(textPositions);
                super.writeString(text, textPositions);
            }

            @Override
            protected void writeLineSeparator() throws IOException {
                if (!allTextPositions.isEmpty()) {
                    TextPosition last = allTextPositions.get(allTextPositions.size() - 1);
                    if (!" ".equals(last.getUnicode())) {
                        Matrix textMatrix = last.getTextMatrix().clone();
                        textMatrix.setValue(2, 0, last.getEndX());
                        textMatrix.setValue(2, 1, last.getEndY());
                        TextPosition separatorSpace = new TextPosition(last.getRotation(), last.getPageWidth(), last.getPageHeight(),
                                textMatrix, last.getEndX(), last.getEndY(), last.getHeight(), 0, last.getWidthOfSpace(), " ",
                                new int[]{' '}, last.getFont(), last.getFontSize(), (int) last.getFontSizeInPt());
                        allTextPositions.add(separatorSpace);
                    }
                }
                super.writeLineSeparator();
            }
        };

        stripper.setSortByPosition(true);
        stripper.setStartPage(page);
        stripper.setEndPage(page);
        stripper.getText(document);

        final List<TextPositionSequence> hits = new ArrayList<TextPositionSequence>();
        TextPositionSequence word = new TextPositionSequence(allTextPositions, searchTerm);
        String string = word.toString();
        //System.out.printf("  -- %s\n", string);

        int fromIndex = 0;
        int index;
        while ((index = string.indexOf(searchTerm, fromIndex)) > -1) {
            hits.add(word.subSequence(index, index + searchTerm.length()));
            fromIndex = index + 1;
        }

        return hits;
    }

    public static void createPDF(int x, int y, String pdfPath) throws IOException {

        /// CREAR DOCUMENTO
        PDDocument document = new PDDocument();
        /// CREAR PAGINA
        PDPage page = new PDPage();
        /// AGREGAR LA PAGINA AL DOCUMENTO
        document.addPage(page);
        /// CREAR UN STREAM PARA AGREGAR TEXTO
        PDPageContentStream stream = new PDPageContentStream(document, page);
        /// CREAR UN OBJETO CON LA FUENTE QUE VAMOS A USAR PARA EL TEXTO
        PDFont font = PDType1Font.HELVETICA;
        /// INICIAMOS EL STREAM DE TEXTO
        stream.beginText();
        /// DEFINIMOS LA FUENTE Y EL TAMA~O PARA EL TITULO
        stream.setFont(font, 24);
        /// DEFINIMOS LAS COORDENADAS PARA INICIAR EL TEXTO
        stream.newLineAtOffset(10, 760);
        /// MOSTRAMOS EL TEXTO/ TITULO
        stream.showText("Crear PDF con PDFBox en Java");
        /// DEFINIMOS EL LA FUENTE Y TAMA~O PARA LOS PARRAFOS
        stream.setFont(font, 12);
        /// DEFINIMOS LAS COORDENADAS EN REFERENCIA A LA DEFINICION DE COORDENADAS ANTERIOR
        stream.newLineAtOffset(x, y);
        /// MOSTRAMOS EL TEXTO DE UN PARRAFO
        stream.showText("Hola Mundo !!!");
        /// DEFINIMOS LAS COORDENADAS EN REFERENCIA A LA DEFINICION DE COORDENADAS ANTERIOR
        stream.newLineAtOffset(0, -10);
        /// MOSTRAMOS EL TEXTO DE OTRO PARRAFO
        stream.showText("Linea 3!");
        /// FINALIZAMOS EL STREAM DE TEXTO
        stream.endText();
        /// CERRAMOS EL STREAM
        stream.close();
        /// GUARDAMOS EL DOCUMENTO
        document.save(pdfPath);
        /// CERRAMOS EL DOCUMENTO
        document.close();

    }

    public static void updatePDF(int x, int y, String pdfPath) throws IOException {
        System.out.println("x " + x);
        System.out.println("y " + y);
        // Cargar el documento PDF existente
        PDDocument document = PDDocument.load(new File(pdfPath));

        // Obtener la primera página (puedes cambiar esto para obtener cualquier página específica)
        PDPage page = document.getPage(0); // Obtener la primera página (índice 0)

        // Crear un flujo de contenido para agregar texto o gráficos
        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true);

        // Comienza el contexto de texto
        contentStream.beginText();

        // Asegurarnos de que no haya rotaciones o transformaciones aplicadas
        Matrix matrix = new Matrix(1, 0, 0, 1, x, y);  // Sin rotación, solo desplazamiento
        contentStream.setTextMatrix(matrix);
        // contentStream.setTextMatrix(1, 0, 0, 1, x, y);  // Establece la matriz de texto "normal" (sin rotaciones ni volteos)
//contentStream.setTextMatrix(1, 0, 0, 1, 0, 0); 
        // Establecer la fuente y tamaño
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);  // Usar una fuente estándar

        // Agregar el texto
        contentStream.showText("Texto agregado al PDF en: (" + x + ", " + y + ")");

        // Finalizar el bloque de texto
        contentStream.endText();

        // Dibujar un "punto" en las coordenadas (x, y)
        contentStream.setLineWidth(1f);  // Grosor de la línea
        contentStream.moveTo(x, y);  // Mover a las coordenadas donde dibujaremos el punto
        contentStream.lineTo(x + 1, y);  // Dibujar una pequeña línea de 1 unidad para simular un punto
        contentStream.stroke();  // Traza el punto (realmente traza una línea muy pequeña)

        // Cerrar el flujo de contenido
        contentStream.close();

        // Sobrescribir el archivo original con el PDF modificado
        document.save(pdfPath);

        // Cerrar el documento
        document.close();

        System.out.println("Documento PDF modificado y guardado en: " + pdfPath);
    }

    public static String extractPageToNewPDF(String inputPdfPath, int pageNumber) throws IOException {
        // Cargar el documento PDF original
        PDDocument document = PDDocument.load(new File(inputPdfPath));

        // Verificar que el número de página esté dentro del rango válido
        if (pageNumber < 1 || pageNumber > document.getNumberOfPages()) {
            throw new IllegalArgumentException("Número de página inválido. El PDF tiene " + document.getNumberOfPages() + " páginas.");
        }

        // Crear un nuevo documento PDF
        PDDocument newDocument = new PDDocument();

        // Extraer la página especificada (recordar que PDFBox usa índices 0-based)
        PDPage page = document.getPage(pageNumber - 1);

        // Añadir la página al nuevo documento
        newDocument.addPage(page);

        // Generar un nombre de archivo con la fecha y hora actual en milisegundos
        String fileName = "extracted_page_" + System.currentTimeMillis() + ".pdf";

        // Obtener la ruta del directorio del archivo de entrada
        File inputFile = new File(inputPdfPath);
        String outputPdfPath = inputFile.getParent() + File.separator + fileName;

        // Guardar el nuevo documento PDF en la misma carpeta que el archivo de entrada
        newDocument.save(outputPdfPath);

        // Cerrar los documentos para liberar recursos
        document.close();
        newDocument.close();

        System.out.println("Página " + pageNumber + " ha sido extraída a un nuevo archivo PDF: " + outputPdfPath);
        System.out.println("Página " + pageNumber + " ha sido extraída a un de archivo PDF: " + inputPdfPath);
        return outputPdfPath;
    }

    public static void escribirSobrePDF(String archivoEntrada, String texto, float x, float y) {
        try {
            // Cargar el PDF existente
            PDDocument document = PDDocument.load(new java.io.File(archivoEntrada));

            // Obtener la primera página del documento
            PDPage pagina = document.getPage(0);

            // Corregir rotación si es necesario
            int rotation = pagina.getRotation();
            if (rotation != 0) {
                pagina.setRotation(0); // Restablecer la rotación a 0 grados
            }

            // Obtener la altura de la página
            float paginaAltura = pagina.getMediaBox().getHeight();

            // Crear un PDPageContentStream para escribir sobre la página
            PDPageContentStream contentStream = new PDPageContentStream(document, pagina, 
                    PDPageContentStream.AppendMode.APPEND, true);

            // Establecer el color rojo para el texto
            contentStream.setNonStrokingColor(1.0f, 0.0f, 0.0f); // Color rojo (RGB: 1, 0, 0)

            // Seleccionar la fuente y tamaño del texto
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12); // Fuente Helvetica, tamaño 12

            // Asegurarse de que no haya rotación aplicada a la página ni a la transformación del texto
            contentStream.beginText();

            // Ajustar la coordenada y considerando el sistema de coordenadas de PDFBox
            contentStream.newLineAtOffset(x, paginaAltura - y); // Reemplazamos y con (paginaAltura - y)

            // Escribir el texto
            contentStream.showText(texto);
            contentStream.endText();

            // Cerrar el contentStream
            contentStream.close();

            // Guardar los cambios directamente en el mismo archivo
            document.save(archivoEntrada); // Sobrescribe el archivo original

            // Cerrar el documento
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
