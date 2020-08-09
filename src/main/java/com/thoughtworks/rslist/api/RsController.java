package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Service.RsEventService;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class RsController {
  @Autowired
  RsEventRepository rsEventRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RsEventService rsEventService;


  public List<RsEvent> rsList = initRsEvent();

  private List<RsEvent> initRsEvent() {
    List<RsEvent> result = new ArrayList<>();
    return result;
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end){

    return ResponseEntity.ok(rsEventService.getRsEventBetween(start, end));
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index) throws InvalidIndexException {
     return ResponseEntity.ok(rsEventService.getOneRsEvent(index));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addOneRsEvent(@RequestBody @Validated RsEvent rsEvent){
    boolean flag = rsEventService.addOneRsEVent(rsEvent);
    return flag?ResponseEntity.created(null).build() : ResponseEntity.badRequest().build();
  }

  @PostMapping("/rs/delete/{index}")
  public ResponseEntity deleteOneRsEvent(@PathVariable int index) throws InvalidIndexException {
    rsEventService.deleteOneRsEvent(index);
    return ResponseEntity.created(null).header("index",index+"").build();
  }

  @PostMapping("/rs/modify/{index}")
  public ResponseEntity modifyOneRsEvent(@PathVariable int index,@RequestBody RsEvent rsEvent){
    boolean flag = rsEventService.modifyOneRsEvent(index,rsEvent);
    return flag?ResponseEntity.created(null).header("index",index+"").build() : ResponseEntity.badRequest().build();
  }
}
