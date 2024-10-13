/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.search.words.models;

import org.apache.pdfbox.text.TextPosition;

import java.util.List;

/**
 * This class implements {@link CharSequence} for a list of {@link TextPosition}
 * instances to allow for use as a {@link String}-like object in case of
 * character-oriented operations.
 *
 * @author mkl
 */
public class TextPositionSequence implements CharSequence {
    public TextPositionSequence(List<TextPosition> textPositions, String word) {
        this(textPositions, 0, textPositions.size(), word);
    }

    public TextPositionSequence(List<TextPosition> textPositions, int start, int end, String word) {
        this.textPositions = textPositions;
        this.start = start;
        this.end = end;
        this.word = word;
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public char charAt(int index) {
        TextPosition textPosition = textPositionAt(index);
        String text = textPosition.getUnicode();
        return text.charAt(0);
    }

    @Override
    public TextPositionSequence subSequence(int start, int end) {
        return new TextPositionSequence(textPositions, this.start + start, this.start + end, this.word);
    }
    public TextPositionSequence subSequence(int start, int end, String word) {
        return new TextPositionSequence(textPositions, this.start + start, this.start + end, word);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(length());
        for (int i = 0; i < length(); i++) {
            builder.append(charAt(i));
        }
        return builder.toString();
    }

    public TextPosition textPositionAt(int index) {
        return textPositions.get(start + index);
    }

    public float getX() {
        return textPositions.get(start).getXDirAdj();
    }

    public float getY() {
        // return textPositions.get(start).getYDirAdj();
        return textPositions.get(start).getEndY();
    }

    public float getYtemp() {
        return textPositions.get(start).getY();
    }

    public float getYScale() {
        return textPositions.get(start).getYScale();
    }

    public float getEndY() {
        return textPositions.get(start).getEndY();
    }

    public float getWidth() {
        TextPosition first = textPositions.get(start);
        TextPosition last = textPositions.get(end - 1);
        return last.getWidthDirAdj() + last.getXDirAdj() - first.getXDirAdj();
    }

    public String getWord() {
        return word;
    }

    final List<TextPosition> textPositions;
    final int start, end;
    final String word;
}