package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Service.VoteService;
import com.thoughtworks.rslist.domain.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {
    @Autowired
    VoteService voteService;

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteRsEvent(@PathVariable int rsEventId, @RequestBody Vote vote){
        boolean flag = voteService.voteRsEvent(rsEventId,vote);
        return flag? ResponseEntity.ok().build():ResponseEntity.badRequest().build();
    }
}
