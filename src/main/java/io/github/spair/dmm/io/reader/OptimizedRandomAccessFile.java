/*
 * The MIT License
 *
 * Copyright 2013 Joos Kiener <Joos.Kiener@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.spair.dmm.io.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

class OptimizedRandomAccessFile implements AutoCloseable {

    private static final int BUFFER_SIZE = 8192;
    private RandomAccessFile raf;
    private Long actualFilePointer;
    private char[] charBuffer;
    private int nChars, nextChar;
    private int bufferSize;
    private long lastOffset;
    private boolean skipLF;

    OptimizedRandomAccessFile(final File file, final String mode) throws FileNotFoundException {
        this.raf = new RandomAccessFile(file, mode);
        actualFilePointer = null;
        this.bufferSize = BUFFER_SIZE;
        charBuffer = new char[bufferSize];
    }

    /**
     * Returns the current offset in this file.
     *
     * @return the offset from the beginning of the file, in bytes, at which the
     * next read or write occurs.
     * @throws IOException if an I/O error occurs.
     */
    synchronized long getFilePointer() throws IOException {
        if (actualFilePointer == null) {
            return raf.getFilePointer();
        } else {
            return this.actualFilePointer;
        }
    }

    /**
     * Sets the file-pointer offset, measured from the beginning of this file,
     * at which the next read or write occurs. The offset may be set beyond the
     * end of the file. Setting the offset beyond the end of the file does not
     * change the file length. The file length will change only by writing after
     * the offset has been set beyond the end of the file.
     *
     * @param pos the offset position, measured in bytes from the beginning of
     *            the file, at which to set the file pointer.
     * @throws IOException if <code>pos</code> is less than <code>0</code> or
     *                     if an I/O error occurs.
     */
    synchronized void seek(final long pos) throws IOException {
        actualFilePointer = null;
        resetPosition();
        raf.seek(pos);
    }

    /**
     * Closes this random access file stream and releases any system resources
     * associated with the stream. A closed random access file cannot perform
     * input or output operations and cannot be reopened.
     *
     * <p> If this file has an associated channel then the channel is closed as
     * well.
     *
     * @throws IOException if an I/O error occurs.
     * @revised 1.4
     * @spec JSR-51
     */
    public void close() throws IOException {
        raf.close();
    }

    /**
     * <p> Read the file line by line omitting the line separator. </p> <p> see
     * {@link java.io.RandomAccessFile#readLine() readLine()} and see
     * {@link java.io.BufferedReader#readLine(boolean) readLine(boolean ignoreLF)}.
     * <p>
     *
     * <p> Subsequent calls of this method are buffered. If certain other
     * methods that are affected by the current position of the reader in the
     * file is called after this method, the position is set to the start of the
     * next line and the buffer is invalidated. </p>
     *
     * <p> This method is copied from
     * {@link java.io.BufferedReader BufferedReader} with minor changes like
     * tracking position (offset) were next line starts. </p>
     *
     * @return the next line of text from this file, or null if end of file is
     * encountered before even one byte is read.
     * @throws IOException if an I/O error occurs.
     */
    private synchronized String readLine(final boolean ignoreLF) throws IOException {
        StringBuilder s = null;
        int startChar;
        int separatorIndex = 0;

        boolean omitLF = ignoreLF || skipLF;

        while (true) {
            if (nextChar >= nChars) {
                fill();
            }
            if (nextChar >= nChars) { /* EOF */
                if (s != null && s.length() > 0) {
                    //EOF -> hence no need to adjust position in file
                    // changed by fill()
                    return s.toString();
                } else {
                    return null;
                }
            }
            boolean eol = false;
            char c = 0;
            int i;

            /* Skip a leftover '\n', if necessary */
            if (omitLF && (charBuffer[nextChar] == '\n')) {
                nextChar++;
            }
            skipLF = false;
            omitLF = false;

            for (i = nextChar; i < nChars; i++) {
                c = charBuffer[i];
                if ((c == '\n') || (c == '\r')) {
                    eol = true;
                    break;
                }
            }

            startChar = nextChar;
            nextChar = i;

            if (eol) {
                String str;
                if (s == null) {
                    str = new String(charBuffer, startChar, i - startChar);
                } else {
                    s.append(charBuffer, startChar, i - startChar);
                    str = s.toString();
                }
                nextChar++;
                if (c == '\r') {
                    skipLF = true;
                    if (nextChar >= nChars) {
                        fill();
                    }
                    if (charBuffer[nextChar] == '\n') {
                        separatorIndex = 1;
                    }
                }
                actualFilePointer = lastOffset + nextChar + separatorIndex;
                return str;
            }

            if (s == null) {
                int defaultExpectedLineLength = 80;
                s = new StringBuilder(defaultExpectedLineLength);
            }
            s.append(charBuffer, startChar, i - startChar);
        }
    }

    /**
     * see {@link #readLine(boolean) readLine(boolean ignoreLF)}
     *
     * @return
     * @throws IOException
     */
    synchronized String readLine() throws IOException {
        return readLine(false);
    }

    private void fill() throws IOException {

        lastOffset = raf.getFilePointer();
        actualFilePointer = lastOffset;
        byte[] buffer = new byte[bufferSize];
        int n = raf.read(buffer);
        if (n > 0) {
            nChars = n;
            nextChar = 0;
        }
        for (int i = 0; i < buffer.length; i++) {
            charBuffer[i] = (char) buffer[i];
        }
    }

    private void resetPosition() throws IOException {
        if (actualFilePointer != null) {
            raf.seek(actualFilePointer);
            actualFilePointer = null;
        }
        nChars = 0;
        nextChar = 0;
    }
}
