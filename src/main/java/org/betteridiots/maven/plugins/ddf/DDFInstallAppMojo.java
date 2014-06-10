package org.betteridiots.maven.plugins.ddf;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.commons.io.FileUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

@Mojo( name = "install-app", defaultPhase = LifecyclePhase.INTEGRATION_TEST )
public class DDFInstallAppMojo extends AbstractMojo
{

    // Maven Project
    /**
     * @parameter default-value="${project}" doesn't work
     * @required
     * @readonly
     */
    @Parameter( property = "project")
    MavenProject project;

    // Array of parameters to be inserted into ddf config command
    @Parameter( property = "install-app-ddf.apps")
    private String apps;

    // DDF Home
    @Parameter( property = "install-app-ddf.deploy", defaultValue = "/opt/dib/" )
    private String deploy;

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
        getLog().info( "DDF Apps: " );
	String[] apps2=apps.split(new String(":"));
        for (String app : apps2) {
            getLog().info( app );
        }

        ////////////////////////////////////////////////////////
	Set<Artifact> artifacts = project.getDependencyArtifacts();

        for (Artifact artifact : artifacts) {

	    Boolean pass=false;
            File dest = new File(deploy);
            try {
		for (String app : apps2) {
                    if (artifact.getArtifactId().contains(app)) {
                        getLog().info( "cp " + artifact.getFile() +" " + deploy);
	                FileUtils.copyFileToDirectory(artifact.getFile(), dest);

                        try {
		            int count=0;
			    while(count<10){
				    Thread.sleep(1000); // Sleep 1 Second
				    if (checkStatus(artifact)) {
                                            pass = true;
					    break;
				    }
				    count++;
			    }
                        } catch (InterruptedException e) {
                            getLog().info( "Caught: InteruptException: " + e.toString() );
                        }
			if (!pass){
				getLog().error("Unable to install "+ artifact.getArtifactId());
			}
                    }
                }
            } catch (IOException e) {
                getLog().info( "Caught: IOException: " + e.toString() );
            }
        } 
        getLog().debug( "DONE......\n\n" );

    }

    public Boolean checkStatus(Artifact artifact) throws MojoExecutionException, MojoFailureException {

        // Disable strict host key checking and set auth method to password
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications","password");

	String command = "app:list |grep "+ artifact.getArtifactId();
        Boolean status = false;

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
		((ChannelExec)channel).setCommand( command );

		channel.setInputStream(null);

		((ChannelExec)channel).setErrStream(System.err);

		InputStream in=channel.getInputStream();

		channel.connect();

		OutputStream out=channel.getOutputStream();

		byte[] tmp=new byte[1024];
		while(true){
			while(in.available()>0){
				int i=in.read(tmp, 0, 1024);
				if(i<0)break;
				String appStatus = new String(tmp, 0, i-1); // -1 to chop newline from STDOUT
				getLog().info(appStatus);
				if ((appStatus.contains("ACTIVE")) &&
                                    (!appStatus.contains("INACTIVE"))) {
					status = true;
				}
			}
			if(channel.isClosed()){
				getLog().debug("exit-status: "+channel.getExitStatus());
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
	return status;
    }
}
