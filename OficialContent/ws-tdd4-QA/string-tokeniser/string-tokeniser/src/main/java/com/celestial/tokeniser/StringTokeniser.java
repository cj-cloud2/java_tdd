package com.celestial.tokeniser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringTokeniser
{
    public String[] tokenise(String inputVal)
    {
        StringTokenizer st = new StringTokenizer(inputVal, ",");
        List<String> list = new ArrayList<>();
        while( st.hasMoreTokens() )
            list.add(st.nextToken().trim());
        return  list.toArray(new String[list.size()]);
    }
}
