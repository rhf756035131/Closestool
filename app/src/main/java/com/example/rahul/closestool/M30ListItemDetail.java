package com.example.rahul.closestool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;






import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class M30ListItemDetail extends Activity {

	private static final int MAX_DATA_PACKET_LENGTH = 40;  
	private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];  
	private DatagramPacket dataPacket;  
	private DatagramSocket udpSocket; 
	
	
	private Button gpio0_on;
	private Button gpio0_off;
	
	private Button gpio1_on;
	private Button gpio1_off;
	
	private Button gpio2_on;
	private Button gpio2_off;

	String addrIP;
	private String id;
	private String addr;
	private Button btn_back;
	private TextView title;
	
	private byte[] DataReceive;  
	private DatagramPacket pack = null;
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		id = getIntent().getStringExtra("id");
		addrIP = getIntent().getStringExtra("addr");
//		System.out.println(addr);
		
		setContentView(R.layout.m30_list_item_detail);
		
		btn_back=(Button)findViewById(R.id.bnt_global_back1);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title =  (TextView)findViewById(R.id.title);
//
		title.setText(this.getResources().getString(R.string.ledcontrol));
		
		gpio0_on = (Button) findViewById(R.id.gpio0_on);
		gpio0_off = (Button) findViewById(R.id.gpio0_off);
		gpio0_on.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//
				M30Brocast(addrIP,"hlkATat+GW=0,0");
			}	
		});
		gpio0_off.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				M30Brocast(addrIP,"hlkATat+GW=0,1");
			}
		});
		
		gpio1_on = (Button) findViewById(R.id.gpio1_on);
		gpio1_off = (Button) findViewById(R.id.gpio1_off);
		gpio1_on.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				M30Brocast(addrIP,"hlkATat+GW=1,0");
			}
		});
		gpio1_off.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				M30Brocast(addrIP,"hlkATat+GW=1,1");
			}
		});
		
		
		gpio2_on = (Button) findViewById(R.id.gpio2_on);
		gpio2_off = (Button) findViewById(R.id.gpio2_off);
		gpio2_on.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				M30Brocast(addrIP,"hlkATat+GW=2,0");
			}
		});
		gpio2_off.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				M30Brocast(addrIP,"hlkATat+GW=2,1");
			}
		});
		
//		SqlManager mSqlManager  = new SqlManager(this); 
//        ArrayList<HashMap<String, Object>> temp = mSqlManager.QueryAllDataExByID(id);
//        addrIP = temp.get(0).get(NoteColumns.PRODUCT_ID).toString();
//        mstrPwd = temp.get(0).get(NoteColumns.PRODUCT_PWD).toString();
//        log("mstrUUID,mstrPwd:"+mstrUUID+","+mstrPwd);


	}

	private void M30Control(String addr,String command) {

		 DatagramPacket pack = null;
         byte[] DataReceive= new byte[512];
         
         dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);
         
      
        String str = new String(command);
        byte out[] = str.getBytes();  
		dataPacket.setData(out);  
        dataPacket.setLength(out.length); 
        dataPacket.setPort(8080); 
		
		try {
			
			udpSocket = new DatagramSocket(8080);  
			InetAddress broadcastAddr = InetAddress.getByName(addr);
			dataPacket.setAddress(broadcastAddr);  
			udpSocket.send(dataPacket);
			udpSocket.close();
	         
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

         
	}
	
	public void M30Brocast(String addr,String command)
	{
	
		dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);
//		String str = new String(command);
		byte out[] = command.getBytes();  
        dataPacket.setData(out);  
        dataPacket.setLength(out.length);  
//        DataReceive= new byte[512];
//	    pack = new DatagramPacket(DataReceive,DataReceive.length);
        MulticastSocket ms=null;
        
		try {
			ms = new MulticastSocket();
//			ms.setTTL((byte) 100);
			InetAddress address = InetAddress.getByName(addr);
			dataPacket = new DatagramPacket(out, out.length, address, 988);
			ms.send(dataPacket);
//			ms.receive(pack);
			

			ms.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
         
	}

	public String M30Brocast_check(String addr,String command)
	{
		String udpresult;
		
		
		byte[] DataReceive= new byte[512];
		DatagramPacket pack = new DatagramPacket(DataReceive,DataReceive.length);
        MulticastSocket ms=null;
		try {
			ms = new MulticastSocket();
			InetAddress address = InetAddress.getByName(addr);
			byte out[] = command.getBytes();  
			DatagramPacket dataPacket = new DatagramPacket(out, out.length, address, 988);

			ms.send(dataPacket);
			ms.setSoTimeout(500);
			ms.receive(pack);
			udpresult = new String(pack.getData(), pack.getOffset(), pack.getLength());

			ms.close();
			return udpresult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return null;
	}
	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//
//		switch (v.getId()) {
//		case R.id.gpio0_on:
//			M30Brocast(addrIP,"hlkATat+GW=0,0");
//			break;
//		case R.id.gpio0_off:
//			M30Brocast(addrIP,"hlkATat+GW=0,1");
//			break;
//			
//		case R.id.gpio1_on:
//			M30Brocast(addrIP,"hlkATat+GW=1,0");
//			break;
//		case R.id.gpio1_off:
//			M30Brocast(addrIP,"hlkATat+GW=1,1");
//			break;
//			
//		case R.id.gpio2_on:
//			M30Brocast(addrIP,"hlkATat+GW=2,0");
//			break;
//		case R.id.gpio2_off:
//			M30Brocast(addrIP,"hlkATat+GW=2,1");
//			break;
//			
//		default:
//			break;
//		}
//	}

}
