package com.stargazerstudios.existence.conductor.utils;

import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import org.springframework.stereotype.Service;

@Service
public class StringUtil {

    public String checkInput(String in) {
        if (in != null && !in.isBlank()) {
            return in;
        } else {
            return EnumUtilOutput.EMPTY.getValue();
        }
    }

    public String checkInputTrim(String in) {
        if (in != null && !in.isBlank()) {
            String out = in.trim();
            if (out.length() == 0) return EnumUtilOutput.EMPTY.getValue();
            return out;
        } else {
            return EnumUtilOutput.EMPTY.getValue();
        }
    }

    public String checkInputTrimToUpper(String in) {
        if (in != null && !in.isBlank()) {
            String out = in.trim();
            if (out.length() == 0) return EnumUtilOutput.EMPTY.getValue();
            return out.toUpperCase();
        } else {
            return EnumUtilOutput.EMPTY.getValue();
        }
    }
}
