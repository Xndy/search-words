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

    public List<PdfMarcador> printSubwordsString(String filePath, String searchTerm) throws IOException {
        PDDocument document = PDDocument.load(new File(filePath));
        List<PdfMarcador> marcadoresTemp = new ArrayList<>();
        System.out.printf("* Looking for '%s'\n", searchTerm);
        for (int page = 1; page <= document.getNumberOfPages(); page++) {
            List<TextPositionSequence> hits = findSubwords(document, page, searchTerm);
            System.out.println("size hits: " + hits.size());
            if (Utils.isNotEmpty(hits)) {
                for (TextPositionSequence hit : hits) {
                    if (hit.getWord().startsWith(searchTerm)) {
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

                    }
                }
            }
        }

        List<PdfMarcador> marcadores = new ArrayList<>();
        for (PdfMarcador m : marcadoresTemp) {
            /* Integer[] posiciones = printSubwordsByPage(document, m.getTexto(), m.getNumeroPagina(), m.getVecesRepetidas());
            if (posiciones != null) {
                m.setMovimientoX(posiciones[0]);
                m.setMovimientoY(posiciones[1]);
                m.setTexto(m.getTexto().replace(searchTerm, ""));
                marcadores.add(m);
            }*/
            List<Integer[]> posiciones = printSubwordsImprovedBypage(document, m.getTexto(), m.getNumeroPagina());

            System.out.println("\nusuario : " + m.getTexto() + " posiciones: " + posiciones.size());
            for (int i = 0; i <= posiciones.size() - 1; i++) {
                Integer[] tp = posiciones.get(i);

                PdfMarcador pdf = new PdfMarcador();
                pdf.setNumeroPagina(m.getNumeroPagina());
                pdf.setTexto(m.getTexto().replace(searchTerm, ""));
                pdf.setMovimientoX((int) tp[0]);
                pdf.setMovimientoY((int) tp[1]);
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

}
