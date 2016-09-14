package com.xpple.sheep.bean;


import java.util.List;

public class CreditsHistory extends BaseObject {
    private String type;

    private Integer change;

    private List<CreditsHistory> results;

    public List<CreditsHistory> getElements() {
        return results;
    }
}
