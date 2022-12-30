package com.stargazerstudios.existence.conductor.erratum.system;

public class SearchNotImplementedException extends FeatureNotImplementedException {
    public SearchNotImplementedException() {
        super("Generic text search has not been fully implemented yet. Please use filters for the meantime. Thank you.");
    }
}
