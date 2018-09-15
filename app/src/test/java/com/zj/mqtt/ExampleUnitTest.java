package com.zj.mqtt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testRemove() {
        List<Integer> srcList = new ArrayList<>();
        List<Integer> newList = new ArrayList<>();

        srcList.add(1);
        srcList.add(3);
        srcList.add(4);
        srcList.add(5);

        newList.add(3);
        newList.add(6);
        newList.add(7);

        Iterator<Integer> it = srcList.iterator();
        while (it.hasNext()) {
            int place = it.next();

            System.out.println(place + " = " + srcList.size() +"/"+ newList.size() );

            for (int j= 0; j<newList.size(); j++) {
                if (place == newList.get(j)) {
                    break;
                }
                if (j == newList.size()-1) {
                    it.remove();
                }
            }

            //Iterator<Integer> newIten = srcList.iterator();
            //while (newIten.hasNext()) {
            //    int newInt = newIten.next();
            //    if (newInt == place) {
            //        newIten.remove();
            //        break;
            //    }
            //    if (!newIten.hasNext()) {
            //        it.remove();
            //    }
            //}
        }

    }
}