package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
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


@RestController
public class RsController {
  @Autowired
  RsEventRepository rsEventRepository;

  @Autowired
  UserRepository userRepository;


  public List<RsEvent> rsList = initRsEvent();

  private List<RsEvent> initRsEvent() {
    List<RsEvent> result = new ArrayList<>();
//    User user1 = new User("Mike", "male",18,"805560811@qq.com","13667899265");
//    User user2 = new User("Darcy", "female",25,"125560811@qq.com","15887899265");
//    User user3 = new User("John", "male",30,"655560811@qq.com","16787899265");
//    result.add(new RsEvent("第一条事件","无分类",user1));
//    result.add(new RsEvent("第二条事件","无分类",user2));
//    result.add(new RsEvent("第三条事件","无分类",user3));
    return result;
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end){

    if (start == null||end == null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index) throws InvalidIndexException {
    if (index >rsList.size()){
      throw  new InvalidIndexException("invalid index");
    }
     return ResponseEntity.ok(rsList.get(index-1));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addOneRsEvent(@RequestBody @Validated RsEvent rsEvent){
//    if (!UserController.userList.contains(rsEvent.getUser()) ){
//      UserController.userList.add(rsEvent.getUser());
//    }
//    rsList.add(rsEvent);
//    return ResponseEntity.created(null).header("index",rsList.size()+1+"").build();
//    if (!userRepository.existsById(Integer.valueOf(rsEvent.getUserId()))){
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    }
    RsEventEntity rsEventEntity = RsEventEntity.builder().eventName(rsEvent.getEventName())
            .keyWord(rsEvent.getKeyWord()).userId(rsEvent.getUserId()).build();
    rsEventRepository.save(rsEventEntity);
    return ResponseEntity.created(null).build();
  }

  @PostMapping("/rs/delete/{index}")
  public ResponseEntity deleteOneRsEvent(@PathVariable int index){
    rsList.remove(index-1);
    return ResponseEntity.created(null).header("index",index+"").build();
  }

  @PostMapping("/rs/modify/{index}")
  public ResponseEntity modifyOneRsEvent(@PathVariable int index,@RequestBody RsEvent rsEvent){
    RsEvent rs = rsList.get(index-1);
    if (rsEvent.getEventName()!=null){
      rs.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord()!=null){
      rs.setKeyWord(rsEvent.getKeyWord());
    }
    return ResponseEntity.created(null).header("index",index+"").build();
  }
}
