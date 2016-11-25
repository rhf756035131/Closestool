package com.example.rahul.closestool;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by rahul on 15-12-28.
 */
public class Closestool_info  extends ActionBarActivity {
    /* 一些常量，代表服务器的名称 */
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";

    public static final  byte cmd_device_ack=0x01;
    public static final  byte cmd_syn_time=0x02;
    public static final  byte cmd_read_time=0x03;
    public static final  byte cmd_read_single_date=0x04;
    public static final  byte cmd_read_all_date=0x05;
    public static final  byte cmd_del_date=0x06;
    public static final  byte cmd_error=0x07;
    public static final  byte cmd_close_bt=0x09;
    public static final  byte cmd_rename_bt=0x0a;
    public static final  byte cmd_auto_test=0x0B;
   // public static final  byte cmd_auto_test=0x0B;
    public static final  byte cmd_collect_finger=0x20;
    public static final  byte cmd_regist_finger=0x40;
    public static final  byte cmd_del_finger=0x44;


    public static final int S_TV_GLU_R = 9;
    //public static final  byte[] cmd_device_ack = new byte[]{(byte)0x93,(byte)0x8e,0x08,0x00,0x08,0x01,0x43,0x4f,0x4e,0x54,0x45};
    //public static final  byte[] cmd_read_date = new byte[]{(byte)0x93,(byte)0x8e,0x04,0x00,0x08,0x04,0x10};
    Context mContext;

    private BluetoothServerSocket mserverSocket = null;
    private ServerThread startServerThread = null;
    private clientThread clientConnectThread = null;
    private BluetoothSocket socket = null;
    private BluetoothDevice device = null;
    private readThread mreadThread = null;;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private TextView TV_GLU_R;
    private TextView TV_BIL_R;
    private TextView TV_PH_R;
    private TextView TV_SG_R;
    private TextView TV_KET_R;
    private TextView TV_BLD_R;
    private TextView TV_RPO_R;
    private TextView TV_URO_R;
    private TextView TV_NIT_R;
    private TextView TV_LEU_R;
    private TextView TV_VC_R;
    private TextView TV_Prompt;
    private Button B_FINGER, B_DELFINGER;

    private byte[] revice_date=new byte[1024];
    private int revice_date_length=0;
    private byte command=0x00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.closesetool_info);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
//        LinearLayout layout=(LinearLayout)findViewById(R.id.Normal_Result);
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setClass(Closestool_info.this,Check_nomal_Result.class);
//                startActivity(intent);
//            }
//        });
        TV_GLU_R=(TextView)findViewById(R.id.TV_GLU_R);
        TV_BIL_R=(TextView)findViewById(R.id.TV_BIL_R);
        TV_SG_R =(TextView)findViewById(R.id.TV_SG_R);
        TV_PH_R =(TextView)findViewById(R.id.TV_PH_R);
        TV_KET_R=(TextView)findViewById(R.id.TV_KET_R);
        TV_BLD_R=(TextView)findViewById(R.id.TV_BLD_R);
        TV_RPO_R=(TextView)findViewById(R.id.TV_RPO_R);
        TV_URO_R=(TextView)findViewById(R.id.TV_URO_R);
        TV_NIT_R=(TextView)findViewById(R.id.TV_NIT_R);
        TV_LEU_R=(TextView)findViewById(R.id.TV_LEU_R);
        TV_VC_R =(TextView)findViewById(R.id.TV_VC_R);
        B_FINGER =(Button)findViewById(R.id.B_FINGER);
        B_DELFINGER =(Button)findViewById(R.id.B_DELFINGER);
        B_FINGER.setOnClickListener(new BClickListener());
        B_DELFINGER.setOnClickListener(new BClickListener());

        TV_Prompt=(TextView)findViewById(R.id.TV_Prompt);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            setCommand(cmd_read_single_date);
            sendCommand(cmd_read_single_date);
            ShowSpinnerDialog();
            return true;
        }else if (id == R.id.action_read_time) {
            setCommand(cmd_read_time);
            sendCommand(cmd_read_time);
            ShowSpinnerDialog();
            return true;
        }
        if(id== android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {

        BluetoothMsg.serviceOrCilent=BluetoothMsg.ServerOrCilent.CILENT;
        super.onResume();
        if(BluetoothMsg.isOpen)
        {
            Toast.makeText(mContext, "连接已经打开，可以通信。如果要再建立连接，请先断开！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.CILENT)
        {
            String address = BluetoothMsg.BlueToothAddress;
            if(!address.equals("null"))
            {
                device = mBluetoothAdapter.getRemoteDevice(address);
                clientConnectThread = new clientThread();
                clientConnectThread.start();
                BluetoothMsg.isOpen = true;
            }
            else
            {
                Toast.makeText(mContext, "address is null !", Toast.LENGTH_SHORT).show();
            }
        }
        else if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.SERVICE)
        {
            startServerThread = new ServerThread();
            startServerThread.start();
            BluetoothMsg.isOpen = true;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.CILENT)
        {
            shutdownClient();
        }
        else if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.SERVICE)
        {
            shutdownServer();
        }
        BluetoothMsg.isOpen = false;
        BluetoothMsg.serviceOrCilent = BluetoothMsg.ServerOrCilent.NONE;
    }
    //开启客户端
    private class clientThread extends Thread {
        @Override
        public void run() {
            try {
                //创建一个Socket连接：只需要服务器在注册时的UUID号
                // socket = device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                //连接
                Message msg2 = new Message();
                msg2.obj = "请稍候，正在连接马桶:"+BluetoothMsg.BlueToothAddress;
                msg2.what = 0;
                LinkDetectedHandler.sendMessage(msg2);

                socket.connect();

                Message msg = new Message();
                msg.obj = "已经连接上马桶！可以发送命令。";
                msg.what = 0;
                LinkDetectedHandler.sendMessage(msg);
                //启动接受数据
                mreadThread = new readThread();
                mreadThread.start();
               // sendCommand(cmd_device_ack);
               // sendMessageHandle(cmd_read_date);
            }
            catch (IOException e)
            {
                Log.e("connect", "", e);
                Message msg = new Message();
                msg.obj = "连接马桶异常！断开连接重新试一试。";
                msg.what = 0;
                LinkDetectedHandler.sendMessage(msg);
            }
        }
    };

    //开启服务器
    private class ServerThread extends Thread {
        @Override
        public void run() {

            try {
                    /* 创建一个蓝牙服务器
                     * 参数分别：服务器名称、UUID   */
                mserverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                Log.d("server", "wait cilent connect...");

                Message msg = new Message();
                msg.obj = "请稍候，正在等待客户端的连接...";
                msg.what = 0;
                LinkDetectedHandler.sendMessage(msg);

                    /* 接受客户端的连接请求 */
                socket = mserverSocket.accept();
                Log.d("server", "accept success !");

                Message msg2 = new Message();
                String info = "客户端已经连接上！可以发送信息。";
                msg2.obj = info;
                msg.what = 0;
                LinkDetectedHandler.sendMessage(msg2);
                //启动接受数据
                mreadThread = new readThread();
                mreadThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /* 停止服务器 */
    private void shutdownServer() {
        new Thread() {
            @Override
            public void run() {
                if(startServerThread != null)
                {
                    startServerThread.interrupt();
                    startServerThread = null;
                }
                if(mreadThread != null)
                {
                    mreadThread.interrupt();
                    mreadThread = null;
                }
                try {
                    if(socket != null)
                    {
                        socket.close();
                        socket = null;
                    }
                    if (mserverSocket != null)
                    {
                        mserverSocket.close();/* 关闭服务器 */
                        mserverSocket = null;
                    }
                } catch (IOException e) {
                    Log.e("server", "mserverSocket.close()", e);
                }
            };
        }.start();
    }
    /* 停止客户端连接 */
    private void shutdownClient() {
        new Thread() {
            @Override
            public void run() {
                if(clientConnectThread!=null)
                {
                    clientConnectThread.interrupt();
                    clientConnectThread= null;
                }
                if(mreadThread != null)
                {
                    mreadThread.interrupt();
                    mreadThread = null;
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    socket = null;
                }
            };
        }.start();
    }

    //发送数据
    private void sendMessageHandle(byte[] msg)
    {
        if (socket == null)
        {
            Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            OutputStream os = socket.getOutputStream();
            os.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //读取数据
    private class readThread extends Thread {
        @Override
        public void run() {

            byte[] buffer = new byte[1024];
            int bytes;
            InputStream mmInStream = null;

            try {
                mmInStream = socket.getInputStream();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            while (true) {
                try {
                    // Read from the InputStream
                    if( (bytes = mmInStream.read(buffer)) > 0 )
                    {
                        byte[] buf_data = new byte[bytes];
                        for(int i=0; i<bytes; i++)
                        {
                            buf_data[i] = buffer[i];
                            add_revice_date(buffer[i]);
                        }
                       // String s = new String(buf_data);
                        Message msg = new Message();
                        msg.obj = getRevice_date();
                        msg.arg1=bytes;
                        msg.what = 1;
                        LinkDetectedHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    try {
                        mmInStream.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
    private Handler LinkDetectedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                int date_length=msg.arg1;
                byte[] buf_data = new byte[32];
                analyse_cmd((byte[]) msg.obj, buf_data);
                //Log.d("clicent", "buf_data[5]=" + buf_data[5] + " getCommand()=" + getCommand());
                if(buf_data[5]==getCommand()&&checkdata(buf_data)) {
                    switch (buf_data[5]) {
                        case cmd_device_ack:
                            break;
                        case cmd_syn_time:
                            break;
                        case cmd_read_time:
                           // get_textview_read_time(buf_data);
                            setCommand((byte)0x0);
                            del_revice_date();
                            break;
                        case cmd_read_single_date: {
                           // TV_Prompt.setText(byteArrayToHexString((byte[]) msg.obj));
                            set_textview_GLU_R((buf_data[16] >> 1) & 0x07);
                            set_textview_TV_BIL_R(((buf_data[16] << 2) & 0x04) | ((buf_data[17] >> 6) & 0x03));
                            set_textview_TV_KET_R((buf_data[17] >> 3) & 0x07);
                            set_textview_TV_BLD_R((buf_data[14] >> 4) & 0x07);
                            set_textview_TV_RPO_R(((buf_data[14] << 2) & 0x04) | ((buf_data[15] >> 6) & 0x03));
                            set_textview_TV_URO_R((buf_data[15] >> 3) & 0x07);
                            set_textview_TV_NIT_R(buf_data[15] & 0x07);
                            set_textview_TV_VC_R(buf_data[16] >> 4 & 0x07);
                            set_textview_TV_LEU_R((buf_data[12] >> 3) & 0x07);
                            set_textview_TV_PH_R((buf_data[14] >> 1) & 0x07);
                            set_textview_TV_SG_R(buf_data[17] & 0x07);
                            setCommand((byte)0x0);
                            del_revice_date();
                            break;
                        }
                        case cmd_read_all_date:
                            break;
                        case cmd_del_date:
                            break;
                        case cmd_error:
                            break;
                        case cmd_close_bt:
                            break;
                        case cmd_rename_bt:
                            break;
                        case cmd_collect_finger:{
                            if(buf_data[6]==0x0) {
                                Toast.makeText(mContext, "检查到指纹输入", Toast.LENGTH_LONG).show();
                            }else if(buf_data[6]==0x28){
                                Toast.makeText(mContext, "未检出到指纹输入", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(mContext, "代码有错", Toast.LENGTH_LONG).show();
                            }
                        }
                            break;
                        case cmd_del_finger:{
                            if(buf_data[6]==0x0) {
                                Toast.makeText(mContext, "删除指纹成功", Toast.LENGTH_LONG).show();
                            }else if(buf_data[6]==0x28){
                                Toast.makeText(mContext, "删除指纹失败", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(mContext, "代码有错", Toast.LENGTH_LONG).show();
                            }
                        }
                            break;
                        default:

                    }
                }
            }
            else if(msg.what==2){
                sendCommand(getCommand());
            }
            else
            {
                Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
            }


        }
    };
    class BClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.B_FINGER:
                    setCommand(cmd_collect_finger);
                    sendCommand(cmd_collect_finger);
                    ShowSpinnerDialog();
                    Toast.makeText(mContext, "采集指纹", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.B_DELFINGER:
                    setCommand(cmd_del_finger);
                    sendCommand(cmd_del_finger);
                    ShowSpinnerDialog();
                    Toast.makeText(mContext, "删除指纹", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    public  String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length*2];
        int v;

        for(int j=0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v>>>4];
            hexChars[j*2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    private void sendCommand(byte command)
    {
        switch (command) {

            case cmd_device_ack:
                break;
            case cmd_syn_time:
                break;
            case cmd_read_time:
                byte[] read_time_buffer = new byte[]{(byte)0x93,(byte)0x8e,0x04,0x00,0x08,0x03,0x0f};
                sendMessageHandle(read_time_buffer);
                break;
            case cmd_read_single_date: {
                byte[] ackbuffer = new byte[]{(byte)0x93,(byte)0x8e,0x04,0x00,0x08,0x04,0x10};
                sendMessageHandle(ackbuffer);
            }
            case cmd_read_all_date:
                break;
            case cmd_del_date:
                break;
            case cmd_error:
                break;
            case cmd_close_bt:
                break;
            case cmd_rename_bt:
                break;
            default:
        }
    }
    private void add_revice_date(byte buf_data) {
        revice_date[revice_date_length] = buf_data;
        revice_date_length++;

    }
    private void del_revice_date()
    {
        Arrays.fill(revice_date,(byte)0);
        revice_date_length=0;
    }
    private byte[] getRevice_date()
    {
        return revice_date;
    }
    private void analyse_cmd(byte[] buf_data,byte[]packages)
    {
        for(int i=0;i<revice_date_length-4;i++)
        {
            if ((buf_data[i]==(byte)0x93)&&(buf_data[i+1]==(byte)0x8e)&&(buf_data[i+3]==(byte)0x00)&&(buf_data[i+4]==(byte)0x08))
            {
                for(int j=0;j<(buf_data[i+2]+3);j++)
                {
                    packages[j]=buf_data[i+j];
                }

            }

        }

    }
    private boolean checkdata(byte[]buf_data)
    {
       int  mun=0;
        int i=0;
        for(i=2;i<(int)buf_data[2]+2;i++)
        {
            mun=mun+buf_data[i]&0xff;
        }
        return ((byte)mun==buf_data[buf_data[2]+2]);
    }
    private void ShowSpinnerDialog()
    {
        final ProgressDialog updatedialog = new ProgressDialog(this);
        updatedialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        //updatedialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        updatedialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        //updatedialog.setIcon(R.drawable.ic_launcher);//
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        //updatedialog.setTitle("提示");
        updatedialog.setMessage("请等待....");
        updatedialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    int time=0;
                    while ((getCommand()!=0x0)&&(time<50)) {
                        Thread.sleep(500);
                        time++;
                        Message msg = new Message();
                        msg.what = 2;
                        LinkDetectedHandler.sendMessage(msg);
                        Log.e("server", "cmd_read_single_date="+getCommand());
                        Thread.sleep(50);
                        // dialog.dismiss();
                    }
                    updatedialog.cancel();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private byte getCommand()
    {
        return command;
    }
    private void setCommand(byte cmd)
    {
        command=cmd;
    }
    private void get_textview_read_time(byte[] buffdata)
    {
        String times=Byte.toString(buffdata[6])+"/"+Byte.toString(buffdata[7])+"/"+Byte.toString(buffdata[8])+" " +Byte.toString(buffdata[9])+":"+Byte.toString(buffdata[10]);
        TV_Prompt.setText(times);
    }
    private void set_textview_GLU_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_GLU_R.setText("-");
                break;
            case 1:
                TV_GLU_R.setText("+-");
                break;
            case 2:
                TV_GLU_R.setText("+1");
                break;
            case 3:
                TV_GLU_R.setText("+2");
                break;
            case 4:
                TV_GLU_R.setText("+3");
                break;
            case 5:
                TV_GLU_R.setText("+4");
                break;
            default:
                TV_GLU_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_BIL_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_BIL_R.setText("-");
                break;
            case 1:
                TV_BIL_R.setText("+1");
                break;
            case 2:
                TV_BIL_R.setText("+2");
                break;
            case 3:
                TV_BIL_R.setText("+3");
                break;
            default:
                TV_BIL_R.setText("error");
                break;
        }
    }

    private void set_textview_TV_PH_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_PH_R.setText("5.0");
                break;
            case 1:
                TV_PH_R.setText("6.0");
                break;
            case 2:
                TV_PH_R.setText("6.5");
                break;
            case 3:
                TV_PH_R.setText("7.0");
                break;
            case 4:
                TV_PH_R.setText("7.5");
                break;
            case 5:
                TV_PH_R.setText("8.0");
                break;
            case 6:
                TV_PH_R.setText("8.5");
                break;
            default:
                TV_GLU_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_SG_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_SG_R.setText("1.000");
                break;
            case 1:
                TV_SG_R.setText("1.005");
                break;
            case 2:
                TV_SG_R.setText("1.010");
                break;
            case 3:
                TV_SG_R.setText("1.015");
                break;
            case 4:
                TV_SG_R.setText("1.020");
                break;
            case 5:
                TV_SG_R.setText("1.025");
                break;
            case 6:
                TV_SG_R.setText("1.030");
                break;
            default:
                TV_SG_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_KET_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_KET_R.setText("-");
                break;
            case 1:
                TV_KET_R.setText("+-");
                break;
            case 2:
                TV_KET_R.setText("+1");
                break;
            case 3:
                TV_KET_R.setText("+2");
                break;
            case 4:
                TV_KET_R.setText("+3");
                break;
            case 5:
                TV_KET_R.setText("+4");
                break;
            default:
                TV_KET_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_BLD_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_BLD_R.setText("-");
                break;
            case 1:
                TV_BLD_R.setText("+-");
                break;
            case 2:
                TV_BLD_R.setText("+1");
                break;
            case 3:
                TV_BLD_R.setText("+2");
                break;
            case 4:
                TV_BLD_R.setText("+3");
                break;
            default:
                TV_BLD_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_RPO_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_RPO_R.setText("-");
                break;
            case 1:
                TV_RPO_R.setText("+-");
                break;
            case 2:
                TV_RPO_R.setText("+1");
                break;
            case 3:
                TV_RPO_R.setText("+2");
                break;
            case 4:
                TV_RPO_R.setText("+3");
                break;
            case 5:
                TV_RPO_R.setText("+4");
                break;
            default:
                TV_RPO_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_URO_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_URO_R.setText("-");
                break;
            case 1:
                TV_URO_R.setText("+1");
                break;
            case 2:
                TV_URO_R.setText("+2");
                break;
            case 3:
                TV_URO_R.setText("+3");
                break;
            default:
                TV_URO_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_NIT_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_NIT_R.setText("-");
                break;
            case 1:
                TV_NIT_R.setText("+");
                break;
            default:
                TV_NIT_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_LEU_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_LEU_R.setText("-");
                break;
            case 1:
                TV_LEU_R.setText("+-");
                break;
            case 2:
                TV_LEU_R.setText("+1");
                break;
            case 3:
                TV_LEU_R.setText("+2");
                break;
            case 4:
                TV_LEU_R.setText("+3");
                break;
            default:
                TV_LEU_R.setText("error");
                break;
        }
    }
    private void set_textview_TV_VC_R(int Reslut)
    {
        switch (Reslut)
        {
            case 0:
                TV_VC_R.setText("-");
                break;
            case 1:
                TV_VC_R.setText("+-");
                break;
            case 2:
                TV_VC_R.setText("+1");
                break;
            case 3:
                TV_VC_R.setText("+2");
                break;
            case 4:
                TV_VC_R.setText("+3");
                break;
            default:
                TV_VC_R.setText("error");
                break;
        }
    }
}
