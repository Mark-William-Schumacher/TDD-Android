package com.example.mobile.skyline_test_driven_development;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.LinkedList;
import java.util.List;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

/**
 * Mark Schumacher
 * 905mws@gmail.com
 * 2016-02-13
 *
 * This class shows some other features of mockito, stubbing, spying
 * examples taken from
 * http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html
 */
public class MockitoAdvanced {

    // Special Annotation for quickly creating mocks , initMocks() must be called to initialize this
    @Mock
    LinkedList mockedList;

    /******************************************************************************************
     *  Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
     *  inject the mocks in the test the initMocks method needs to be called.
     */
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    /******************************************************************************************
     *  Method Stubbing
     */
    @Test
    public void methodStubbing(){
        //stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());
        when(mockedList.get(2)).thenReturn(35);

        //following prints "first"
        System.out.println(mockedList.get(0));

        //following throws runtime exception
        //System.out.println(mockedList.get(1));

        //following throws runtime exception
        System.out.println(String.valueOf(mockedList.get(2)));

        //following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        verify(mockedList).get(0);
    }

    /********************************************************************************************
     * You can create spies of real objects. When you use the spy then the real methods are called
     * (unless a method was stubbed). Real spies should be used carefully and occasionally,
     * for example when dealing with legacy code.
     */
    @Test
    public void objectSpies() {
        List list = new LinkedList();
        List spy = spy(list);

        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);

        //using the spy calls *real* methods
        spy.add("one");
        spy.add("two");

        //prints "one" - the first element of a list
        System.out.println(spy.get(0));

        //size() method was stubbed - 100 is printed
        System.out.println(spy.size());

        //optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");
    }

    /********************************************************************************************
     * Capitors:
     *      Powerful mockito feature that allows you to steal the parameters from a method call
     *
     *      Not particularly useful in this example but, more useful when we are capturing callbacks
     *      in an MVP architecture
     */
    @Test
    public void mockito_capitors(){
        // Were gonna capture the persons dog and walk him
        ArgumentCaptor<Dog> captor = ArgumentCaptor.forClass(Dog.class);

        Person mike = spy(new Person()); //Spy is a real object
        mike.startDay();

        // We are verifying mike walked his dog and capturing his dog in the same line
        verify(mike).walkDog(captor.capture());
        Dog doggy = captor.getValue(); // we have the dog

        for(int i = 0; i<1000; i++){
            doggy.walk();
        }
        assert (doggy.isDead);
    }


    /*
     * Person class for capitors example
     */
    public class Person {

        public void walkDog(Dog d){
            d.walk();
        }

        public void startDay(){
            eatBreakfast();
            Dog pet = new Dog();
            walkDog(pet);
        }

        public void eatBreakfast(){
            // Yum
        }
    }

    /*
     * Dog Class for capitors example
     */
    public class Dog{
        private int walkCount = 0;
        private boolean isDead = false;
        public void walk(){
            walkCount++;
            isDead = (walkCount>1000)? true :  false;
        }
    }






}
