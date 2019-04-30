package com.etp.resumeg.resumeg;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testIndexOf() {
        String[] stringArr = {" ", " ", "lorem ipsum dolor sit amet, consectetuer adipiscing elit",
                " ", "123 your street"};

//        String[] stringArr = {" ", " ", "1","2","3", " ", "y", "o", "u", "r", " ", "s", "t", "r", "e", "e", "t"};

//        String[] stringArr = {" ", " ", " 12", "3", " ", "y", "o", "u", "r", " ", "s", "t", "r", "e", "e", "t"};
//        String[] stringArr = {" ", " ", "  12 ", "  12 ", " 3", " ", " y", "    o", "u  ", " r", " ", "s", "t", "r", "e", "e", "t"};

//        String[] stringArr = {" ", " ", "123 your", " ", "s", "t", "r", "e", "e", "t"};


        String searchString = "123 your street";
        int searchStringLength = searchString.length();
        int searchStartIndex = 0;
        int searchEndIndex = 0;

        int searchIndex = 0;
        int searchStringIndex = 0;

        int letterCount = 0;

        int index = 0;
//        for (String item :
//                stringArr) {
//            if(item.equals(searchString)){
//                    searchIndex = index;
//                    break;
//            }
//
//            index++;
//        }

        for (String item :
                stringArr) {
            if(item.length() == 1){

                System.out.println("if(item.length() == 1)");
                System.out.println("item:" + item);
                System.out.println("searchString.toCharArray()[searchIndex]:" + searchString.toCharArray()[searchIndex]);
//                if("1".equals(searchString.toCharArray()[searchIndex])){
//                    System.out.println("yes");
//                }
                if(item.toCharArray()[0] == searchString.toCharArray()[searchIndex]){
                    System.out.println("if(item.equals(searchString.toCharArray()[searchIndex]))");
                    searchIndex++;
                    letterCount++;
                }
            }else if(item.length() > 1){

                int realItemLength = item.length();
                item = item.trim();

                System.out.println("item:" + item);

                if(item.toCharArray()[0] == searchString.toCharArray()[searchIndex]){
                    System.out.println("if(item.toCharArray()[0] == searchString.toCharArray()[searchIndex])");
                    searchIndex += item.length();

                }
            }

            if(searchIndex == searchString.length()){
                System.out.println("break");
                searchEndIndex = index + 1;
                if(searchStartIndex == 0){
                    searchStartIndex = index;
                }
                break;
            }

            System.out.println("searchIndex:" + searchIndex);

            if(searchIndex - 1 == 0 || searchIndex - item.length() == 0){

                searchStartIndex = index;
                System.out.println("searchStartIndex:" + searchStartIndex  + " filled");
            }

            index++;

            System.out.println();
        }

        for (int i = searchStartIndex; i < searchEndIndex; i++) {
            stringArr[i] = "";
        }

        System.out.println();
        System.out.println("Final version of stringArr");
        for (String item :
                stringArr) {
            System.out.print(item);
        }

        System.out.println(" ");
        System.out.println("Final:");
        System.out.println("searchIndex:" + searchIndex);
        System.out.println("searchStartIndex:" + searchStartIndex);
        System.out.println("searchEndIndex:" + searchEndIndex);

        // Assert
        assertTrue(searchIndex == 15);
    }
}
