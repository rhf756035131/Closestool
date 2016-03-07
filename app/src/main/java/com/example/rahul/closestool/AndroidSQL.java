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
import java.util.List;


import mediatek.android.IoTManager.SmartConnection;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * 本例实现SQLite数据库增加、删除、修改、模糊查询操作。这里不是最好的实现方法,
 * 如想研究SQL如何封装，请详细查看SQLiteDatebase类.
 * 查看SQL语句：String sql = SQLiteQueryBuilder.buildQueryString();
 * 
 * 
 * @author  
 */
public class AndroidSQL extends Activity {
	private static String DB_NAME = "mycity.db";
	private static int DB_VERSION = 1;
	private static int POSTION;
	private ListView listview;
	private Cursor cursor;
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private ListAdapter listAdapter;
	
	private EditText etCity;
	private EditText etCode;
	private Button bt_add;
	private Button bt_modify;
	private Button bt_query;
	private Button bntSearch;
	private Button backSetting;

	private byte[] DataReceive;  
	private byte[] DataReceive1;  
	private DatagramPacket pack = null;
	private DatagramPacket pack1 = null;
	
	private MulticastSocket ms;  
	private MulticastSocket ms1;
	private DatagramPacket dataPacket;  
	private DatagramPacket dataPacket1;
	private DatagramSocket udpSocket; 
	private static final int MAX_DATA_PACKET_LENGTH = 40;  
	private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH]; 
	private byte[] buffer1 = new byte[MAX_DATA_PACKET_LENGTH];  
	
	private String udpresult;	//服务器返回的消息
	private String udpresult1;	//服务器返回的消息
	
	private ProgressBar ProgressBar1;
	private WifiAdmin mWifiAdmin;   
	int ip;
	private boolean isSearchTimeout = false;
	private TextView title;
	
//	protected final static String VERSION="1.0";
	protected final static int MENU_ADD = Menu.FIRST;
	protected final static int MENU_DELETE = Menu.FIRST + 1;
	protected final static int MENU_VERSION = Menu.FIRST + 2;
	
	private List<CityBean> cityList = new ArrayList<CityBean>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	
    	bntSearch= (Button)findViewById(R.id.global_search);
		bntSearch.setVisibility(View.VISIBLE);
		backSetting = (Button) findViewById(R.id.bnt_global_back);
		backSetting.setVisibility(View.VISIBLE);
		title =  (TextView)findViewById(R.id.title);
		title.setText(this.getResources().getString(R.string.settinglist));
//		bntSearch.setOnClickListener(this);
		
		ProgressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
		
		
		
    	etCity = (EditText) findViewById(R.id.etCity);
    	etCode = (EditText) findViewById(R.id.etCode);
    	bt_add = (Button) findViewById(R.id.bt_add);
    	bt_modify = (Button) findViewById(R.id.bt_modify);
    	bt_query = (Button) findViewById(R.id.bt_query);
    	
    	
    	
    	
    	
    	mWifiAdmin = new WifiAdmin(AndroidSQL.this); 
    	try{
    		/* 初始化并创建数据库 */
    		dbHelper = new SQLiteHelper(this, DB_NAME, null, DB_VERSION);
    		/* 创建表 */
    		db = dbHelper.getWritableDatabase();	//调用SQLiteHelper.OnCreate()        	
        	/* 查询表，得到cursor对象 */
        	cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, CityBean.NAME + " DESC");
        	cursor.moveToFirst();
        	while(!cursor.isAfterLast() && (cursor.getString(1) != null)){    
        		CityBean city = new CityBean();
        		city.setId(cursor.getString(0));
        		city.setIp(cursor.getString(1));
        		city.setName(cursor.getString(2));
        		cityList.add(city);
        		cursor.moveToNext();
        	}
    	}catch(IllegalArgumentException e){
    		//当用SimpleCursorAdapter装载数据时，表ID列必须是_id，否则报错column '_id' does not exist
    		e.printStackTrace();
    		//当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表 注：表以前数据将丢失
    		++ DB_VERSION;
    		dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
//    		dbHelper.updateColumn(db, SQLiteHelper.ID, "_"+SQLiteHelper.ID, "integer");
    	}
    	listview = (ListView)findViewById(R.id.listView);
    	listAdapter = new ListAdapter();
    	listview.setAdapter(listAdapter);
    	listview.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int postion,
					long arg3) {
				System.out.println("你选择了项目"+postion);
//				setSelectedValues(postion);
				
				WifiManager wifimanager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
				if(wifimanager.isWifiEnabled())
				{
					System.out.println("WIFI是打开的");
					
					Intent intent2 = new Intent(AndroidSQL.this,Closestool_info.class);
					String addr=cityList.get(postion).getIp();
					intent2.putExtra("addr", addr);
//					System.out.println("这个是进入控制界面的");
					startActivity(intent2);
				}
				else
				{
					System.out.println("请打开wifi");
//					Toast.makeText(AndroidSQL.this, "按键名称修改为:"+username, Toast.LENGTH_SHORT).show();
					Toast.makeText(AndroidSQL.this,"请先连接WiFi",Toast.LENGTH_LONG).show();
				}
				
			}    		
    	});
    	bntSearch.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("你点击了按钮");
				
				
				
				db.delete(SQLiteHelper.TB_NAME, null, null);
				cityList.clear();
				
				
				
				//只有在wifi模式下才需要搜索
				WifiManager wifimanager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
				if(wifimanager.isWifiEnabled())
				{
					System.out.println("WIFI是打开的");
					
					ProgressBar1.setVisibility(View.VISIBLE);
					bntSearch.setVisibility(View.INVISIBLE);
					SearchModuleTimeout(); 
					M30UDPBroast1();     //进行搜索  
				}
				else
				{
					System.out.println("请打开wifi");
//					Toast.makeText(AndroidSQL.this, "按键名称修改为:"+username, Toast.LENGTH_SHORT).show();
					Toast.makeText(AndroidSQL.this,"请先连接WiFi",Toast.LENGTH_LONG).show();
				}
				
//				 Message mes=new Message();  
//	        	 mes.what=3;        	 
//	        	 handler.sendMessage(mes);
	        	 
				
//				
//				
//				ProgressBar1.setVisibility(View.INVISIBLE);
//	    		bntSearch.setVisibility(View.VISIBLE); 
			}

    	});
    	backSetting.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("你点击了按钮");
				//只有在wifi模式下才需要搜索
				WifiManager wifimanager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
				if(wifimanager.isWifiEnabled())
				{
					System.out.println("WIFI是打开的");
					Intent intent2 = new Intent(AndroidSQL.this,SmartConnection.class);
					startActivity(intent2);
				}
				else
				{
					System.out.println("请打开wifi");
//					Toast.makeText(AndroidSQL.this, "按键名称修改为:"+username, Toast.LENGTH_SHORT).show();
					Toast.makeText(AndroidSQL.this,"请先连接WiFi",Toast.LENGTH_LONG).show();
				}
				
			}

    	});
    	/* 插入表数据并ListView显示更新 */
    	bt_add.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(etCity.getText().length() > 1 && etCode.getText().length() >1){
					ContentValues values = new ContentValues();
					values.put(CityBean.IP, etCity.getText().toString().trim());
					values.put(CityBean.NAME, etCode.getText().toString().trim());
					values.put(CityBean.MAC, "this is mac");
					//插入数据 用ContentValues对象也即HashMap操作,并返回ID号
					Long cityID = db.insert(SQLiteHelper.TB_NAME, CityBean.ID, values);

					System.out.println("CityBean.ID="+CityBean.ID);
					System.out.println("cityID="+cityID);

					CityBean city = new CityBean();
					city.setId(""+cityID);
	        		city.setIp(etCity.getText().toString().trim());
	        		city.setName(etCode.getText().toString().trim());
	        		city.setMac("this is mac");
	        		cityList.add(city);
	        		listview.setAdapter(new ListAdapter());
	        		resetForm();
				}
			}
		});
    	
    	/* 查询表，模糊条件查询 */
    	bt_query.setOnClickListener(new Button.OnClickListener(){
    		@Override
    		public void onClick(View view) {
    			cityList.removeAll(cityList);
    			String sql = null;
    			String sqlCity = etCity.getText().length() > 0 ? CityBean.IP + " like '%" + etCity.getText().toString().trim() + "%'" : "";
    			String sqlCode = etCode.getText().length() > 0 ? CityBean.IP + " like '%" + etCity.getText().toString().trim() + "%'" : "";
    			if( (!"".equals(sqlCity)) && (!"".equals(sqlCode)) ){
    				sql = sqlCity + " and" + sqlCode;
    			}else if(!"".equals(sqlCity)){
    				sql = sqlCity;
    			}else if(!"".equals(sqlCode)){
    				sql = sqlCode;
    			}
    			cursor = db.query(true, SQLiteHelper.TB_NAME,
    					new String[]{CityBean.ID, CityBean.IP, CityBean.NAME},
    					sql,
    					null, null, null, null, null);
    			cursor.moveToFirst();
    			while(!cursor.isAfterLast() && (cursor.getString(1) != null)){
    				CityBean city = new CityBean();
            		city.setId(cursor.getString(0));
            		city.setIp(cursor.getString(1));
            		city.setName(cursor.getString(2));
            		cityList.add(city);
    				cursor.moveToNext();
    			}
    			listview.setAdapter(new ListAdapter());
    			resetForm();
    		}
    	});
    	
    	/* 修改表数据 */
    	bt_modify.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ContentValues values = new ContentValues();
				values.put(CityBean.IP, etCity.getText().toString().trim());
				values.put(CityBean.NAME, etCode.getText().toString().trim());
				db.update(SQLiteHelper.TB_NAME, values, CityBean.ID + "=" + cityList.get(POSTION).getId(), null);
				cityList.get(POSTION).setIp(etCity.getText().toString().trim());
				cityList.get(POSTION).setName(etCode.getText().toString().trim());
				listview.setAdapter(new ListAdapter());
				resetForm();
			}
		});
    }
    
    
	Handler handler = new Handler(){   
	    public void handleMessage(Message msg) {  
	    	String str =(String)msg.obj;
	    	
	    	switch(msg.what){
	    	case 1:
//	    		getList(str);	
	    		if(str.indexOf("+/")!=-1)
	    		{
	    			String addr=str.substring(str.indexOf("+/")+2);
	    			String str1=str.substring(0,str.indexOf("+/"));
	    	  	     
	    			ContentValues values = new ContentValues();
					values.put(CityBean.IP, addr);
					values.put(CityBean.NAME, str1);
					values.put(CityBean.MAC, "this is mac");
					//插入数据 用ContentValues对象也即HashMap操作,并返回ID号
					Long cityID = db.insert(SQLiteHelper.TB_NAME, CityBean.ID, values);
					
					System.out.println("CityBean.ID="+CityBean.ID);
					System.out.println("cityID="+cityID);
					
					CityBean city = new CityBean();
					city.setId(""+cityID);
	        		city.setIp(addr);
	        		city.setName(str1);
	        		city.setMac("this is mac");
	        		cityList.add(city);
	        		listview.setAdapter(new ListAdapter());
	    		}
	    		break;
	    	case 2:
	    		ProgressBar1.setVisibility(View.INVISIBLE);
	    		bntSearch.setVisibility(View.VISIBLE); 
	    	case 3:
//	    		SearchModule("HLK","HLK");
	    		break;
	    		default:
	    			break;
	    	}
	    	
	    }  
	      
	};  
	
	Thread mSearchModuleTimeout;
	public void SearchModuleTimeout() {
		if (mSearchModuleTimeout == null) { //这个是给搜索线程计时的线程
			mSearchModuleTimeout = new Thread(new Runnable() { //
						@Override
						public void run() {
							try {
								isSearchTimeout = false;
								Thread.sleep(3000);
								if (isSearchTimeout == false) {
									isSearchTimeout = true;
									System.out.println("延时结束");
									Message mes=new Message();    
									mes.what=2;
						        	mes.obj="closeprogressbar";					       
						        	handler.sendMessage(mes);

//									ms.close();
//									ms=null;
								}
							} catch (InterruptedException e) {
								System.out.println("timeout thread is Interrupted");
							} finally {
								mSearchModuleTimeout = null;
							}
						}
					});
			mSearchModuleTimeout.start();
		}
	}
	
	public void M30UDPBroast1( )
	  {

		String strip = "";
		String mac;
		String mac1;
		String gatewayip=null;
//		mac=mWifiAdmin.getMacAddress();//这个是手机的MAC地址
//		mac1=mWifiAdmin.getBSSID();//这个返回的手机所连接的wifi的mac
//		System.out.println(mac1);
		
		//只有在wifi模式下才需要搜索
		WifiManager wifimanager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		if(wifimanager.isWifiEnabled())
		{
			System.out.println("WIFI是打开的");
//			WifiInfo wifiinfo=wifimanager.getConnectionInfo();
//			int ipAddress=wifiinfo.getIpAddress();
			DhcpInfo dhcpinfo=wifimanager.getDhcpInfo();
			gatewayip=Formatter.formatIpAddress(dhcpinfo.gateway);
			System.out.println("网关是："+gatewayip);
			
			WifiInfo wifiinfo=wifimanager.getConnectionInfo();
			int ipAddress=wifiinfo.getIpAddress();
			 if(ipAddress != 0){
					strip = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + "255");
				}
		
		
//		 ip=mWifiAdmin.getIpAddress();
//		 if(ip != 0){
//				strip = ((ip & 0xff) + "." + (ip >> 8 & 0xff) + "." + (ip >> 16 & 0xff) + "." + "255");
//			}
		  try {  
	          /*创建socket实例*/  
			  if(ms==null)
			  {
				  ms = new MulticastSocket();
			  }
			  if(ms1==null)
			  {
				  ms1= new MulticastSocket();
			  }
			 SearchModule(); 
			 SearchModule1();
	      } catch (Exception e) {  
	    	  System.out.println("创建ms出现错误");
	          e.printStackTrace();  
	      } 
		  dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH); 
		  String str=new String("HLK");
		  byte out[] =str.getBytes();
	      dataPacket.setData(out);  
	      dataPacket.setLength(out.length);  
	      
	      dataPacket1 = new DatagramPacket(buffer1, MAX_DATA_PACKET_LENGTH); 
		  String str1=new String("HLK");
		  byte out1[] =str.getBytes();
	      dataPacket1.setData(out1);  
	      dataPacket1.setLength(out1.length);  

	      try {  
	          InetAddress address = InetAddress.getByName(strip);  
	          InetAddress address1 = InetAddress.getByName(gatewayip);  
	          dataPacket = new DatagramPacket(out, out.length, address, 988); 
	          dataPacket1 = new DatagramPacket(out1, out1.length, address1, 988);  
	          ms.send(dataPacket);
	          ms1.send(dataPacket1);
	      } catch (Exception e) {  
	          e.printStackTrace();  
	      }  
	  }		
	}
	
	
//	public int SearchModule(String str,String uuid)
//	{
//			int a=0;
//			//只有在wifi模式下才需要搜索
//			WifiManager wifimanager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
//			if(wifimanager.isWifiEnabled())
//			{
//				System.out.println("WIFI是打开的");
//				WifiInfo wifiinfo=wifimanager.getConnectionInfo();
//				int ipAddress=wifiinfo.getIpAddress();
////				DhcpInfo dhcpinfo=wifimanager.getDhcpInfo();
////				System.out.println("网络信息："+Formatter.formatIpAddress(dhcpinfo.gateway));
//				
//				
//						
////				String ip=(ipAddress & 0xFF ) + "." + ((ipAddress>> 8 ) & 0xFF) + "." +((ipAddress>> 16 ) & 0xFF) + "." +(ipAddress>> 24 & 0xFF) ;
//				String ip=(ipAddress & 0xFF ) + "." + ((ipAddress>> 8 ) & 0xFF) + "." +((ipAddress>> 16 ) & 0xFF) + "." +"255" ;
//				System.out.println(ip);
//			
//			byte out[] =str.getBytes();
//			InetAddress address;
//			try {
//				address = InetAddress.getByName(ip);
//				dataPacket = new DatagramPacket(out, out.length, address, 988);  
//		        dataPacket.setData(out);  
//			    dataPacket.setLength(out.length);  
////		        try {
//		        	MulticastSocket ms = null;
//					try {
//						ms = new MulticastSocket();
//						ms.send(dataPacket);
////						ms.getNetworkInterface();
////						System.out.println("ms的信息"+ms.getRemoteSocketAddress());
//						
//						
//						
////						try {  
////				            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
////				                NetworkInterface intf = en.nextElement();  
////				                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
////				                    InetAddress inetAddress = enumIpAddr.nextElement();  
////				                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {  
////				                    //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {  
////				                        System.out.println( "网络类型判断"+inetAddress.getHostAddress().toString() );
////				                        byte[] b=inetAddress.getAddress();
////				                        String c=new String(b.toString() );
////				                        
////				                        System.out.println( "getLocalHost"+ c );
////				                    }  
////				                }  
////				            }  
////				        } catch (Exception e) {  
////				        }  
//						
//						
//						
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//					
//					
//					
//					while(a<=3)
//					{
//						a+=1;
//						byte[] DataReceive= new byte[512];
////						for(int i=0;i<512;i++)
////						{
////							DataReceive[i]=0;
////						}
////						DataReceive=null;
//						DatagramPacket pack = new DatagramPacket(DataReceive,DataReceive.length);
//						try {
//							ms.setSoTimeout(100);
//							ms.receive(pack);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
////							e.printStackTrace();
//						}
////						udpresult = new String(pack.getData(), pack.getOffset(), pack.getLength());	
////						String udpresult = new String(pack.getData());	
////						String udpresult =pack.getData().toString();
//						byte[] Dat= new byte[512];
////						byte[] Dat= pack.getData();
//						Dat=pack.getData();
//						String a1=new String(Dat,0,512);
//						a1=a1.trim();
////				        System.out.println(a1);
////				        if(  a1.indexOf(uuid) !=-1 )
//						if(a1!=null)
//				        {
////				        	System.out.println("已查找到字符串"+uuid);
//				        	System.out.println("IP地址是："+a1);
//				        	
//				        	
//				        	 Message mes=new Message();  
//				        	 mes.what=1;
//				        	 mes.obj=udpresult+"+"+pack.getAddress();			
////				        	 System.out.println(mes.obj);
//				        	 handler.sendMessage(mes);
//				        	 
////				        	return 1; //如果查找到则返回1
//				        }
//				        
//					}
//					
//					
////					Message mes=new Message();    
////					mes.what=2;
////		        	mes.obj="closeprogressbar";					       
////		        	handler.sendMessage(mes);
////		        	
////		        	ms.close();
//		        	
//					System.out.println("接收结束");
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
//		}
//		else
//		{
//			
//		}
//			return 0;
//	}
	
	Thread mSearchModule;
	public void SearchModule() {
		if (mSearchModule == null) { //这个是搜索线程
			mSearchModule = new Thread(new Runnable() { //	
						@Override
						public void run() {	
							//System.out.println("进入搜索进程");
							DataReceive= new byte[512];
						    pack = new DatagramPacket(DataReceive,DataReceive.length);
							try {	
								while (isSearchTimeout == false) {
									ms.setSoTimeout(1000);
									ms.receive(pack);
									
									udpresult = new String(pack.getData(), pack.getOffset(), pack.getLength());	
//									udpresult=new String(udpresult.getBytes(), "utf-8");
//							        System.out.println(udpresult);
//							        if(udpresult.indexOf("HLK")!=-1)
//							        {							        	
							        	 Message mes=new Message();  
							        	 mes.what=1;
							        	 
							        	 mes.obj=udpresult+"+"+pack.getAddress();			
							        	 System.out.println(mes.obj);
							        	 handler.sendMessage(mes);    //
//							        }
									System.out.println("线程1正在搜索......");
								}
								ms.close();
							}catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally {
								mSearchModule = null;
							
							}
							System.out.println("搜索结束");
						}
					});
			mSearchModule.start();
		}

	}
	
	Thread mSearchModule1;
	public void SearchModule1() {
		if (mSearchModule1 == null) { //这个是搜索线程
			mSearchModule1 = new Thread(new Runnable() { //	
						@Override
						public void run() {	
							//System.out.println("进入搜索进程");
							DataReceive1= new byte[512];
						    pack1 = new DatagramPacket(DataReceive1,DataReceive1.length);
							try {	
								while (isSearchTimeout == false) {
									ms1.setSoTimeout(1000);
									ms1.receive(pack1);
									
									udpresult1 = new String(pack1.getData(), pack1.getOffset(), pack1.getLength());	
//									udpresult=new String(udpresult.getBytes(), "utf-8");
//							        System.out.println(udpresult);
//							        if(udpresult.indexOf("HLK")!=-1)
//							        {							        	
							        	 Message mes=new Message();  
							        	 mes.what=1;
							        	 
							        	 mes.obj=udpresult1+"+"+pack1.getAddress();			
							        	 System.out.println(mes.obj);
							        	 handler.sendMessage(mes);    //
//							        }
									System.out.println("线程2正在搜索......");
								}
								ms1.close();
							}catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally {
								mSearchModule1 = null;
							
							}
							System.out.println("搜索结束");
						}
					});
			mSearchModule1.start();
		}

	}
	
	
    /* 设置选中ListView的值 */
    public void setSelectedValues(int postion){
    	POSTION = postion;
		etCity.setText(cityList.get(postion).getIp());
		etCode.setText(cityList.get(postion).getName());
    }
    
    /* 重值form */
    public void resetForm(){
		etCity.setText("");
		etCode.setText("");
    }
    
    @Override
    protected void onDestroy() {
//    	db.delete(SQLiteHelper.TB_NAME, null, null);
    	super.onDestroy();
    }
    
    private class ListAdapter extends BaseAdapter{
    	public ListAdapter(){
    		super();
    	}
		@Override
		public int getCount() {
			return cityList.size();
		}

		@Override
		public Object getItem(int postion) {
			return postion;
		}

		@Override
		public long getItemId(int postion) {
			return postion;
		}

		@Override
		public View getView(final int postion, View view, ViewGroup parent) {
			view = getLayoutInflater().inflate(R.layout.listview, null);
			
			TextView tvip = (TextView) view.findViewById(R.id.ip);
			tvip.setText("" + cityList.get(postion).getIp());
			
			TextView tvname = (TextView) view.findViewById(R.id.name);
			tvname.setText("" + cityList.get(postion).getName());
			
			TextView tvmac = (TextView) view.findViewById(R.id.mac);
			tvmac.setText("" + cityList.get(postion).getMac());
			
			Button bu = (Button) view.findViewById(R.id.btRemove);
//			bu.setText(R.string.delete);
			bu.setId(Integer.parseInt(cityList.get(postion).getId()));
			
//			TextView bu = (TextView) view.findViewById(R.id.btRemove);
//			bu.setText(R.string.delete);
//			bu.setId(Integer.parseInt(cityList.get(postion).getId()));
			
			/* 删除表数据 */
			bu.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View view) {
//					try{
//						db.delete(SQLiteHelper.TB_NAME, CityBean.ID + "=" + view.getId(), null);
//						cityList.remove(postion);
//						listview.setAdapter(new ListAdapter());						
//					}catch(Exception e){
//						e.printStackTrace();
//					}
					
					WifiManager wifimanager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
					if(wifimanager.isWifiEnabled())
					{
						System.out.println("WIFI是打开的");
						
						Intent intent2 = new Intent(AndroidSQL.this,EditM30Paramter.class);
						String addr=cityList.get(postion).getIp();
						intent2.putExtra("addr", addr);
						startActivity(intent2);
					}
					else
					{
						System.out.println("请打开wifi");
//						Toast.makeText(AndroidSQL.this, "按键名称修改为:"+username, Toast.LENGTH_SHORT).show();
						Toast.makeText(AndroidSQL.this,"请先连接WiFi",Toast.LENGTH_LONG).show();
					}
					
					
				}
			});
			return view;
		}
    }
    
    
    public void showInfo(){
		new AlertDialog.Builder(this)
		.setTitle("HLK-M30配置工具")
		.setMessage("版本:"+this.getResources().getString(R.string.app_name_ver))
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
		
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_ADD, 0, "主页");
		menu.add(Menu.NONE, MENU_DELETE, 0, "版本更新");
		menu.add(Menu.NONE, MENU_VERSION, 0, "ver");
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_ADD:   //跳转到公司主页
			System.out.println("MENU_ADD");
			Uri uri = Uri.parse("http://www.hlktech.com/");    
			Intent it = new Intent(Intent.ACTION_VIEW, uri);    
			startActivity(it);
			break;
		case MENU_DELETE:
			System.out.println("MENU_DELETE");
//			delete();
			break;
		case MENU_VERSION:
			System.out.println("MENU_UPDATE");
			showInfo();
//			update();
			break;
		}
		return true;
	}
}