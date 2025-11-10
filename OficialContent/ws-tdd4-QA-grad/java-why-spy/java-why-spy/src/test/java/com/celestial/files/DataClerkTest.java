package com.celestial.files;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author selvy
 */
public class DataClerkTest
{
   @Test
   public void testProcessData()
   {
      // arrange
      DataClerk.FileLog fl = new DataClerk.FileLog();
      DataClerk cut = new DataClerk( fl );
      
      // act
      cut.ProcessData();
      
      // assert
      // There is no way of knowing if this ran as expected
   }
   
   @Test
   public void files_are_logged_before_8pm_mocked()
   {
      // arrange
      IFileLog fl = mock(IFileLog.class);
      
      // use the doNothing on void methods
      doNothing().when(fl).clearTheLog();
      DataClerk cut = new DataClerk(fl);

      // act
      cut.ProcessData();

      // assert
      verify(fl, times(1)).clearTheLog();
   }
   
   @Test
   public void files_are_wrongly_logged_after_8pm_mocked()
   {
      // We can now run this test at any time of the day
      // This test now passes because we are able to set the time in the test
      IFileLog fl = mock(IFileLog.class);
      
      // use the doNothing on void methods
      doNothing().when(fl).clearTheLog();
      DataClerk cut = new DataClerk(fl);
      // wrap the cut in a spy object
      DataClerk cutSpy = spy( cut );

      // Set an expectation for getTime(), this is a new method will write
      Mockito.doReturn(LocalTime.parse("21:00")).when(cutSpy).getTime();

      // act
      // use the SPY not the CUT
      cutSpy.ProcessData();

      // assert
      verify(fl, times(0)).clearTheLog();
   }
}
