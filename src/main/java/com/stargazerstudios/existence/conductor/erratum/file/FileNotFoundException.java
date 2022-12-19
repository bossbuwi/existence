package com.stargazerstudios.existence.conductor.erratum.file;

import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;

public class FileNotFoundException extends FileProcessingException  {

    public FileNotFoundException(String filename) {
        super("File: " + filename + " not found. Please verify that the filename is correct.");
    }
}
