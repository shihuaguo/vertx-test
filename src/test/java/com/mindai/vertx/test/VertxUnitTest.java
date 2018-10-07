package com.mindai.vertx.test;

import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;
import org.junit.Test;

public class VertxUnitTest {

    @Test
    public void test1(){
        TestSuite suite = TestSuite.create("the_test_suite");
        suite.test("my_test_case", context -> {
            String s = "value";
            context.assertEquals("value", s);
        });
        suite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
    }

    @Test
    public void testFluent(){
        TestSuite suite = TestSuite.create("the_test_suite");
        suite.before(testContext -> {
            System.out.println("before test");
        }).test("t1", testContext -> {
            System.out.println("on test t1");
        }).test("t2", testContext -> {
            System.out.println("on test t2");
        }).after(testContext -> {
            System.out.println("after test");
        });
        suite.run();
    }
}
