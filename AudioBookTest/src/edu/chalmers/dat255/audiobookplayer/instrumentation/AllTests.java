package edu.chalmers.dat255.audiobookplayer.instrumentation;
import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

/**
 * This class will run all tests within the test project.
 * 
 */
public class AllTests extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
    }
}

