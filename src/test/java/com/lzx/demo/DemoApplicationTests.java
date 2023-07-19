package com.lzx.demo;


import cn.hutool.json.JSONObject;
import com.lzx.demo.dao.PgDao;
import com.lzx.demo.entity.JwtUser;
import com.lzx.demo.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import cn.hutool.core.io.FileUtil;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.trans.Trans;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;


import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests
{


//    public static void main(String[] args) throws Exception
//    {
//        //设置参数
//        String site  = "www.cmaza.top"; //网站URL，需要替换为您自己的URL
//        String token = "5PpxLDsRQz2sSuGM"; //推送接口调用凭据，需要替换为您自己的token
////        String urls = getUrls(); //获取需要推送的URL列表
//        String urls = "http://www.cmaza.top/dm/html/dmcn_article_27_622.html";
//        //构造请求URL
//        String apiUrl = "http://data.zz.baidu.com/urls?site=" + site + "&token=" + token;
//        URL    url    = new URL(apiUrl);
//
//        //发送请求
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "text/plain");
//        connection.setRequestProperty("Content-Length", "83");
//        connection.setDoOutput(true);
//
//        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//        outputStream.writeBytes(urls);
//        outputStream.flush();
//        outputStream.close();
//
//        //处理响应
//        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String         line;
//        while ((line = reader.readLine()) != null)
//        {
//            System.out.println(line);
//        }
//        reader.close();
//    }


    //获取需要推送的URL列表
    @Test
    public void getUrls()
    {
        String        folderPath  = "E:\\demo\\2023-05-09";
        File          folder      = new File(folderPath);
        File[]        files       = folder.listFiles();
        StringBuilder urlsBuilder = new StringBuilder();
        if (files == null) return;
        for (File file : files)
        {
            if (file.getName().endsWith(".html"))
            {
                urlsBuilder.append(file.toURI().toString()).append("\n");
            }
        }
        String repString = folderPath.replace("\\", "/");
        repString = "file:/" + repString;
        System.out.println(urlsBuilder.toString().replace(repString, "http://www.cmaza.top/html"));
//        return null;
    }


    @Test
    public void testKTRExecution()
    {
        try
        {
            // 初始化Kettle环境
            KettleEnvironment.init();

            // 加载KTR文件
            String    ktrFilePath = "E:\\kettleSpace\\project\\tableToTable.ktr";
            TransMeta transMeta   = new TransMeta(ktrFilePath);

            // 创建Transformation对象
            Trans trans = new Trans(transMeta);

            // 执行Transformation
            trans.execute(null);
            trans.waitUntilFinished();

            // 验证执行结果
            assertEquals(0, trans.getErrors(), "Transformation execution failed with errors!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void test_path() throws KettleException
    {
//        可以把文件放到resources目录下面，然后用hutool读取这文件
//        File file = FileUtil.file("33.ktr");
        File   file = FileUtil.file("E:\\kettleSpace\\project\\sjtb.ktr");
        String path = file.getPath();
//        初始化
        KettleEnvironment.init();
//        加载文件
        TransMeta transMeta = new TransMeta(path);
        Trans     trans     = new Trans(transMeta);
//        放入参数，这里其实可以从数据库中取到
//        如果没有参数，可以把这步忽略
//        trans.setParameterValue("stade", "2019-04-24");
        trans.prepareExecution(null);
        trans.startThreads();
//        等待执行完毕
        trans.waitUntilFinished();
    }

    private static final String SECRET_KEY = "ioap_wl_token_key";


    @Test
    public void testIsVerify_ValidToken_ReturnsTrue()
    {
        // Generate a valid token for testing
        String token = generateValidToken();
        System.out.println(token);

        // Verify the token using isVerify method
        boolean result = JwtUtils.isVerify(token);
        System.out.println(result);

        // Assert that the result is true
        assertTrue(result);
    }


    public String generateValidToken()
    {
        // Create a sample JwtUser object
        JwtUser user = new JwtUser("admin", "123456");

        // Generate a valid token using createJWT method
        String token = JwtUtils.createJWT(3600000, user);
        System.out.print(token);
        return token;
    }

    public String generateExpiredToken()
    {
        // Create a sample JwtUser object
        JwtUser user = new JwtUser("john_doe", "password");

        // Generate an expired token by setting a past expiration time
        return JwtUtils.createJWT(3600000, user);
    }

    @Test
    public void a()
    {
        String token = generateValidToken();
//        String token = generateExpiredToken();
//        System.out.print(token);
//        String token  ="eyJhbGciOiJIUzI1NiJ9.eyJqd3RfdXNlcm5hbWUiOiJhZG1pbiIsImp3dF9wYXNzd29yZCI6ImI5ZDExYjNiZTI1ZjVhMWE3ZGM4Y2EwNGNkMzEwYjI4IiwianRpIjoiZGE0MGEzODYtZDA5Yi00ZDAyLWI3YjgtYWZiYTZhYWI2YWM0IiwiaWF0IjoxNjg5MjI5NzY2LCJzdWIiOiJhZG1pbiIsImV4cCI6MTY4OTI2NTc2Nn0.APAY7UwjEQq49m1HLmOU5o4j2k1KsuPiYPMW5OUkHrE";
//          String token  ="eyJhbGciOiJIUzI1NiJ9.eyJqd3RfdXNlcm5hbWUiOiJhZG1pbiIsImp3dF9wYXNzd29yZCI6ImI5ZDExYjNiZTI1ZjVhMWE3ZGM4Y2EwNGNkMzEwYjI4IiwianRpIjoiOWVlY2U2NDktYWI3Ni00ZTAxLWE3YjktNDA2OWQxNjlkYjUzIiwiaWF0IjoxNjg5MjMwMjk4LCJzdWIiOiJhZG1pbiIsImV4cCI6MTY4OTIzMzg5OH0.4rQnvqgzPoAEsAb61lZyt-sZO0dFyu5uXBY2naw8-9w";
//        JwtUtils.isVerify(token);
        String key = JwtUtils.getClaim(token, "jwt_password");

        Claims c   = JwtUtils.parseJWT(token, key);
        System.out.println(c.getExpiration());

        boolean f = c.getExpiration().after(new Date());
    }


    @Test
    public void tk()
    {
        // 假设的 JWT token 字符串
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqd3RfdXNlcm5hbWUiOiJhZG1pbiIsImp3dF9wYXNzd29yZCI6ImI5ZDExYjNiZTI1ZjVhMWE3ZGM4Y2EwNGNkMzEwYjI4IiwianRpIjoiZGE0MGEzODYtZDA5Yi00ZDAyLWI3YjgtYWZiYTZhYWI2YWM0IiwiaWF0IjoxNjg5MjI5NzY2LCJzdWIiOiJhZG1pbiIsImV4cCI6MTY4OTI2NTc2Nn0.APAY7UwjEQq49m1HLmOU5o4j2k1KsuPiYPMW5OUkHrE";

        // 提取 Payload 部分
        String[] jwtParts      = token.split("\\.");
        String   payloadBase64 = jwtParts[1];

        // Base64 解码
        byte[]     payloadBytes = Base64.getUrlDecoder().decode(payloadBase64);
        String     payload      = new String(payloadBytes);
        JSONObject jsonObject   = new JSONObject(payload);
        System.out.println(jsonObject.toString());

        System.out.println(jsonObject.get("jwt_password"));

        // 输出 Payload 数据
        System.out.println("Payload: " + payload);
    }

    @Test
    public void dt()
    {
//                1688520720044 1688524750
        long t    = 1688524750;
        Date date = new Date(t);
        System.out.println(date.after(new Date()));
    }

//    /**
//     * 工具类用法示例
//     *
//     * @param args
//     */
//    public static void main(String[] args) throws Exception
//    {
//        System.out.println(RandomUtil.randomNumbers(15));
//        //安全码safeCode
//        String safeCode = "KYzidfu1x78bmbt04Q9QS8pRZdJQieOZ";
//        //上送json数据，转换成&拼接形式
//        String data      = "appId=APP_211325A1138E4A34B55EE56C476FFA4B&deviceId=DEV_9DF38AC1C52E4AE7AA45D0A31D50FFA4&length=15&signAlgo=HmacSHA256&transId=1234567890123456&version=1";
//        String signature = genSignature(data, safeCode);
//        System.out.println(signature);
//    }

    /**
     * @param data     上送接口的报文数据
     * @param safeCode 安全码，可以在管理端页面的应用管理菜单，(编辑对应的应用信息记录)找到APPID对应的安全码，即该safeCode。集成时若APPID固定，该安全码可以写死
     * @return 计算完成的签名值signature
     * @throws Exception
     */
    public static String genSignature(String data, String safeCode) throws Exception
    {
        String        algo       = "HmacSHA256";
        SecretKeySpec signingKey = new SecretKeySpec(safeCode.getBytes(), algo);
        Mac           mac        = Mac.getInstance(algo);
        mac.init(signingKey);
        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes("UTF-8")));
    }

    public static String generateRandomString(int length)
    {
        String randomUUID = UUID.randomUUID().toString().replace("-", "");
        return randomUUID.substring(0, length);
    }

    public static void main(String[] args)
    {
        System.out.println(Base64.getEncoder().encodeToString("烈烈风中".getBytes())); // 使用Base64编码字符串
    }

    public static String generateJson(Map<String, String> parameters)
    {
        // 使用TreeMap对参数按照键的ASCII码从小到大排序
        Map<String, String> sortedMap = new TreeMap<>(parameters);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet())
        {
            String key   = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isEmpty())
            {
                if (sb.length() > 0)
                {
                    sb.append("&");
                }
                sb.append(key).append("=").append(value);
            }
        }

        return sb.toString();
    }

    @Autowired
    PgDao pgDao;
    @Test
    public void testPg(){
        List<Object> list = pgDao.getList();
        for (Object o : list)
        {
            System.out.println(o.toString());
        }
    }


}


