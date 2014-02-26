package org.betteridiots.maven.plugins.ddf;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.maven.project.MavenProject;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
//import org.apache.felix.karaf.tooling.features.MojoSupport;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Set;

@Mojo( name = "install-app-ddf", defaultPhase = LifecyclePhase.INTEGRATION_TEST )
public class DDFInstallAppMojo extends AbstractMojo
{

    // DDF Project
    /** 
      * @parameter expression="${project}" 
      * @readonly 
      * @required 
      */ 
    protected MavenProject project; 

    // File containing DDF commands
    @Parameter( property = "install-app-ddf.paramsFile")
    private String paramsFile;

    // Array of parameters to be inserted into ddf config command
    @Parameter( property = "install-app-ddf.apps")
    private String[] apps;

    // DDF Home
    @Parameter( property = "install-app-ddf.ddfHome", defaultValue = "/opt/dib/" )
    private String ddfHome;

    // DDF User
    @Parameter( property = "install-app-ddf.user", defaultValue = "admin" )
    private String user;

    // DDF Password
    @Parameter( property = "install-app-ddf.password", defaultValue = "admin" )
    private String password;

    // DDF Host
    @Parameter( property = "install-app-ddf.host", defaultValue = "localhost" )
    private String host;

    // DDF SSH Port
    @Parameter( property = "install-app-ddf.port", defaultValue = "8101" )
    private int port;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // Log Parameters
        getLog().info( "DDF Host is: " + host );
        getLog().info( "DDF Port is: " + port );
        getLog().info( "DDF user is: " + user );
        getLog().info( "DDF password is: " + password );
        getLog().info( "DDF parameter file is: " + paramsFile );
        getLog().info( "DDF Apps are: " );
	for (String app: apps){
            getLog().info( app+", " );
        }

	Set<Artifact> artifacts;
        artifacts  = project.getDependencyArtifacts();

        ////////////////////////////////////////////////////////
        // for (Artifact artifact : this.project.getDependencyArtifacts()) { 
            //Artifact artifact = (Artifact) object; 
        
        for (Artifact artifact : artifacts) {
            getLog().info( "Artifact: ");
            getLog().info( artifact.getArtifactId() );
        
            if (artifact.getArtifactId().equals(apps[0])) {

		getLog().info( "Found Project dependency: " + artifact.getArtifactId());

	    }
        //     if ("my.group.id".equals(artifact.getGroupId()) && "myArtifactId".equals(artifact.getArtifactId())) { 
        // 
        //         for (Object object2 : this.artifactMetadataSource.retrieve(artifact, this.localRepository, this.project.getRemoteArtifactRepositories()).getArtifacts()) { 
        // 
        //             Artifact artifact2 = (Artifact) object2; 
        //             this.artifactResolver.resolve(artifact2, this.project.getRemoteArtifactRepositories(), this.localRepository); 
        // 
        //             JarFile jarFile = new JarFile(artifact2.getFile()); 
        //             JarEntry workflow = null; 
        // 
        //             for (Enumeration<JarEntry> jarEntries = jarFile.entries(); workflow == null && jarEntries.hasMoreElements();) { 
        //                 JarEntry jarEntry = jarEntries.nextElement(); 
        // 
        //                 if (jarEntry.getName() != null && 
        // jarEntry.getName().endsWith("filename.extension")) { 
        //                     workflow = jarEntry; 
        //                 } 
        //             } 
        // 
        //             if (workflow != null) { 
        //                 workflowFiles.add(workflow.getName()); 
        //             } 
        //         } 
        //     } 
        } 
        getLog().info( "DONE......\n\n" );

//////////////////////////////////////////////////////

//        // Initialize BufferedReader and commands ArrayList
//        StringBuffer params = new StringBuffer();
//        String line;
//        
//        if (paramsFile != null) {
//            
//            // Initialize FileReader
//            FileReader fileReader = null;
//            try {
//                fileReader = new FileReader(paramsFile);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//
//            // Parse paramsFile into commands single string
//            try {
//                while ((line = bufferedReader.readLine()) != null) {
//                    params.append(line +"; ");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try
//            {
//                bufferedReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//	}
//        // else{
//
//	//     for (String config : configs){
//        //         params.append(config+"; ");
//        //     }
//        // }
//
//        // Disable strict host key checking and set auth method to password
//        java.util.Properties config = new java.util.Properties();
//        config.put("StrictHostKeyChecking", "no");
//        config.put("PreferredAuthentications","password");
//
//        // Build JSch Session
//        try
//        {
//            JSch jsch=new JSch();
//
//            Session session=jsch.getSession( user, host, port);
//            session.setPassword(password);
//            session.setConfig(config);
//
//            // Connect and open an exec channel
//            session.connect();
//            Channel channel=session.openChannel("exec");
//
//            // Pass in commands from array
//            ((ChannelExec)channel).setCommand( params.toString() );
//
//            channel.setInputStream(null);
//
//            ((ChannelExec)channel).setErrStream(System.err);
//
//            InputStream in=channel.getInputStream();
//
//            channel.connect();
//
//            byte[] tmp=new byte[1024];
//            while(true){
//                while(in.available()>0){
//                    int i=in.read(tmp, 0, 1024);
//                    if(i<0)break;
//                    System.out.print(new String(tmp, 0, i));
//                }
//                if(channel.isClosed()){
//                    System.out.println("exit-status: "+channel.getExitStatus());
//                    break;
//                }
//                try{Thread.sleep(1000);}catch(Exception ee){}
//            }
//            channel.disconnect();
//            session.disconnect();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
    }

}
