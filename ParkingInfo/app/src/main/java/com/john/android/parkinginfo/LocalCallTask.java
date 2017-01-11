package com.john.android.parkinginfo;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jeonghyonkim on 2016. 10. 11..
 */
public class LocalCallTask extends AsyncTask<String,Void, String[]> {


    public final static String TAG="Parking";
    String[] xy= new String[2];


    @Override
    protected String[] doInBackground(String... params) {

        try{

            byte[] str = params[0].getBytes("UTF-8");
            String requestUrl = "http://apis.daum.net/local/geo/addr2coord";
            requestUrl += "?apikey=" + "d3c08e2068fc1d22e2b54cf2516dbfaa"; //발급된 키
            requestUrl += "&q=" + byteArrayToHex(str); //검색어
            requestUrl += "&result=" + "20"; //출력될 결과수
            requestUrl += "&pageno=" + "1"; //페이지 번호
            requestUrl += "&output=" + "xml";
            URL url = new URL(requestUrl);

            Log.d(TAG, "url:" + url.toString());

            //System.out.println(Util.byteArrayToHex(str));
            //API 요청 및 반환
            URLConnection conn = url.openConnection();

            Log.d(TAG, "conn:" +conn.toString());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            Log.d(TAG, "factory:" +factory.toString());

            DocumentBuilder builder = factory.newDocumentBuilder();

            Log.d(TAG, "builder:" +builder.toString());

            InputStream is = conn.getInputStream();

            Log.d(TAG, "InputStream:" +is.toString());

            Document doc = builder.parse(is);

            Log.d(TAG, "doc:" +doc.toString());

            //channel노드를 객체화 하기
            Node node = doc.getElementsByTagName("channel").item(0);
            Log.d(TAG, "Node:" + node.toString());

            for (int i=0 ;i< node.getChildNodes().getLength();i++) {
                Node channelNode = node.getChildNodes().item(i);
                String nodeName = channelNode.getNodeName();

                //item 노드들일 경우
                if ("item".equals(nodeName))
                {
                    //item노드의 자식노드를 검색
                    for (int j=0 ;j< channelNode.getChildNodes().getLength();j++)
                    {
                        Node itemNode = channelNode.getChildNodes().item(j);
                        //title노드 일 경우 출력
                        if("lng".equals(itemNode.getNodeName())){
                            xy[0]= itemNode.getTextContent();
                            Log.d(TAG,itemNode.getTextContent());
                        }else if("lat".equals(itemNode.getNodeName())){
                            xy[1]= itemNode.getTextContent();
                            Log.d(TAG, "["+itemNode.getNodeName()+"]:lat["+itemNode.getTextContent()+"]");
                        }
                    }
                }
            }
        }catch(Exception e) {
            Log.e("LocalCallTask Error",e.toString());
        }
        return xy;
    }

    public static String byteArrayToHex(byte[] ba){
        if(ba == null || ba.length == 0){
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length *2);
        String hexNumber;

        int idx = 1;
        for(int x = 0; x < ba.length; x++){
            //hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
            hexNumber = "0" + Integer.toHexString(0xFF & ba[x]);
            sb.append("%"+hexNumber.substring(hexNumber.length()-2));



            idx++;
        }
        return sb.toString();
    }
}
