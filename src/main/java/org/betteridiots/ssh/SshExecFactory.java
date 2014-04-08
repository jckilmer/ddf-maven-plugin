package org.betteridiots.ssh;

import com.jcraft.jsch.*;

import java.io.InputStream;
import java.util.Properties;


public class SshExecFactory {

    public void buildChannel(String user, String password, String host, int port, Properties config, String cmd)
    {
        try
        {
            JSch jsch=new JSch();

            Session session=jsch.getSession( user, host, port);
            session.setPassword(password);
            session.setConfig(config);


            // Connect and open an exec channel
            session.connect();
            Channel channel=session.openChannel("exec");

            // Pass in commands from array
            ((ChannelExec)channel).setCommand( cmd );

            channel.setInputStream(null);

            ((ChannelExec)channel).setErrStream(System.err);

            InputStream in=channel.getInputStream();

            channel.connect();

            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
            session.disconnect();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
