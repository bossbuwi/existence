package com.stargazerstudios.existence.conductor.utils;

import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import org.springframework.stereotype.Service;

@Service
public class StringUtil {

    public String checkInput(String in) {
        if (in != null && !in.isBlank()) {
            return in;
        } else {
            return EnumUtilOutput.EMPTY.toString();
        }
    }

    public String checkInputTrimToUpper(String in) {
        if (in != null && !in.isBlank()) {
            String out = in.trim();
            if (out.length() == 0) return EnumUtilOutput.EMPTY.toString();
            return out.toUpperCase();
        } else {
            return EnumUtilOutput.EMPTY.toString();
        }
    }
}
