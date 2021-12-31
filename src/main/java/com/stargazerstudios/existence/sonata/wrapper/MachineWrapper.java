package com.stargazerstudios.existence.sonata.wrapper;

import lombok.*;

import java.util.HashMap;

@Getter @Setter @NoArgsConstructor
public class MachineWrapper {
    private HashMap<String, String> machine;
    private long id;
    private String name;
}
