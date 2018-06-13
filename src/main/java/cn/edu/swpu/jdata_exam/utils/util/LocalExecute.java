package cn.edu.swpu.jdata_exam.utils.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//用于操作本地文件,临时保存文件,上传76服务器后删除临时文件
public class LocalExecute {
    public static String doCmd(String commmand){
        try {

            String[] cmd = new String[]{"/bin/sh", "-c", commmand};

            Process ps = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));

            StringBuffer sb = new StringBuffer();

            String line;

            while ((line = br.readLine()) != null) {

                sb.append(line).append("\n");

            }

            String result = sb.toString();

            return result;

        } catch (Exception e) {

            e.printStackTrace();
            return e.toString();
        }
    }
    //删除本地临时保存的文件
    public static boolean removeFile(String localFilePath){
        doCmd("rm -f "+localFilePath);
        return true;
    }

}
