package com.celestial.mockito.filetodb;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author selvy
 */
public class FileLoaderTest 
{
   // To use a different type of file system loader, pass a lambda to loadFile()
   // as shown here
   /*
       int bytesRead = cut.loadFile((fname) ->
       {
          List<String> result = null;
          try
          {
              result = Files.readAllLines(Paths.get(fname), StandardCharsets.UTF_8);
          }
          catch (IOException e){}
          return result;
       });
   */

    // Redesign the FileLoader so that the machenism to load files up can be 
    // passed in as a lambda - still titghtly coupled the file system
    @Test
    public void load_all_of_file_using_inbuilt_Files_type_as_lambda() 
    {
        // arrange
        String fileToLoad = "c:/tmp/KeyboardHandler.txt";
        FileLoader cut = new FileLoader(fileToLoad);
        int expectedBytesRead = 10;    //1371;
        List<String> pretendFileContent = new ArrayList<>();
        pretendFileContent.add("Hello");
        pretendFileContent.add("world");
        MockedStatic<Files> ff = Mockito.mockStatic(Files.class);
        ff.when(() -> Files.readAllLines(Paths.get(fileToLoad), StandardCharsets.UTF_8)).thenReturn(pretendFileContent);

        // act
        int bytesRead = cut.loadFile((fname) ->
        {
           List<String> result = null;
           try
           {
               result = Files.readAllLines(Paths.get(fname), StandardCharsets.UTF_8);
           }
           catch (IOException e){}
           return result;
        });

        // assert
        assertEquals(expectedBytesRead, bytesRead);
    }
    
         // Redesign has worked so this test uses a stub to simulate a file being
     // loaded, it is passed in as lambda - We've decoupled ourselves from the 
     // filesystem
     @Test
     public void load_all_of_file_via_stub() 
     {
         // arrange
         String fileToLoad = "";
         FileLoader cut = new FileLoader(fileToLoad);
         int expectedBytesRead = 10;
         
         // act
         int bytesRead = cut.loadFile((fname) ->
         {
            List<String> result = new ArrayList<>();
            
            result.add("Hello");
            result.add("world");

            return result;
         });
         
         // assert
         assertEquals(expectedBytesRead, bytesRead);
     }
     
     @Test
     public void load_all_of_file_using_mock()
     {
        // arrange
        String fileToLoad = "c:/tmp/KeyboardHandler.java.txt";
        FileLoader cut = new FileLoader( fileToLoad );
        // setup ur canned data, these will represent the lines in the file
        ArrayList<String> pretendFileContent = new ArrayList<>();
        pretendFileContent.add("Hello");
        pretendFileContent.add("world");
        int expectedBytesRead = 10;
        
        // Mock the interface
        MyFile file = mock(MyFile.class);
        
        // Setup the expectation
        when(file.readAllLines(Paths.get(fileToLoad), StandardCharsets.UTF_8)).thenReturn(pretendFileContent);
        
        // act
        int bytesRead = cut.loadFile((fname) ->
        {
            List<String> result = file.readAllLines(Paths.get(fname), StandardCharsets.UTF_8);

            return result;
        });
        
         // assert
         assertEquals(expectedBytesRead, bytesRead);        
     }
}
