package org.p001;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class C001BasicExampleTest {

     @Test
     public void testIsValidUserName() {
         // Arrange
         C001BasicExample cut = new C001BasicExample();
         String userName = "john1234";
         // Act
         boolean result = cut.isValidUserName(userName);
         // Assert
         assertTrue(result);


         String userNameInvalid1 = "john_1234";
         String userNameInvalid2 = "john@1234";
         String userNameInvalid3 = "";
         String userNameInvalid4 = " ";
         String userNameInvalid5 = null;

         assertFalse(cut.isValidUserName(userNameInvalid1));
         assertFalse(cut.isValidUserName(userNameInvalid2));
         assertFalse(cut.isValidUserName(userNameInvalid3));
         assertFalse(cut.isValidUserName(userNameInvalid4));
         assertFalse(cut.isValidUserName(userNameInvalid5));


     }
}
