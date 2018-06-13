package cn.edu.swpu.jdata_exam;

import cn.edu.swpu.jdata_exam.utils.util.thread.TimerManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdataExamApplication {

    public static void main(String[] args) {
        TimerManager.startManager();
        SpringApplication.run(JdataExamApplication.class, args);
    }
}
