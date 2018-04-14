package ch.fhnw.edu.wodss.tippspielapi.controller;

import lombok.Data;

@Data
public class APIInfo {

    private String name;
    private String version;

    APIInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

}
