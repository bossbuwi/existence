package com.stargazerstudios.existence.conductor.erratum.file;

import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;

public class FileCreationException extends FileProcessingException  {

    public FileCreationException() {
        super("File creation failed. Please try again after a while or contact an admin.");
    }
}
