package cn.edu.swpu.jdata_exam.utils.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class SshUtil {
    public static String ip = "118.24.93.59";
    public static String uploadPath = "/home/ubuntu/qiu/testFile/";
    public static String username = "ubuntu";
    public static String password = "qiu980717";
    public static boolean putFile(String localFilePath) throws IOException {
        Connection conn = new Connection(ip);//目标服务器地址
        conn.connect();
        boolean isAuthenticated = conn.authenticateWithPassword(username, password);
        if (isAuthenticated == false){
            throw new IOException("Authentication failed.");
        }

        SCPClient client = new SCPClient(conn);
//        client.get("目标服务器文件路径", "本服务器用来存放文件路径");//get方法用来将目标服务器的文件下载到本地服务器
        client.put(localFilePath,uploadPath+DateTimeUtil.convertDateToString(new Date()));//put方法用来将本地文件上传到目标服务器

        log.info("文件完成转发");

        ch.ethz.ssh2.Session session = conn.openSession();
        conn.close();
        session.close();
        return true;
    }


}
