package edu.byu.cs.tweeter.model.service.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class StatusResponse extends PagedResponse {

    private List<Status> statuses;

    public StatusResponse(String message) {
        super(false, message, false);
    }

    public StatusResponse(List<Status> statuses, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statuses = statuses;
    }

    public List<Status> getStatuses() {
        return statuses;
    }
}
