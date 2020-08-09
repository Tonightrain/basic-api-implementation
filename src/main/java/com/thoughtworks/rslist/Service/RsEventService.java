package com.thoughtworks.rslist.Service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.jboss.jandex.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;


@Service
public class RsEventService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    public List<RsEvent> getRsEventBetween(Integer page,Integer size){
        List<RsEventEntity> rsEventEntityList;
        if (page!=null && size !=null && page>0 && size>0){
            Pageable pageable = (Pageable) PageRequest.of(page-1, size);
            rsEventEntityList = rsEventRepository.findAll(pageable).getContent();
        }else {
            rsEventEntityList = rsEventRepository.findAll();
        }
        return rsEventEntityList.stream().map(
                item -> RsEvent.builder().eventName(item.getEventName()).keyWord(item.getKeyWord())
                        .userId(String.valueOf(item.getUserId())).build()).collect(Collectors.toList());
    }

    public RsEvent getOneRsEvent(int index) throws InvalidIndexException {
        Optional<RsEventEntity> rsEventEntity = rsEventRepository.findById(index);
        if (!rsEventEntity.isPresent()){
            throw new InvalidIndexException("invalid index");
        }
        RsEvent rsEvent = RsEvent.builder()
                .eventName(rsEventEntity.get().getEventName())
                .keyWord(rsEventEntity.get().getKeyWord())
                .userId(String.valueOf(rsEventEntity.get().getUserId())).build();
        return rsEvent;
    }

    public boolean addOneRsEVent(RsEvent rsEvent){
        if (!userRepository.existsById(Integer.valueOf(rsEvent.getUserId()))){
            return false;
        }
        //UserEntity userEntity = userRepository.findById(Integer.valueOf(rsEvent.getUserId())).get();
        RsEventEntity rsEventEntity = RsEventEntity.builder().eventName(rsEvent.getEventName())
                .keyWord(rsEvent.getKeyWord()).userId(rsEvent.getUserId()).build();
        rsEventRepository.save(rsEventEntity);
        return true;
    }

    public void deleteOneRsEvent(int index) throws InvalidIndexException {
        if (!rsEventRepository.existsById(index)){
            throw new InvalidIndexException("invalid index");
        }
        rsEventRepository.deleteById(index);
    }

    public boolean modifyOneRsEvent(int index,RsEvent rsEvent){
        if (!rsEventRepository.existsById(index)){
            return false;
        }
        RsEventEntity rsEventEntity = rsEventRepository.findById(index).get();
        if (!rsEvent.getUserId().equals(String.valueOf(rsEventEntity.getUserId()))) {
            return false;
        }
        if (rsEvent.getEventName()!=null){
            rsEventEntity.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord()!=null){
            rsEventEntity.setKeyWord(rsEvent.getKeyWord());
        }
        rsEventRepository.save(rsEventEntity);
        return true;
    }

}
