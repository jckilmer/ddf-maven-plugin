package org.betteridiots.maven.plugins.ddf;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.betteridiots.ssh.SshExecFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * The config-ddf goal is used for passing configuration options into the ddf kernel
 */
@Mojo( name = "config", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST )
public class DDFConfigureMojo extends AbstractMojo
{

    /**
     * File containing DDF commands (optional)
     */
    @Parameter( property = "config-ddf.paramsFile")
    private String paramsFile;

    /**
     * Username credentials for accessing ddf
     */
    @Parameter( property = "config-ddf.user", defaultValue = "admin" )
    private String user;

    /**
     * Password for accessing the ddf
     */
    @Parameter( property = "config-ddf.password", defaultValue = "admin" )
    private String password;

    /**
     * Hostname or IP address of the ddf
     */
    @Parameter( property = "config-ddf.host", defaultValue = "localhost" )
    private String host;

    /**
     * SSH Port for the ddf
     */
    @Parameter( property = "config-ddf.port", defaultValue = "8101" )
    private int port;

    /**
     * Config file to be edited
     */
    @Parameter( property = "config-ddf.config" )
    private String config;

    /**
     * Array of properties to set
     */
    @Parameter( property = "config-ddf.props" )
    private String[] props;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // Log Parameters
        getLog().info( "DDF Host is: " + host );
        getLog().info( "DDF Port is: " + port );
        getLog().info( "DDF user is: " + user );
        getLog().info( "DDF password is: " + password );
        getLog().info( "DDF parameter file is: " + paramsFile );

        // Initialize BufferedReader and commands ArrayList
        StringBuffer params = new StringBuffer();
        String line;
        
        if (paramsFile != null) {
            
            // Initialize FileReader
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(paramsFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(fileReader);


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
	}else{

            params.append("config:edit " + config + "; ");

	    for (String prop : props){
                params.append("config:propset " + prop +"; ");
            }
        }

        params.append("config:update;");

        // Disable strict host key checking and set auth method to password
        java.util.Properties sshConfig = new java.util.Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshConfig.put("PreferredAuthentications","password");

        // Build JSch Session

        SshExecFactory sef = new SshExecFactory();

        String commands = params.toString();

        sef.buildChannel(user, password, host, port, sshConfig, commands);
        
    }

}
