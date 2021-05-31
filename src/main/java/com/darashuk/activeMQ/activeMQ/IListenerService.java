package com.darashuk.activeMQ.activeMQ;


import com.darashuk.activeMQ.entity.Source;

public interface IListenerService {
    void getFileBySourceFromListener(Source sourceFromListener);
}
