package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class RsController {

  public List<RsEvent> rsList = initRsEvent();

  private List<RsEvent> initRsEvent() {
    List<RsEvent> result = new ArrayList<>();
    result.add(new RsEvent("第一条事件","无分类"));
    result.add(new RsEvent("第二条事件","无分类"));
    result.add(new RsEvent("第三条事件","无分类"));
    return result;
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end){

    if (start == null||end == null){
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @GetMapping("rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index){
    return rsList.get(index-1);
  }

  @PostMapping("/rs/event")
  public void addOneRsEvent(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
  }

  @PostMapping("/rs/delete/{index}")
  public void deleteOneRsEvent(@PathVariable int index){
    rsList.remove(index-1);
  }

  @PostMapping("/rs/modify/{index}")
  public void modifyOneRsEvent(@PathVariable int index,@RequestBody RsEvent rsEvent){
    RsEvent rs = rsList.get(index-1);
    if (rsEvent.getEventName()!=null){
      rs.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord()!=null){
      rs.setKeyWord(rsEvent.getKeyWord());
    }
  }
}
