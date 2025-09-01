package org.p001;

public class C001BasicExample {

    public boolean isValidUserName(String userName) {
        if(userName==null || userName.isEmpty()) return false;
        return userName.matches("[a-zA-Z0-9]+");
    }
}
