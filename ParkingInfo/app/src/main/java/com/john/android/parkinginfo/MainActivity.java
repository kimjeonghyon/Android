package com.john.android.parkinginfo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Document;

public class MainActivity extends AppCompatActivity {


    public String html;
    public String[] AString ={"list_total_count","PARKMASTER_CD","PARK_NAME","MAX_PARKING_CNT",
            "PARKING_CNT","PARK_ADDRESS","TEL_NO","OWNER_NAME","COMPANY_NM"};

    public DataInfo[] datainfo;


    public int datacnt;
    public int strcnt;

    public static boolean refreshflag =false; /* 재조회인 경우는 일단 속도 개선을 위해 위도,경도를 그대로 둔다 */

    public final static String TAG="Parking";

    public TextView[] headerView;

    public TextView[][] tv;

    @Override
    protected void onStart() {
        super.onStart();
        Callhtml();

        appendHeader();
        appendRow(datainfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Callhtml();
//
//        appendHeader();
//        appendRow(datainfo);

    }

    public void appendRow(DataInfo[] datainfo){
        tv = new TextView[datacnt][5];
        TableLayout tb2 = (TableLayout)findViewById(R.id.table2);
        ScrollView sv = (ScrollView)findViewById(R.id.scrollView1);
        ImageButton btn1 = new ImageButton(this);
        sv.removeAllViews();
        tb2.removeAllViewsInLayout();
        for(int i=1;i<datacnt+1;i++){
            final TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT
            ));
            for(int j =0; j < 5; j++){
                tv[i-1][j] = new TextView(this);

                switch(j){
                    case 0:

                        String v0 = datainfo[i-1].getPark_name().replaceAll("주차장", "");
                        tv[i-1][j].setText(v0);
                        break;
                    case 1:
                        String v1 = Integer.toString(datainfo[i-1].getMax_parking_cnt());
                        tv[i-1][j].setText(v1);
                        tv[i-1][j].setGravity(Gravity.RIGHT);
                        break;
                    case 2:
                        String v2 = Integer.toString(datainfo[i-1].getParking_cnt());
                        tv[i-1][j].setText(v2);
                        tv[i-1][j].setGravity(Gravity.RIGHT);
                        break;
                    case 3:
                        String v3 = datainfo[i-1].getPark_address().replaceFirst("서울시", "");
                        tv[i-1][j].setText(v3);

                        break;
                    case 4:
                        String v4 = datainfo[i-1].getTel_no().replaceFirst("02-", "");
                        tv[i-1][j].setText(v4);
                        break;
                }
                tv[i-1][j].setTextSize(16);
                tv[i-1][j].setGravity(Gravity.TOP | Gravity.LEFT);
                tv[i-1][j].setBackgroundResource(R.drawable.border);
                tv[i-1][j].setShadowLayer(1.0f, 1, 1, Color.parseColor("#AFFFFFFF"));
                tv[i-1][j].setTextAppearance(this, R.style.BodyRow);
                if(j==3)
                    tr.addView(tv[i-1][j], new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT,1));
                else
                    tr.addView(tv[i-1][j], new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT,0));
            }

            tr.setId(i);
            tr.setBackgroundColor(Color.parseColor("#DFDFDF"));
//            /*** table row 클릭시 처리하게 되는 Listener 등록 */
//            tr.setOnClickListener(ClickListener);
            tb2.addView(tr,new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));
        }
        sv.addView(tb2);
    }

    /***
     * 서울시 공공 API call해서 datainfo배열에 저장
     */
    public void Callhtml(){
        html = DownloadHtml("http://openapi.seoul.go.kr:8088/41487565477a6170373946545a6279/xml/PublicParkingAvaliable/1/100");

        datainfo = getDataInfo(html, AString);
        datacnt = datainfo.length;
        strcnt = AString.length;
    }

    static public DataInfo[] getDataInfo(String str ,String[] xmlname){
        DataInfo[] datainfo = null ;
        String[] xy = new String[2];
        try {
            Document document = XmlUtil.loadXmlString(str);
            String cnttext= XmlUtil.getTagValue(document,xmlname[0]);

            int cnt = Integer.parseInt(cnttext);

            datainfo = new DataInfo[cnt];
            int len = xmlname.length;
            Log.d(TAG, "cnt:["+ cnt + "],xmlname.length["+xmlname.length+"]");
            for(int i=0;i<cnt;i++){
                datainfo[i] = new DataInfo();
                String tmp;
                for(int j =1; j <len;j++){
                    tmp =XmlUtil.getTagValue(document,xmlname[j],i);
                    Log.d(TAG, "getTagValue ["+i+"]["+j+"] :" + tmp );

                    switch(j){
                        case 1:
                            datainfo[i].setParkmaster_cd(tmp);
                            break;
                        case 2:
                            datainfo[i].setPark_name(tmp);
                            break;
                        case 3:
                            datainfo[i].setMax_parking_cnt(Integer.parseInt(tmp));
                            //Log.v(TAG,"Max:["+tmp +"]");
                            break;
                        case 4:
                            datainfo[i].setParking_cnt(Integer.parseInt(tmp));
                            //Log.d(TAG,"Cnt:["+tmp +"]");
                            break;
                        case 5:
                            datainfo[i].setPark_address(tmp);
                            if(MainActivity.refreshflag == false){
                                xy = localCall(tmp);
                                if(xy == null){
                                    datainfo[i].setLng("0");
                                    datainfo[i].setLat("0");
                                }
                                datainfo[i].setLng(xy[0]);
                                datainfo[i].setLat(xy[1]);
                            }
                            //Log.d(TAG,"[0]addr:["+tmp +"],x:["+ xy[0] + "],y:["+ xy[1] +"]");
                            //Log.d(TAG,"[1]addr:["+tmp +"],x:["+ datainfo[i].GetLng() + "],y:["+ datainfo[i].GetLat()+"]");
                            break;
                        case 6:
                            datainfo[i].setTel_no(tmp);
                            break;
                        case 7:
                            datainfo[i].setOwner_name(tmp);
                            break;
                        case 8:
                            datainfo[i].setCompany_nm(tmp);
                            break;
                    }
                }
            }

        }
        catch (Exception e) {
            Log.e(TAG,"getDataInfo ex=["+e+"]");
        }

        return datainfo;
    }

    static public String[] localCall(String addr) {
        AsyncTask<String, Void, String[]> htmlTask = new LocalCallTask().execute(addr);
        String [] str = {"",""};
        try {
            str = new String[2];
            str = htmlTask.get();
            if(str == null) return null;
                //Log.d(TAG, "str0:["+ str[0] + "],"+"str1:["+ str[1] + "]");

        } catch (Exception e) {

        }

        return str;
    }

    public String DownloadHtml(String addr) {
        AsyncTask<String, Void, String> htmlTask = new DownloadHtmlTask().execute(addr);
        String str = "";
        try {
            str = htmlTask.get();
//            Log.d(TAG, "str:["+ str + "]");
            if(str == null) return null;

        } catch (Exception e) {
            return null;
        }

        return str;
    }


    /*
	 * 동적 tablelayout 작성 중
	 * header부분 설정
	 */
    public void appendHeader(){
        TableLayout tb1 = (TableLayout)findViewById(R.id.table1);
        tb1.removeAllViewsInLayout();

        headerView = new TextView[5];
        /*** 설명 헤더 추가 table row 설정 **/

        TableRow trh = new TableRow(this);
        trh.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        for(int j =0; j < 5; j++){
            headerView[j]= new TextView(this);
            switch(j){
                case 0:
                    headerView[j].setText("주차장명");
                    break;
                case 1:
                    headerView[j].setText("최대수");
                    break;
                case 2:
                    headerView[j].setText("잔여수");
                    break;
                case 3:
                    headerView[j].setText("주소");
                    break;
                case 4:
                    headerView[j].setText("전화번호");
                    break;
            }
            headerView[j].setTextSize(16);
            headerView[j].setGravity(Gravity.TOP | Gravity.LEFT);
            headerView[j].setBackgroundResource(R.drawable.border);
            headerView[j].setShadowLayer(1.0f, 1, 1, Color.parseColor("#AFFFFFFF"));
            headerView[j].setTextAppearance(this,R.style.HeaderRow);
            if(j ==3)
                trh.addView(headerView[j], new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT,1));
            else
                trh.addView(headerView[j], new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT,0));
        }
        tb1.addView(trh,new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
    }
}
