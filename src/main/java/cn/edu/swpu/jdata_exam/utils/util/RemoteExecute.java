package cn.edu.swpu.jdata_exam.utils.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.springframework.util.StringUtils;

import java.io.*;

//用于操作远程服务器创建文件夹
public class RemoteExecute {
    //字符编码默认是utf-8
    private static String DEFAULTCHART = "UTF-8";
    private static Connection conn;

    //连接远程服务器主机
    public Boolean login() {

        boolean flg = false;
        try {
            conn = new Connection(SshUtil.ip,22);
            conn.connect();//连接
            flg = conn.authenticateWithPassword(SshUtil.username, SshUtil.password);//认证
            if (flg) {
                System.out.println("服务器认证成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flg;
    }

    //执行命令
    public String execute(String cmd) {
        String result = "";
        try {
            if (login()) {

                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);

                //如果得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isEmpty(result)) {
                    System.out.println("无返回值");
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    //创建文件夹
    public static String mkdir(String dirName){
        RemoteExecute remoteExecute = new RemoteExecute();
        String result = remoteExecute.execute("mkdir "+SshUtil.uploadPath+dirName);
        conn.close();
        return result;
    }

//    public static void main(String[] args) {
//
//        RemoteExecute rec = new RemoteExecute();
//
//        try {
//            System.out.println("=====第一个步骤=====");
//            String result = rec.execute("mkdir /home/ubuntu/clf/testFile");
//            System.out.println(result);
//
//            /*System.out.println("=====第二个步骤=====");
//            String result2 = rec.execute("java -version");
//            System.out.println(result2);*/
//
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
