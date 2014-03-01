package org.betteridiots.maven.plugins.ddf;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class DDFConfigureMojoTest extends AbstractMojoTestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void testMojoGoal() throws Exception
    {
        File testPom = new File( getBasedir(), "src/test/resources/unit/config-test/config-test.xml" );

        DDFConfigureMojo mojo = (DDFConfigureMojo) lookupMojo( "config-ddf", testPom );

        // Check for null values
        assertNotNull( mojo );
        assertNotNull( getVariableValueFromObject( mojo, "host" ) );
        assertNotNull( getVariableValueFromObject( mojo, "port" ) );
        assertNotNull( getVariableValueFromObject( mojo, "user" ) );
        assertNotNull( getVariableValueFromObject( mojo, "password" ) );
        assertNotNull( getVariableValueFromObject( mojo, "paramsFile" ) );

        // Execute mojo test
//        mojo.execute();
    }
}
