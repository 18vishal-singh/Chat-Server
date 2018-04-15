import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

class Login
{
	JFrame f;
	JButton b;
	JTextField tf;
	JLabel jl;
	String str="";
	String hostname;
	Login()
	{
		try
		{
			InetAddress addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		}		
		catch(Exception ex)
		{
			hostname="HERE";
		}
		b=new JButton("login");
		f=new JFrame("LOGIN");
		tf=new JTextField(hostname,20);
		jl=new JLabel("Enter Your Name");
		f.add(jl);
		f.add(tf);
		f.add(b);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==b)
				{
					if(tf.getText().equals("HERE")||tf.getText().equals(""))
						str="anonymous"; 
					else
						str=tf.getText();
					tf.setEditable(false);
					f.setVisible(false);
					new ChatBox(str);
					
				}
				
			}
		});
		f.setLayout(new FlowLayout());
		f.setSize(250,150);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setLocation(300,200);
	}
	public static void main(String s[])
	{
		new Login();
	}
}




class ChatBox
{
	JFrame f;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem1,menuItem2;
	static JTextArea ta1,ta2,ta3;
	JPanel jp1,jp2,jp11,jp12,jp13,jp21,jp22,jp23;
	JButton b1,b2,b3;
	JLabel l1,l2;
	static int flag=1;

	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	String str="";

	ChatBox(String strr)
	{
		this.str=strr;
		f=new JFrame("Chat Box");
		System.out.println(str);
		menuBar = new JMenuBar();
		menu = new JMenu("About");
		menu.setMnemonic(KeyEvent.VK_N);
		menuBar.add(menu);
		menuItem1 = new JMenuItem("Developer",KeyEvent.VK_T);
		menuItem1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null,"This software is totally developed in java.\n             Made by VISHAL SINGH.");
			}
		});
		menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menu.add(menuItem1);
		menuItem2 = new JMenuItem("Instruction",KeyEvent.VK_T);
		menuItem2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null,"This is a trial software.Update will\nbe soon coming. Just click on connect\nbutton and start group chat.\n\nNOTE: Active user area is disabled.");
			}
		});
		menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menu.add(menuItem2);
		
		ta1=new JTextArea("",22,35);
		ta1.setEditable(false);
		ta2=new JTextArea("your message", 8, 35);
		ta3=new JTextArea("Activ users \n \n UPDATE \n WILL BE COMMING \n SOON", 29, 12);
		ta3.setEditable(false);
		b1=new JButton("send");
		b1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String s1;
					s1=ta2.getText();
					dout.writeUTF(str+"  :->  "+s1);
					dout.flush();
					ta2.setText("");
					
				}
				catch(Exception ex)
				{
					ta1.append("\n"+"SORRY...... PLEASE CONNECT AGAIN THEN SEND AGAIN"); 
					flag=1;
				}
				
			}
		});
		b2=new JButton("exit group");
		b2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String st;
					st=str+" ........leaved the group........";
					dout.writeUTF(st);
					dout.flush();
					System.exit(0);
					
				}
				catch(Exception ex)
				{
					ta2.setText("*****YOU ARE NOT YET CONNECTED*****");
					flag=1;
				}
				
			}
		});
		b3=new JButton("connect");
		b3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					if(flag==1)
					{
						flag=0;
						ta2.setText("");
						s=new Socket("localhost",10);
						System.out.println(s);
						din=new DataInputStream(s.getInputStream());
						dout=new DataOutputStream(s.getOutputStream());
						String st;
						st=str+" **********joined the group**********";
						dout.writeUTF(st);
						dout.flush();
						My m=new My(din);
						Thread t1=new Thread(m);
						t1.start();
						
						
					}
					else
						ta2.setText("*****YOU ARE ALREADY CONNECTED*****");
					
		   		}
				catch(Exception ex)
				{
					ta1.append("\n"+"SERVER DOWN");
					flag=1;
				}
				
			}
		});
		l1=new JLabel("active users ");
		l2=new JLabel("hello "+str);
	
		jp1=new JPanel();
		jp1.setLayout(new BorderLayout());
		jp11=new JPanel();
		jp12=new JPanel();
		jp13=new JPanel();
		jp2=new JPanel();
		jp2.setLayout(new BorderLayout());
		jp21=new JPanel();
		jp22=new JPanel();
		jp23=new JPanel();

		jp1.add(jp11,"North");
		jp1.add(jp12,"Center");
		jp1.add(jp13,"South");
		jp2.add(jp21,"North");
		jp2.add(jp22,"Center");
		jp2.add(jp23,"South");
		f.getContentPane().add(jp1,"Center");
		f.getContentPane().add(jp2,"East");
		jp11.add(new JScrollPane(ta1),"North");
		jp12.add(new JScrollPane(ta2),"Center");
		jp13.add(b1);
		jp13.add(b2);
		jp13.add(b3);
		jp21.add(l1);
		jp22.add(new JScrollPane(ta3));
		jp23.add(l2);
				
		f.setLocation(280,80);
		f.pack();
		f.setResizable(false);
		f.setJMenuBar(menuBar);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		JOptionPane.showMessageDialog(null,"hello : "+str+"\nThis Software is under development.\nNew update is going to launch soon.");
		
	}	
	
}

class My implements Runnable
{
	DataInputStream din;
	My(DataInputStream din)
	{
		this.din=din;
	}
	public void run()
	{
		String s2="";
		try
		{
			while(true)
			{
				s2=din.readUTF();
				ChatBox.ta1.append("\n"+s2);
				System.out.println(s2);
			}
		}
		catch(Exception ex)
		{
			ChatBox.flag=1;
		}
	}
}


