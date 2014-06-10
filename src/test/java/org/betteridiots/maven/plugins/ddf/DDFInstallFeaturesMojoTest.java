package org.betteridiots.maven.plugins.ddf;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class DDFInstallFeaturesMojoTest extends AbstractMojoTestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void testMojoGoal() throws Exception
    {
        File testPom = new File( getBasedir(), "src/test/resources/unit/basic-test/basic-test-plugin-config.xml" );

        DDFInstallFeaturesMojo mojo = (DDFInstallFeaturesMojo) lookupMojo( "install-features", testPom );

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

//    public void testMojoDefaults() throws Exception
//    {
//        File testPom = new File( getBasedir(), "src/test/resources/unit/test-defaults/test-ddfclient-maven-plugin-defaults.xml" );
//
//        DDFInstallFeaturesMojo mojo = ( DDFInstallFeaturesMojo ) lookupMojo( "config-ddf", testPom );
//
//        // Check that parameters are not null
//        try {
//            assertNotNull( mojo );
//            assertNotNull( getVariableValueFromObject( mojo, "host" ) );
//            assertNotNull( getVariableValueFromObject( mojo, "port" ) );
//            assertNotNull( getVariableValueFromObject( mojo, "user" ) );
//            assertNotNull( getVariableValueFromObject( mojo, "password" ) );
//            assertNotNull( getVariableValueFromObject( mojo, "paramsFile" ) );
//        } catch (IllegalAccessException e) {
//            e.getMessage();
//        }
//
//        // Check that all parameters have been set to default values
//        // TODO: Add checks for default values
//
//        // Execute mojo test
////        mojo.execute();
//    }
}
