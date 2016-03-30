package org.codeisland.sonosservice.network.model;

/**
 * Created by lukas on 30.03.16.
 */
public class Status {

    private final String status;

    public Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public boolean isOk(){
        return getStatus().equals("ok");
    }
}
