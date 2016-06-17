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
import org.betteridiots.ssh.SshExecFactory;

import java.io.*;

/**
 * The install-features goal is used to install features for the ddf
 */
@Mojo( name = "install-features", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST )
public class DDFInstallFeaturesMojo extends AbstractMojo
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
     * Array of features to install
     */
    @Parameter( property = "install-features.features" )
    private String[] features;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // Disable strict host key checking and set auth method to password
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications","password");

        // Log Parameters
        getLog().info( "DDF Host is: " + host );
        getLog().info( "DDF Port is: " + port );
        getLog().info( "DDF user is: " + user );
        getLog().info( "DDF password is: " + password );
        getLog().info( "DDF parameter file is: " + paramsFile );

        if (paramsFile !=null) {
            // Initialize FileReader
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(paramsFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Initialize BufferedReader and commands ArrayList
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            // Parse paramsFile into commands single string
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    // Build JSch Session

                    SshExecFactory sef = new SshExecFactory();

                    sef.buildChannel(user, password, host, port, config, line);
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
        }
        else {
            for (String feature: features) {

                String cmd = "feature:install " + feature;

                SshExecFactory sef = new SshExecFactory();

                sef.buildChannel(user, password, host, port, config, cmd);
            }
        }
    }
}