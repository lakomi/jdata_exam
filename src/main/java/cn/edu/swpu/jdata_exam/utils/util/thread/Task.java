package cn.edu.swpu.jdata_exam.utils.util.thread;


import cn.edu.swpu.jdata_exam.utils.util.DateTimeUtil;
import cn.edu.swpu.jdata_exam.utils.util.RemoteExecute;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Slf4j
public class Task extends TimerTask{
    @Override
    public void run() {
        try {
            log.info("启动定时任务..");

            RemoteExecute.mkdir(DateTimeUtil.getNowDir());
        }catch (Exception e){
            log.error("定时任务异常..");
        }
        System.out.println("定时任务...");
    }

}
