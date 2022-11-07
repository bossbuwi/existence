package com.stargazerstudios.existence.conductor.erratum.file;

import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;

public class RecordRestoreException extends FileProcessingException  {

    public RecordRestoreException() {
        super("Restoration of records failed. Please try again or contact an admin.");
    }
}
