package org.betteridiots.maven.plugins.ddf;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.betteridiots.ssh.SshExecFactory;

@Mojo(name="abstract-command", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class DDFCommandMojo extends AbstractMojo {

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
     * Command to be executed against ddf
     */
    @Parameter( property = "abstract-command.command", required = true)
    private String command;

    /**
     * Command Operation to be executed against ddf
     */
    @Parameter( property = "abstract-command.operation", required = true)
    private String operation;

    /**
     * Arguments for command
     */
    @Parameter( property = "abstract-command.args", required = false)
    private String[] args;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        String sshCommand = null;

        // Log Parameters
        getLog().info( "DDF Host is: " + host );
        getLog().info( "DDF Port is: " + port );
        getLog().info( "DDF user is: " + user );
        getLog().info( "DDF password is: " + password );

        sshCommand = command + ":" + operation;

        if (args != null)
        {
            for (String arg : args){
                sshCommand = sshCommand + " " + arg;
            }
        }

        // Disable strict host key checking and set auth method to password
        java.util.Properties sshConfig = new java.util.Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshConfig.put("PreferredAuthentications","password");

        // Build JSch Session

        SshExecFactory sef = new SshExecFactory();

        getLog().info("Executing command: " + sshCommand);
        sef.buildChannel(user, password, host, port, sshConfig, sshCommand);
    }
}
