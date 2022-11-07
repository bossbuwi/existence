package com.stargazerstudios.existence.conductor.erratum.file;

import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;

public class FileUploadException extends FileProcessingException  {

    public FileUploadException() {
        super("File upload failed. Please try again or contact an admin.");
    }
}
