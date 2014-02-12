package org.betteridiots.maven.plugins.ddf;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;


@Mojo( name = "config-ddf", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST )
public class DDFClient extends AbstractMojo
{
    
    // File containing DDF commands
    @Parameter( property = "config-ddf.paramsFile")
    private String paramsFile;

    // DDF User
    @Parameter( property = "config-ddf.user", defaultValue = "admin" )
    private String user;
   
    // DDF Password
    @Parameter( property = "config-ddf.password", defaultValue = "admin" )
    private String password;

    // DDF Host
    @Parameter( property = "config-ddf.host", defaultValue = "localhost" )
    private String host;

    // DDF SSH Port
    @Parameter( property = "config-ddf.port", defaultValue = "8101" )
    private int port;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // Log Parameters
        getLog().info( "DDF Host is: " + host );
        getLog().info( "DDF Port is: " + port );
        getLog().info( "DDF user is: " + user );
        getLog().info( "DDF password is: " + password );
        getLog().info( "DDF parameter file is: " + paramsFile );

        // Initialize FileReader
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(paramsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Initialize BufferedReader and commands ArrayList
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer params = new StringBuffer();
        String line;

        // Parse paramsFile into commands single string
        try {
            while ((line = bufferedReader.readLine()) != null) {
                params.append(line +"; ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try
        {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Disable strict host key checking and set auth method to password
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications","password");

        // Build JSch Session
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
            ((ChannelExec)channel).setCommand( params.toString() );

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