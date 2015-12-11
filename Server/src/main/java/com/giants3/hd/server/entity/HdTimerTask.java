package com.giants3.hd.server.entity;

import com.giants3.hd.utils.entity.HdTask;

import java.util.TimerTask;

/**
 * Created by david on 2015/12/10.
 */
public class HdTimerTask extends TimerTask{

    public   HdTask hdTask;
    public HdTimerTask(HdTask hdTask)
    {
       this.hdTask=hdTask;
    }

    @Override
    public void run() {

    }
}
