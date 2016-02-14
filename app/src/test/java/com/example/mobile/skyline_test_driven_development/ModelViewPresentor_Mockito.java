package com.example.mobile.skyline_test_driven_development;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import org.mockito.ArgumentCaptor;


import static org.mockito.Mockito.*;

/**
 * Mark Schumacher
 * 905mws@gmail.com
 * 2016-02-13
 *
 * This class shows the way we would use Mockito to enable a test driven development(TDD) workflow
 */
public class ModelViewPresentor_Mockito {

    @Mock
    MyModel mModel;         // We do not have a concrete version of our model yet. We need to mock it.

    @Mock
    MyView mView;           // We do not have a concrete version of our view yet. We need to mock it.

    Presentor mPresentor;   // We are currently creating the Presentor class in our TDD workflow

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mPresentor= new Presentor(mModel, mView);  // mModel and mView are mocks
    }


    /****************************************************************************************
     * This is a very important feature of Mockito: argument captors
     * http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html#captors
     *
     * Our main use case of this mockito feature will be to capture Callback objects.
     * Lets take a look at how argumentCaptors work
     */
    @Test
    public void argumentCaptors(){
        // Creation of a capitor. Captors allow us to grab parameters of a function call
        ArgumentCaptor<MyModel.DataLoadedCallback> captor = ArgumentCaptor.forClass(MyModel.DataLoadedCallback.class);

        // Lets start the method we are looking to test
        mPresentor.askModelForData();

        // Capturing the callback object and verifying the method call
        verify(mModel).getData(captor.capture());

        MyModel.DataLoadedCallback callback = captor.getValue(); //retrieving the callback
        List<String> stringList = Arrays.asList(new String[]{"1","2","3"}); // mock data

        // invoke the callback, we need to do this manually because our Model is not implemented
        callback.onDataLoaded(stringList);
        verify(mView).showData(stringList); //assert that the presentor gave the view the data
    }


    /***************************************************************************************
     *  Our Presentor Class, in Test driven development we would be implementing this class as we our
     *  creating the tests for it.
     *  However our MyView class and MyModel class are not implemented yet. This is where we can use
     *  mockito to mock both our View and Model prior to implemention enabling a TDD workflow.
     */
    public class Presentor{
        MyModel m;
        MyView v;

        Presentor(MyModel m, MyView v){
            this.m=m;
            this.v=v;
        }

        /**
         * When the presenter asks the model for the data 2 things happen.
         *     1) we provide the model a callback function to hit when the data is ready
         *     2) the callback hit, and the presentor gives the view the data
         */
        public void askModelForData(){
            m.getData(new MyModel.DataLoadedCallback() {
                @Override
                public void onDataLoaded(List<String> dataStrings) {
                    v.showData(dataStrings);
                }
            });
        };
    }


    /****************************************************************************************
     * UnImplemented model class, takes a callback when we want our data
     */
    public interface MyModel{

        interface DataLoadedCallback{
            void onDataLoaded(List<String> dataStrings);
        }

        void getData(DataLoadedCallback callback);
    }

    /****************************************************************************************
     *  UnImplemented view class just shows the data
     */
    public interface MyView{

        void showData(List<String> data);

    }



}
