package com.example.mobile.skyline_test_driven_development;

import org.junit.Test;
import org.mockito.InOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

    /**
     * Hello Mockito Example
     *
     * RUN INSTRUCTIONS
     * To work on unit tests, switch the Test Artifact in the Build Variants view.
     * Build Varients is located at the lower left corner of Android studio. It will open up a pane
     * with a dropdown menu labeled Test Artifact. Select the Unit Tests.
     * This will cause the java folder to become green and will cause the package named
     * com.example.mobile.skyline_test_driven_development and its contents to become runnable in android
     * studio.
     *
     * FILE DESCRIPTION
     * With Mockito we can mock interfaces or fully instantiated objects. Once created, a mock will
     * remember all interactions. Then you can selectively verify whatever interactions you are
     * interested in.
     *
     * @author Mark Schumacher
     * 2016-02-12
     * 905mws@gmail.com
     */

public class HelloMockito {

    /**************************************************************************************
     * Verifying the a method invocation occured on an object
     */
    @Test
    public void hello_mockito() throws Exception {
        List mockedList = mock(List.class);             // We can mock interfaces
        mockedList.add("I'm Learning this");            // call a method on the mocked object to
        verify(mockedList).add(anyString());            // verify the method was called on the mock
                                                        // NOTE: anyString() matches any string
    }


    /**************************************************************************************
     * Verifying multiple method invocations that occured on an object
     * And Verifying using wild cards
     */
    @Test
    public void verifyingWithWildcards() throws Exception {
        ArrayList<String> aL = new ArrayList<>();       // We can also mock real objects too
        aL = mock(aL.getClass());
        aL.add(-20, "Ready");                           // Methods called on mock objects dont need
        aL.add(20, "Mark");                             // to even make sense.

        verify(aL).add(-20, "Ready");                   // verify without wildcard
        verify(aL).add(20, "Mark");                     // verify without wildcard
        //verify(aL).add(100, "Wow");                   // This will fail

        // If you are using argument matchers, all arguments have to be provided by matchers.
        // Here we are verifiying that al.add was called with any arguments twice.
        verify(aL,times(2)).add(anyInt(), anyString());

        // This will fail because add() was called twice and the default verify
        // setting assumes that you are verifying a single call to add() which is not the case
        // with our wildcard parameters.
        // verify(aL).add(anyInt(), anyString());
    }

    /**************************************************************************************
     * Verifying the number of method invocations that occured on an object
     */
    @Test
    public void verifyingNumberOfInvocations() throws Exception{
        LinkedList mockedList = mock(LinkedList.class);
        //using mock
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times");

        //this test will fail
        //verify(mockedList, atLeast(2)).add("five times");
        verify(mockedList, atMost(5)).add("three times");
    }

    /**************************************************************************************
     * Verifying the order of the method invocations
     */
    @Test
    public void verifyingInvocationOrder() throws Exception{
        // A. Single mock whose methods must be invoked in a particular order
        List singleMock = mock(List.class);

        //using a single mock
        singleMock.add("was added first");
        singleMock.add("was added second");

        //create an inOrder verifier for a single mock
        InOrder inOrder = inOrder(singleMock);

        //following will make sure that add is first called with "was added first, then with "was added second"
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        // B. Multiple mocks that must be used in a particular order
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        //using mocks
        firstMock.add("was called first");
        secondMock.add("was called second");

        //create inOrder object passing any mocks that need to be verified in order
        inOrder = inOrder(firstMock, secondMock);

        //following will make sure that firstMock was called before secondMock
        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(secondMock).add("was called second");
    }

    /**************************************************************************************
     * Verifying no specific method innvocations
     * and Verifying zero total method innvocations
     */
    @Test
    public void verifyingNoInvocations() throws Exception{
        List mockOne = mock(List.class);
        List mockTwo = mock(List.class);
        List mockThree = mock(List.class);

        //using mocks - only mockOne is interacted
        mockOne.add("one");

        //ordinary verification
        verify(mockOne).add("one");

        //verify that method was never called on a mock
        verify(mockOne, never()).add("two");

        //verify that other mocks were not interacted
        verifyZeroInteractions(mockTwo, mockThree);
    }





}