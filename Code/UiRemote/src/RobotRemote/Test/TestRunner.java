package RobotRemote.Test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
      RunAllTest();
    }
    public static void RunAllTest(){
        Result result = JUnitCore.runClasses(AppObjectTest.class,MapTranslationTest.class,StateMachineBuilderTest.class,UiConnectionTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if(result.wasSuccessful()){
            System.out.println("Passed all tests ");
        }
        else{
            System.out.println("Failed "+result.getFailureCount()+" tests");
        }

    }
}
