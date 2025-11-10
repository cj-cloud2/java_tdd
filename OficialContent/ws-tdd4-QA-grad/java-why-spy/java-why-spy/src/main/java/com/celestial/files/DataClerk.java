package com.celestial.files;

import java.time.LocalTime;

public class DataClerk
{
   static   public class FileLog implements IFileLog
   {
       public  void    clearTheLog()
       {
           // Simulated method that would do something to files in the log
       }
   }

   private IFileLog theFileLog;

   public   DataClerk( IFileLog fl )
   {
      this.theFileLog = fl;
   }
   
   public   LocalTime getTime()
   {
      return  LocalTime.now();
   }
   
   public  void    ProcessData()
   {
      LocalTime now = getTime();
      LocalTime stopTime = LocalTime.parse("20:00");

      if( now.isBefore(stopTime) )
      {
         System.out.println("Ready to process the data");
         theFileLog.clearTheLog();
      }
   }
}