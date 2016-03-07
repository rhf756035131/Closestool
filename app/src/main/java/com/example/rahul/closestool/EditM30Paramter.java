package com.example.rahul.closestool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditM30Paramter extends Activity implements View.OnClickListener {

	private String TAG="EDITM30PARAMTER";
	private static final String[] botelvliebiao = {"choose:", "115200", "57600", "38400", "19200", "9600", "4800", "2400", "1200" };
	private Spinner botelv;
	private ArrayAdapter<String> botelv_adapter;
	private static final String[] wangluoxieyiliebiao = {"choose:", "TCP server", "TCP client", "UDP server", "UDP client" };
	private Spinner wangluoxieyi;
	private Spinner gongzuomoshi;
	private ArrayAdapter<CharSequence> wangluoxieyi_adapter;
	private ArrayAdapter<CharSequence> gongzuomoshi_adapter;
	private static final String[] shujuweiliebiao = {"choose:",  "8", "7", "6", "5" };
	private Spinner shujuwei;
	private ArrayAdapter<String> shujuwei_adapter;
	private static final String[] jiaoyanweiliebiao = {"choose:",  "NONE", "ODD", "EVEN" };
	private Spinner jiaoyanwei;
	private ArrayAdapter<String> jiaoyanwei_adapter;
	private static final String[] tingzhiweiliebiao = {"choose:",  "1", "2" };
	private Spinner tingzhiwei;
	private ArrayAdapter<String> tingzhiwei_adapter;
	private static final String[] jiamifangshiliebiao = { "choose:", "NONE","WEP_OPEN","WEP_SHARE","WPA_TKIP","WPS_AES","WPA_TKIP","WAP2_AES","WPA/WPA2_TKIP","WPA/WAP2_AES","AUTO"};
	private Spinner jiamifangshi;
	private ArrayAdapter<String> jiamifangshi_adapter;
	private Spinner wangluomoshi;
	private ArrayAdapter<CharSequence> wangluomoshi_adapter;
//	private static final String[] shujuweiliebiao = {"choose:",  "8", "7", "6", "5" };
	
	private Button btn_back;
	private Button btn_ok;
	private Button btn_check;
	private Button editm30paramterbtn1;
	String addrIP;
	private String id;
	private TextView title;
	private EditText wangluomingcheng;
	private EditText mima;
	private EditText remoteip;
	private EditText remoteport;
	private EditText localport;
	
	private EditText localip;
	private EditText ziwangyanma;
	private EditText wangguan;
	private EditText dns;
	
	private CheckBox cb;
	
	private int commandflag=0;
	M30ListItemDetail m30listitemdetail = new M30ListItemDetail();
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		addrIP = getIntent().getStringExtra("addr");
		
		setContentView(R.layout.edit_m30_paramter);
		
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
		title.setText(this.getResources().getString(R.string.paramtersetting));
		
		btn_ok = (Button) findViewById(R.id.bnt_edit_device_paramter_quit);
		btn_ok.setOnClickListener((OnClickListener) this);
		btn_check = (Button) findViewById(R.id.bnt_edit_device_paramter_check);
		btn_check.setOnClickListener((OnClickListener) this);
		
		wangluomingcheng=(EditText)findViewById(R.id.wangluomingcheng);
		mima=(EditText)findViewById(R.id.mima);
		remoteip=(EditText)findViewById(R.id.remoteip);
		remoteport=(EditText)findViewById(R.id.remoteport);
		localport=(EditText)findViewById(R.id.localport);
		
		localip=(EditText)findViewById(R.id.t_localip);
		ziwangyanma=(EditText)findViewById(R.id.t_ziwangyanma);
		wangguan=(EditText)findViewById(R.id.t_wangguan);
		dns=(EditText)findViewById(R.id.t_dns);
		
		cb=(CheckBox)findViewById(R.id.checkbox5);
		
//		editm30paramterbtn1 = (Button)findViewById(R.id.editm30paramter_btn1);
//		editm30paramterbtn1.setOnClickListener((OnClickListener)this);
		
		
		botelv = (Spinner) findViewById(R.id.botelv);
		wangluoxieyi = (Spinner) findViewById(R.id.wangluoxieyi);
		gongzuomoshi= (Spinner) findViewById(R.id.gongzuomoshi);
		shujuwei = (Spinner) findViewById(R.id.shujuwei);
		jiaoyanwei = (Spinner) findViewById(R.id.jiaoyanwei);
		tingzhiwei = (Spinner) findViewById(R.id.tingzhiwei);
		jiamifangshi = (Spinner) findViewById(R.id.jiamifangshi);

		botelv_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, botelvliebiao);
//		wangluoxiyi_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, wangluoxieyiliebiao);
		wangluoxieyi_adapter = ArrayAdapter.createFromResource(this, R.array.NetProtocol, android.R.layout.simple_spinner_item);
		gongzuomoshi_adapter = ArrayAdapter.createFromResource(this, R.array.NetMode, android.R.layout.simple_spinner_item);
		
		shujuwei_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, shujuweiliebiao);
		jiaoyanwei_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, jiaoyanweiliebiao);
		tingzhiwei_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, tingzhiweiliebiao);
		jiamifangshi_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, jiamifangshiliebiao);


		gongzuomoshi_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		botelv_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		wangluoxieyi_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shujuwei_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jiaoyanwei_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tingzhiwei_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jiamifangshi_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//		wangluomoshi.setAdapter(wangluomoshi_adapter);
		botelv.setAdapter(botelv_adapter);
		wangluoxieyi.setAdapter(wangluoxieyi_adapter);
		gongzuomoshi.setAdapter(gongzuomoshi_adapter);
		shujuwei.setAdapter(shujuwei_adapter);
		jiaoyanwei.setAdapter(jiaoyanwei_adapter);
		tingzhiwei.setAdapter(tingzhiwei_adapter);
		jiamifangshi.setAdapter(jiamifangshi_adapter);

		gongzuomoshi.setOnItemSelectedListener(new SpinnerSelectedListener());
		botelv.setOnItemSelectedListener(new SpinnerSelectedListener());
		wangluoxieyi.setOnItemSelectedListener(new SpinnerSelectedListener());
		shujuwei.setOnItemSelectedListener(new SpinnerSelectedListener());
		jiaoyanwei.setOnItemSelectedListener(new SpinnerSelectedListener());
		tingzhiwei.setOnItemSelectedListener(new SpinnerSelectedListener());
		jiamifangshi.setOnItemSelectedListener(new SpinnerSelectedListener());
//		wangluomoshi.setVisibility(View.VISIBLE);
		botelv.setVisibility(View.VISIBLE);
		wangluoxieyi.setVisibility(View.VISIBLE);
		gongzuomoshi.setVisibility(View.VISIBLE);
		shujuwei.setVisibility(View.VISIBLE);
		jiaoyanwei.setVisibility(View.VISIBLE);
		tingzhiwei.setVisibility(View.VISIBLE);
		jiamifangshi.setVisibility(View.VISIBLE);
		

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					localip.setEnabled(false);
					ziwangyanma.setEnabled(false);
					wangguan.setEnabled(false);
					dns.setEnabled(false);
				} else {
					localip.setEnabled(true);
					ziwangyanma.setEnabled(true);
					wangguan.setEnabled(true);
					dns.setEnabled(true);
				}
			}
		});

	}


	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			switch(arg0.getId())
			{
			case R.id.wangluoxieyi:
				if((arg2==1)||(arg2==3))
				{

					remoteip.setEnabled(false);
					remoteport.setEnabled(false);
					localport.setEnabled(true);
				}
				if((arg2==2)||(arg2==4))
				{

					remoteip.setEnabled(true);
					remoteport.setEnabled(true);
					localport.setEnabled(true);
				}
				break;
			case R.id.jiaoyanwei:
				break;
			case R.id.tingzhiwei:
				break;
			case R.id.botelv:

				break;
			case R.id.shujuwei:
				break;
			case R.id.gongzuomoshi:
				if(arg2==1)
				{

					wangluomingcheng.setEnabled(false);
					mima.setEnabled(false);
					jiamifangshi.setEnabled(false);
				}
				if((arg2==2)||(arg2==3))
				{

					wangluomingcheng.setEnabled(true);
					mima.setEnabled(true);
					jiamifangshi.setEnabled(true);
					if(arg2==2)
					{
						cb.setEnabled(true);
						localip.setEnabled(true);
						ziwangyanma.setEnabled(true);
						wangguan.setEnabled(true);
						dns.setEnabled(true);
					}
					if(arg2==3)
					{
//						cb.setSelected(true);
						cb.setChecked(false);
						cb.setEnabled(false);
						localip.setEnabled(true);
						ziwangyanma.setEnabled(true);
						wangguan.setEnabled(false);
						dns.setEnabled(false);
					}
				}
				break;
			case R.id.jiamifangshi:
				if(gongzuomoshi.getSelectedItemPosition()==2)
				{
					if(jiamifangshi.getSelectedItemPosition()==1)
					{
						
					}
					if(jiamifangshi.getSelectedItemPosition()==2)
					{
						
					}
					if(jiamifangshi.getSelectedItemPosition()==3)
					{
						
					}
					if(jiamifangshi.getSelectedItemPosition()==4)
					{
						
						jiamifangshi.setSelection(5);
					}
					if(jiamifangshi.getSelectedItemPosition()==5)
					{
						
					}
					if(jiamifangshi.getSelectedItemPosition()==6)
					{
						
						jiamifangshi.setSelection(7);
					}
					if(jiamifangshi.getSelectedItemPosition()==7)
					{
						
					}
					if(jiamifangshi.getSelectedItemPosition()==8)
					{
						
						jiamifangshi.setSelection(9);
					}
					if(jiamifangshi.getSelectedItemPosition()==9)
					{
						
					}
					if(jiamifangshi.getSelectedItemPosition()==10)
					{
						jiamifangshi.setSelection(1);
					}
				}
				if(gongzuomoshi.getSelectedItemPosition()==3)
				{
					if((jiamifangshi.getSelectedItemPosition()==1)|| 
							(jiamifangshi.getSelectedItemPosition()==2)||
							(jiamifangshi.getSelectedItemPosition()==3)||
							(jiamifangshi.getSelectedItemPosition()==6)||
							(jiamifangshi.getSelectedItemPosition()==7)||
							(jiamifangshi.getSelectedItemPosition()==10))
					{
						jiamifangshi.setSelection(7);
					}
					if((jiamifangshi.getSelectedItemPosition()==4)||(jiamifangshi.getSelectedItemPosition()==5))
					{
						jiamifangshi.setSelection(5);
					}
					if((jiamifangshi.getSelectedItemPosition()==8)||(jiamifangshi.getSelectedItemPosition()==9))
					{
						jiamifangshi.setSelection(9);
					}
				}
				break;
			default:
				break;
			}
//			System.out.println(arg0);
			String str=arg0.getItemAtPosition(arg2).toString();

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch(v.getId())
		{
		case R.id.bnt_edit_device_paramter_quit:
			String sWangluoxieyi= null;
			String sBotelv= null;
			String sShujuwei= null;
			String sTingzhiwei= null;
			String sRemoteip= null;
			String sRemoteport= null;
			String sLocalport= null;
			String sGongzuomoshi1 = null;
			String sGongzuomoshi2 = null;
			String sWangluomingcheng= null;
			String sMima= null;
			String sWangluomingchengLength= null;
			String sMimaLength= null;
			String sSAM = null;
			String sLocalIP= null;
			String sZiwangyanma= null;
			String sWangguan= null;
			String sDNS= null;
			String sDHCP= null;
			
			int i;
			String sJiaoyanwei;
			
			System.out.println(botelv.getSelectedItem());
			System.out.println(botelv.getSelectedItemId());
			System.out.println(botelv.getSelectedItemPosition());
			

			
//			sGongzuomoshi="hlkATat+wm="+Integer.toString( gongzuomoshi.get);
			
			if(gongzuomoshi.getSelectedItemPosition()==1)
			{

				sGongzuomoshi1="hlkATat+WM=0"+"\r";
				sGongzuomoshi2="hlkATat+WA=0"+"\r";
				
				
				sWangluomingcheng="hlkATat+Sssid="+wangluomingcheng.getText().toString().trim()+"\r";
				sWangluomingchengLength="hlkATat+Sssidl="+Integer.toString( wangluomingcheng.getText().toString().trim().length())+"\r";
				sMima="hlkATat+Spw="+mima.getText().toString().trim()+"\r";
				sMimaLength="hlkATat+Spwl="+Integer.toString( mima.getText().toString().trim().length() )+"\r";			
				sDHCP="hlkATat+dhcp=0"+"\r";
				sLocalIP="hlkATat+ip="+localip.getText().toString().trim()+"\r";
				sZiwangyanma="hlkATat+mask="+ziwangyanma.getText().toString().trim()+"\r";		
				sWangguan="hlkATat+gw="+wangguan.getText().toString().trim()+"\r";		
				sDNS="hlkATat+dns="+dns.getText().toString().trim()+"\r";
				sWangluoxieyi="hlkATat+UType="+Integer.toString(  wangluoxieyi.getSelectedItemPosition()  )+"\r\n";
				sRemoteip="hlkATat+UIp="+remoteip.getText()+"\r";				
				sRemoteport="hlkATat+URPort="+remoteport.getText()+"\r\n";				
				sLocalport="hlkATat+ULPort="+localport.getText()+"\r\n";				
				sBotelv="hlkATat+Ub="+(String) botelv.getSelectedItem()+"\r\n";				
				sShujuwei="hlkATat+Ud="+(String) shujuwei.getSelectedItem()+"\r\n";			
				sTingzhiwei="hlkATat+Us="+(String) tingzhiwei.getSelectedItem()+"\r\n";
				sJiaoyanwei="hlkATat+Up="+Integer.toString(  jiaoyanwei.getSelectedItemPosition()-1  )+"\r\n";			

				try {
					m30listitemdetail.M30Brocast(addrIP,sGongzuomoshi1);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sGongzuomoshi2);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluomingcheng);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluomingchengLength);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sSAM);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sMima);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sMimaLength);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sDHCP);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sLocalIP);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sZiwangyanma);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangguan);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sDNS);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluoxieyi);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sRemoteip);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sRemoteport);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sLocalport);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sBotelv);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sShujuwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sTingzhiwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sJiaoyanwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,"hlkATat+Rb=1\r\n");Thread.sleep(50);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			if(gongzuomoshi.getSelectedItemPosition()==2)
			{

				sGongzuomoshi1="hlkATat+WM=2"+"\r";
				sGongzuomoshi2="hlkATat+WA=0"+"\r";
				if(jiamifangshi.getSelectedItemPosition()==1)
				{
					sSAM="hlkATat+SAM=0"+"\r";
				}
				if(jiamifangshi.getSelectedItemPosition()==2)
				{
					sSAM="hlkATat+SAM=2"+"\r";
				}
				if(jiamifangshi.getSelectedItemPosition()==3)
				{
					sSAM="hlkATat+SAM=1"+"\r";
				}
				if(jiamifangshi.getSelectedItemPosition()==4)
				{
					sSAM="hlkATat+SAM=4"+"\r";
					jiamifangshi.setSelection(5);
				}
				if(jiamifangshi.getSelectedItemPosition()==5)
				{
					sSAM="hlkATat+SAM=4"+"\r";
				}
				if(jiamifangshi.getSelectedItemPosition()==6)
				{
					sSAM="hlkATat+SAM=7"+"\r";
					jiamifangshi.setSelection(7);
				}
				if(jiamifangshi.getSelectedItemPosition()==7)
				{
					sSAM="hlkATat+SAM=7"+"\r";
				}
				if(jiamifangshi.getSelectedItemPosition()==8)
				{
					sSAM="hlkATat+SAM=9"+"\r";
					jiamifangshi.setSelection(9);
				}
				if(jiamifangshi.getSelectedItemPosition()==9)
				{
					sSAM="hlkATat+SAM=9"+"\r";
				}
				if(jiamifangshi.getSelectedItemPosition()==10)
				{
					sSAM="hlkATat+SAM=0"+"\r";
				}

				sWangluomingcheng="hlkATat+Sssid="+wangluomingcheng.getText().toString().trim()+"\r";
				sWangluomingchengLength="hlkATat+Sssidl="+Integer.toString( wangluomingcheng.getText().toString().trim().length() )+"\r";
				sMima="hlkATat+Spw="+mima.getText().toString().trim()+"\r";
				sMimaLength="hlkATat+Spwl="+Integer.toString( mima.getText().toString().trim().length() )+"\r";
				if(cb.isChecked())
				{
					sDHCP="hlkATat+dhcp=1"+"\r";
				}
				else
				{
					sDHCP="hlkATat+dhcp=0"+"\r";
				}
				sLocalIP="hlkATat+ip="+localip.getText().toString().trim()+"\r";
				sZiwangyanma="hlkATat+mask="+ziwangyanma.getText().toString().trim()+"\r";		
				sWangguan="hlkATat+gw="+wangguan.getText().toString().trim()+"\r";		
				sDNS="hlkATat+dns="+dns.getText().toString().trim()+"\r";
				sWangluoxieyi="hlkATat+UType="+Integer.toString(  wangluoxieyi.getSelectedItemPosition()  )+"\r\n";
				sRemoteip="hlkATat+UIp="+remoteip.getText()+"\r";				
				sRemoteport="hlkATat+URPort="+remoteport.getText()+"\r\n";				
				sLocalport="hlkATat+ULPort="+localport.getText()+"\r\n";				
				sBotelv="hlkATat+Ub="+(String) botelv.getSelectedItem()+"\r\n";				
				sShujuwei="hlkATat+Ud="+(String) shujuwei.getSelectedItem()+"\r\n";			
				sTingzhiwei="hlkATat+Us="+(String) tingzhiwei.getSelectedItem()+"\r\n";
				sJiaoyanwei="hlkATat+Up="+Integer.toString(  jiaoyanwei.getSelectedItemPosition()-1  )+"\r\n";			

				try {
					m30listitemdetail.M30Brocast(addrIP,sGongzuomoshi1);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sGongzuomoshi2);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluomingcheng);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluomingchengLength);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sSAM);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sMima);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sMimaLength);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sDHCP);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sLocalIP);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sZiwangyanma);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangguan);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sDNS);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluoxieyi);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sRemoteip);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sRemoteport);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sLocalport);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sBotelv);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sShujuwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sTingzhiwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sJiaoyanwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,"hlkATat+WC=1\r\n");Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,"hlkATat+Rb=1\r\n");Thread.sleep(50);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(gongzuomoshi.getSelectedItemPosition()==3)
			{

				sGongzuomoshi1="hlkATat+WM=0"+"\r";
				sGongzuomoshi2="hlkATat+WA=1"+"\r";
				if((jiamifangshi.getSelectedItemPosition()==1)|| 
						(jiamifangshi.getSelectedItemPosition()==2)||
						(jiamifangshi.getSelectedItemPosition()==3)||
						(jiamifangshi.getSelectedItemPosition()==6)||
						(jiamifangshi.getSelectedItemPosition()==7)||
						(jiamifangshi.getSelectedItemPosition()==10))
				{
					sSAM="hlkATat+Aam=7"+"\r";
					jiamifangshi.setSelection(7);
				}
				if((jiamifangshi.getSelectedItemPosition()==4)||(jiamifangshi.getSelectedItemPosition()==5))
				{
					sSAM="hlkATat+Aam=4"+"\r";
					jiamifangshi.setSelection(5);
				}
				if((jiamifangshi.getSelectedItemPosition()==8)||(jiamifangshi.getSelectedItemPosition()==9))
				{
					sSAM="hlkATat+Aam=9"+"\r";
					jiamifangshi.setSelection(9);
				}
				
				sWangluomingcheng="hlkATat+Assid="+wangluomingcheng.getText().toString().trim()+"\r";
				sWangluomingchengLength="hlkATat+Assidl="+Integer.toString( wangluomingcheng.getText().toString().trim().length() )+"\r";
//				System.out.println(sWangluomingcheng);
//				System.out.println(sWangluomingchengLength);		
				sMima="hlkATat+Apw="+mima.getText().toString().trim()+"\r";
				sMimaLength="hlkATat+Apwl="+Integer.toString( mima.getText().toString().trim().length())+"\r";
				sDHCP="hlkATat+dhcp=0"+"\r";
				sLocalIP="hlkATat+Aip="+localip.getText().toString().trim()+"\r";
				sZiwangyanma="hlkATat+mask="+ziwangyanma.getText().toString().trim()+"\r";		
				sWangguan="hlkATat+gw="+localip.getText().toString().trim()+"\r";		
				sDNS="hlkATat+dns="+localip.getText().toString().trim()+"\r";
				sWangluoxieyi="hlkATat+UType="+Integer.toString(  wangluoxieyi.getSelectedItemPosition()  )+"\r\n";
				sRemoteip="hlkATat+UIp="+remoteip.getText()+"\r";				
				sRemoteport="hlkATat+URPort="+remoteport.getText()+"\r\n";				
				sLocalport="hlkATat+ULPort="+localport.getText()+"\r\n";				
				sBotelv="hlkATat+Ub="+(String) botelv.getSelectedItem()+"\r\n";				
				sShujuwei="hlkATat+Ud="+(String) shujuwei.getSelectedItem()+"\r\n";			
				sTingzhiwei="hlkATat+Us="+(String) tingzhiwei.getSelectedItem()+"\r\n";
				sJiaoyanwei="hlkATat+Up="+Integer.toString(  jiaoyanwei.getSelectedItemPosition()-1  )+"\r\n";
				try {
//					m30listitemdetail.M30Brocast(addrIP,sGongzuomoshi1);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sGongzuomoshi2);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluomingcheng);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluomingchengLength);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sSAM);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sMima);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sMimaLength);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sDHCP);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sLocalIP);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sZiwangyanma);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangguan);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sDNS);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sWangluoxieyi);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sRemoteip);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sRemoteport);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sLocalport);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sBotelv);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sShujuwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sTingzhiwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,sJiaoyanwei);Thread.sleep(50);
					m30listitemdetail.M30Brocast(addrIP,"hlkATat+Rb=1\r\n");Thread.sleep(50);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			
			
//			if(jiamifangshi.getSelectedItemPosition()==2)
//			{
//				sSAM="hlkATat+SAM=7"+"\r";
//			}
//			if(jiamifangshi.getSelectedItemPosition()==3)
//			{
//				sSAM="hlkATat+SAM=8"+"\r";
//			}
			
			
			
			

			
			
			break;
		case R.id.bnt_edit_device_paramter_check:

			
			byte[] DataReceive= new byte[512];
			DatagramPacket pack = new DatagramPacket(DataReceive,DataReceive.length);
	        MulticastSocket ms=null;
	        
//			String Ub=m30listitemdetail.M30Brocast_check(addrIP,"hlkATat+Ub=?");
	        try {
				ms = new MulticastSocket();
				InetAddress address = InetAddress.getByName(addrIP);
				
				byte out[] = "hlkATat+Ub=?".getBytes();  
				DatagramPacket dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
				ms.setSoTimeout(3000);
				ms.receive(pack);
				String Ub = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(Ub!=null)
				{
					Log.i(TAG,Ub);
					if(Ub.equals("115200"))botelv.setSelection(1);
					else if(Ub.equals("57600"))botelv.setSelection(2);
					else if(Ub.equals("38400"))botelv.setSelection(3);
					else if(Ub.equals("19200"))botelv.setSelection(4);
					else if(Ub.equals("9600"))botelv.setSelection(5);
					else if(Ub.equals("4800"))botelv.setSelection(6);
					else if(Ub.equals("2400"))botelv.setSelection(7);
					else if(Ub.equals("1200"))botelv.setSelection(8);
				}
				else
				{
					botelv.setSelection(0);
				}
				
				out= "hlkATat+UType=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String UType = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(UType!=null)
					{
						Log.i(TAG,UType);
						if(UType.equals("1"))wangluoxieyi.setSelection(1);
						else if(UType.equals("2"))wangluoxieyi.setSelection(2);
						else if(UType.equals("3"))wangluoxieyi.setSelection(3);
						else if(UType.equals("4"))wangluoxieyi.setSelection(4);
					}
					else
					{
						wangluoxieyi.setSelection(0);
					}
					
				

				out= "hlkATat+Ud=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String Ud = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(Ud!=null)
				{
					Log.i(TAG,Ud);
					if(Ud.equals("8"))shujuwei.setSelection(1);
					else if(Ud.equals("7"))shujuwei.setSelection(2);
					else if(Ud.equals("6"))shujuwei.setSelection(3);
					else if(Ud.equals("5"))shujuwei.setSelection(4);
				}
				else
				{
					shujuwei.setSelection(0);
				}
//				

				out= "hlkATat+Us=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String Us = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(Us!=null)
				{
					Log.i(TAG,Us);
					if(Us.equals("1"))tingzhiwei.setSelection(1);
					else if(Us.equals("2"))tingzhiwei.setSelection(2);
					
				}
				else
				{
					tingzhiwei.setSelection(0);
				}
//				

				out= "hlkATat+Up=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String Up = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(Up!=null)
				{
					Log.i(TAG,Up);
					if(Up.equals("0"))jiaoyanwei.setSelection(1);
					else if(Up.equals("1"))jiaoyanwei.setSelection(2);
					else if(Up.equals("2"))jiaoyanwei.setSelection(3);
				}
				else
				{
					jiaoyanwei.setSelection(0);
				}
//				

				out= "hlkATat+UIp=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String UIp = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(UIp!=null)
				{
					Log.i(TAG,UIp);
					remoteip.setText(UIp);
				}
				else
				{
					remoteip.setText("");
				}
//				

				out= "hlkATat+URPort=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String URPort = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(URPort!=null)
				{
					Log.i(TAG,URPort);
					remoteport.setText(URPort);
				}
				else
				{
					remoteport.setText("");
				}
//				
//				

				out= "hlkATat+ULPort=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String ULPort = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(ULPort!=null)
				{
					Log.i(TAG,ULPort);
					localport.setText(ULPort);
				}
				else
				{
					localport.setText("");
				}
				
				out= "hlkATat+mask=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String mask = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(mask!=null)
				{
					ziwangyanma.setText(mask);
				}
				
				out= "hlkATat+gw=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String gw = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(gw!=null)
				{
					wangguan.setText(gw);
				}
				
				out= "hlkATat+dns=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String dns1 = new String(pack.getData(), pack.getOffset(), pack.getLength());
				if(dns1!=null)
				{
					dns.setText(dns1);
				}
				
				
				out= "hlkATat+WA=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String WA = new String(pack.getData(), pack.getOffset(), pack.getLength());
				
				out= "hlkATat+WM=?".getBytes();  
				dataPacket = new DatagramPacket(out, out.length, address, 988);  			
				ms.send(dataPacket);
//				ms.setSoTimeout(2000);
				ms.receive(pack);
				String WM = new String(pack.getData(), pack.getOffset(), pack.getLength());
				
				
				
				if(WA!=null)
					{
						Log.i(TAG,WA);
						Log.i(TAG,WM);
						if(WM.equals("0")&&WA.equals("0"))
							{

								gongzuomoshi.setSelection(1);
								
								out= "hlkATat+Sam=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String SAM = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(SAM!=null)
								{

									if(SAM.equals("0"))jiamifangshi.setSelection(1);
									if(SAM.equals("2"))jiamifangshi.setSelection(2);
									if(SAM.equals("1"))jiamifangshi.setSelection(3);
									if(SAM.equals("4"))jiamifangshi.setSelection(4);
									if(SAM.equals("5"))jiamifangshi.setSelection(5);
									if(SAM.equals("9"))jiamifangshi.setSelection(9);	
								}
								
								out= "hlkATat+Sssid=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Assid = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Assid!=null)
								{
									wangluomingcheng.setText(Assid);
								}
								
								out= "hlkATat+Spw=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Apw = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Apw!=null)
								{
									mima.setText(Apw);
								}
								
								
								out= "hlkATat+ip=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Aip = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Aip!=null)
								{
									localip.setText(Aip);
								}
								
							}
						else if(WM.equals("2")&&WA.equals("0"))
							{

								out= "hlkATat+Sam=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String SAM = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(SAM!=null)
								{

									if(SAM.equals("0"))jiamifangshi.setSelection(1);
									if(SAM.equals("2"))jiamifangshi.setSelection(2);
									if(SAM.equals("1"))jiamifangshi.setSelection(3);
									if(SAM.equals("4"))jiamifangshi.setSelection(4);
									if(SAM.equals("5"))jiamifangshi.setSelection(5);
									if(SAM.equals("9"))jiamifangshi.setSelection(9);	
								}
								cb.setEnabled(true);
								cb.setChecked(false);
								gongzuomoshi.setSelection(2);
								
								out= "hlkATat+Sssid=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Assid = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Assid!=null)
								{
									wangluomingcheng.setText(Assid);
								}
								
								out= "hlkATat+Spw=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Apw = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Apw!=null)
								{
									mima.setText(Apw);
								}
								
								
								out= "hlkATat+ip=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Aip = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Aip!=null)
								{
									localip.setText(Aip);
								}
							}
//						else if( WM.equals("0")&&WA.equals("1") )
						else if(WA.equals("1") )
							{

								out= "hlkATat+Aam=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Aam = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Aam!=null)
								{
									if(Aam.equals("4"))jiamifangshi.setSelection(5);
									if(Aam.equals("7"))jiamifangshi.setSelection(7);
									if(Aam.equals("9"))jiamifangshi.setSelection(9);
								}
								cb.setChecked(false);
								cb.setEnabled(false);
								gongzuomoshi.setSelection(3);
								
								
								out= "hlkATat+Assid=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Assid = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Assid!=null)
								{
									wangluomingcheng.setText(Assid);
								}
								
								out= "hlkATat+Apw=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Apw = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Apw!=null)
								{
									mima.setText(Apw);
								}
								
								
								out= "hlkATat+Aip=?".getBytes();  
								dataPacket = new DatagramPacket(out, out.length, address, 988);  			
								ms.send(dataPacket);
//								ms.setSoTimeout(2000);
								ms.receive(pack);
								String Aip = new String(pack.getData(), pack.getOffset(), pack.getLength());
								if(Aip!=null)
								{
									localip.setText(Aip);
								}
								
								
							}
						
					}

				ms.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			String UType=m30listitemdetail.M30Brocast_check(addrIP,"hlkATat+UType=?");
//			if(UType!=null)
//			{
//				Log.i(TAG,UType);
//				if(UType.equals("1"))wangluoxieyi.setSelection(1);
//				else if(UType.equals("2"))wangluoxieyi.setSelection(2);
//				else if(UType.equals("3"))wangluoxieyi.setSelection(3);
//				else if(UType.equals("4"))wangluoxieyi.setSelection(4);
//			}
//			else
//			{
//				wangluoxieyi.setSelection(0);
//			}
//			
//			String Ud=m30listitemdetail.M30Brocast_check(addrIP,"hlkATat+Ud=?");
//			if(Ud!=null)
//			{
//				Log.i(TAG,Ud);
//				if(Ud.equals("8"))shujuwei.setSelection(1);
//				else if(Ud.equals("7"))shujuwei.setSelection(2);
//				else if(Ud.equals("6"))shujuwei.setSelection(3);
//				else if(Ud.equals("5"))shujuwei.setSelection(4);
//			}
//			else
//			{
//				shujuwei.setSelection(0);
//			}
//			
//			String Us=m30listitemdetail.M30Brocast_check(addrIP,"hlkATat+Us=?");
//			if(Us!=null)
//			{
//				Log.i(TAG,Us);
//				if(Us.equals("1"))tingzhiwei.setSelection(1);
//				else if(Us.equals("2"))tingzhiwei.setSelection(2);
//				
//			}
//			else
//			{
//				tingzhiwei.setSelection(0);
//			}
//			
//			String Up=m30listitemdetail.M30Brocast_check(addrIP,"hlkATat+Up=?");
//			if(Up!=null)
//			{
//				Log.i(TAG,Up);
//				if(Up.equals("0"))jiaoyanwei.setSelection(1);
//				else if(Up.equals("1"))jiaoyanwei.setSelection(2);
//				else if(Up.equals("2"))jiaoyanwei.setSelection(3);
//			}
//			else
//			{
//				jiaoyanwei.setSelection(0);
//			}
//			
//			String UIp=m30listitemdetail.M30Brocast_check(addrIP,"hlkATat+UIp=?");
//			if(UIp!=null)
//			{
//				Log.i(TAG,UIp);
//				remoteip.setText(UIp);
//			}
//			else
//			{
//				remoteip.setText("");
//			}
//			
//			String URPort=m30listitemdetail.M30Brocast_check(addrIP,"hlkATat+URPort=?");
//			if(URPort!=null)
//			{
//				Log.i(TAG,URPort);
//				remoteport.setText(URPort);
//			}
//			else
//			{
//				remoteport.setText("");
//			}
//			
//			
//			String ULPort=m30listitemdetail.M30Brocast_check(addrIP,"hlkATat+ULPort=?");
//			if(ULPort!=null)
//			{
//				Log.i(TAG,ULPort);
//				localport.setText(ULPort);
//			}
//			else
//			{
//				localport.setText("");
//			}
			
//			botelv.setEmptyView(new a);
//			botelv.setVisibility(View.INVISIBLE);
			
			break;
			default:
				break;
		
		}
		
	}

}
