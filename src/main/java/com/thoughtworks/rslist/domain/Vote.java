package com.thoughtworks.rslist.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Vote {
    private int userId;
    private int rsEventId;
    private int voteNum;
    private Date voteTime;

    public Vote(int userId, int rsEventId, int voteNum, Date voteTime){
        this.userId = userId;
        this.rsEventId = rsEventId;
        this.voteNum = voteNum;
        this.voteTime = voteTime;
    }
}
