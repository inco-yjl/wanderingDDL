package desktop.application.wanderingddl.tools;

import com.alibaba.fastjson2.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportCsvUtil {

    public static <T> String exportCsv(List<JSONObject> list, String filePath) throws IOException, IllegalArgumentException, IllegalAccessException {
        File file = new File(filePath);
        //构建输出流，同时指定编码
        OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(file), "gbk");

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i).toString();
            ow.write(list.get(i).toString());
            //写完一行换行
            ow.write("\r\n");
        }
        ow.flush();
        ow.close();
        return "0";
    }
}