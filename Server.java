import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server
{
	ArrayList al=new ArrayList();
	ServerSocket ss;
	Socket s;
	static int count=0;
	static String hn="";
	static String hn1="";
	public Server()
	{
		try{
			ServerRecord.ta.setText("SERVER STARTED");
			System.out.println("Server started");
			ss=new ServerSocket(10);
			while(true)
			{
				s=ss.accept();
				al.add(s);
				ServerRecord.ta.append("\n"+s);
				count++;
				ServerRecord.ta1.setText("Number of present users (connected) : "+count);
				System.out.println("client connected");
				hn = s.getInetAddress().getLocalHost().getHostName();			//**********
				hn1 = s.getInetAddress().getHostAddress();				//**********
				ServerRecord.ta2.append("\n"+"\n>"+hn+"\n"+"         "+hn1);
				Runnable r=new MyThread(s,al,hn,hn1);
				Thread t=new Thread(r);
				t.start();
			}
		    }
		catch(Exception e){}
	}
	public static void main(String a[])
	{
		new ServerRecord();
		new Server();
	}
}


class MyThread implements Runnable
{
	Socket s;
	ArrayList al;
	DataInputStream din;
	DataOutputStream dout;
	static String hn;
	static String hn1;
	MyThread(Socket s,ArrayList al,String hn,String hn1)
	{
		this.s=s;
		this.al=al;
		this.hn=hn;
		this.hn1=hn1;
	}
	public void run()
	{
		String s1;
		try
		{
			din=new DataInputStream(s.getInputStream());
			while(true)
			{
				s1=din.readUTF();
				System.out.println(s1);
				ServerRecord.ta.append("\n"+s1);
				tellEveryOne(s1);

			}
		}
		catch(Exception e){}
		Server.count--;
		ServerRecord.ta1.setText("Number of present users (connected) : "+Server.count);
		String st=ServerRecord.ta2.getText();
		String st1="\n"+"\n>"+hn+"\n"+"         "+hn1;
	//	ServerRecord.ta.append("\n....."+st+"\n....."+st1);
		String nst=st.replace(st1,"");
		ServerRecord.ta2.setText(nst);
	//	ServerRecord.ta.append("\n....."+nst);
		
	}
	public void tellEveryOne(String s1)
	{
		Iterator i=al.iterator();
		while(i.hasNext())
		{
			try{
				Socket sc=(Socket)i.next();
				dout=new DataOutputStream(sc.getOutputStream());
				dout.writeUTF(s1);
				dout.flush();
				
				System.out.println("client");
			   }
			catch(Exception e){}
		}
	}
}



class ServerRecord
{
	JFrame f;
	static JTextArea ta,ta1,ta2;
	JPanel jp,jp1,jp2,jp3,jp4;
	JButton b;
	JLabel l;
	ServerRecord()
	{
		f=new JFrame("server");
		ta=new JTextArea("",32,35);
		ta.setEditable(false);
		ta1=new JTextArea("Number of present users (connected) : 0");
		ta1.setEditable(false);
		ta2=new JTextArea("ACTIVE USERS",32,10);
		ta2.setEditable(false);
		b=new JButton("exit server");
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);	
			}
		});
		
		l=new JLabel("SERVER AREA : HELLO VISHAL SINGH ");
		jp=new JPanel();
		jp.setLayout(new BorderLayout());
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp4=new JPanel();
		jp3.setLayout(new BorderLayout());
		jp.add(jp1,"North");
		jp.add(jp2,"Center");
		jp.add(jp3,"South");
		jp.add(jp4,"East");
		f.getContentPane().add(jp,"Center");
		
		jp1.add(l);
		jp2.add(new JScrollPane(ta));
		jp3.add(b,"East");
		jp3.add(ta1);
		jp4.add(new JScrollPane(ta2));		

		f.setLocation(280,50);
		f.pack();
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}