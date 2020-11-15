package edu.byu.cs.tweeter.model.net;

import java.util.List;

public class TweeterRemoteException extends Exception {

    private String remoteExceptionType;
    private List<String> remoteStakeTrace;

    protected TweeterRemoteException(String message, String remoteExceptionType, List<String> remoteStakeTrace) {
        super(message);
        this.remoteExceptionType = remoteExceptionType;
        this.remoteStakeTrace = remoteStakeTrace;
    }

    public String getRemoteExceptionType() {
        return remoteExceptionType;
    }

    public List<String> getRemoteStackTrace() {
        return remoteStakeTrace;
    }

    public void setRemoteExceptionType(String remoteExceptionType) {
        this.remoteExceptionType = remoteExceptionType;
    }

    public void setRemoteStakeTrace(List<String> remoteStakeTrace) {
        this.remoteStakeTrace = remoteStakeTrace;
    }
}
