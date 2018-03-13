package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model;

import lombok.Data;

@Data
public class APIInfo {

    private String name;
    private String version;

    public APIInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

}
