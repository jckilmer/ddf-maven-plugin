package org.betteridiots.maven.plugins.ddf;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class DDFInstallAppMojoTest extends AbstractMojoTestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void testMojoGoal() throws Exception
    {
        File testPom = new File( getBasedir(), "src/test/resources/unit/install-app-test/install-app-test.xml" );

        DDFInstallAppMojo mojo = (DDFInstallAppMojo) lookupMojo( "install-app-ddf", testPom );

        // Check for null values
        assertNotNull( mojo );
        assertNotNull( getVariableValueFromObject( mojo, "host" ) );
        assertNotNull( getVariableValueFromObject( mojo, "port" ) );
        assertNotNull( getVariableValueFromObject( mojo, "user" ) );
        assertNotNull( getVariableValueFromObject( mojo, "password" ) );

        // Execute mojo test
//        mojo.execute();
    }
}
