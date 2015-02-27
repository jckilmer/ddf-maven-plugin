package org.betteridiots.maven.plugins.ddf;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.betteridiots.ssh.SshExecFactory;


@Mojo( name="catalog", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST )
public class DDFCatalogMojo extends AbstractMojo {
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
     * Operation to be executed
     */
    @Parameter( property = "catalog.operation", required = true)
    private String operation;

    /**
     * Transformer to use for ingest
     */
    @Parameter( property = "catalog.transformer")
    private String transformer;

    /**
     * Use provider
     */
    @Parameter( property = "catalog.provider", required = false, defaultValue = "false")
    private boolean provider;

    /**
     * Path to metacards for ingest
     */
    @Parameter( property = "catalog.data")
    private String data;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        String argsList = " -f";
        String sshCommand = null;

        // Log Parameters
        getLog().info( "DDF Host is: " + host );
        getLog().info( "DDF Port is: " + port );
        getLog().info( "DDF user is: " + user );
        getLog().info( "DDF password is: " + password );

        if (provider) {
            argsList = argsList + " -p";
        }

        if (operation.equals("ingest")) {
            if (transformer == null)
                getLog().error("Transformer not specified, this is required for performing ingest");
            else
                sshCommand = "catalog:" + operation + argsList + transformer + " " + data;
        }
        if (operation.equals("removeall")) {
            sshCommand = "catalog:" + operation + argsList;
        }
        else
            getLog().error( "The requested operation: " + operation + " is not supported yet, or it does not exist");

        // Disable strict host key checking and set auth method to password
        java.util.Properties sshConfig = new java.util.Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshConfig.put("PreferredAuthentications","password");

        // Build JSch Session

        SshExecFactory sef = new SshExecFactory();

        getLog().info("Executing operation: " + sshCommand);
        sef.buildChannel(user, password, host, port, sshConfig, sshCommand);
    }
}
