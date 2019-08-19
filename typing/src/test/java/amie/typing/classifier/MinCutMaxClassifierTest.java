package amie.typing.classifier;

import amie.data.KB;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javatools.datatypes.Integer;
import junit.framework.TestCase;

public class MinCutMaxClassifierTest extends TestCase {
    
    public void testLinkSortMap() throws Exception {
        Int2ObjectMap<Double> m1 = new Int2ObjectOpenHashMap<>();
        int v1 = KB.map("1");
        int v2 = KB.map("2");
        int v3 = KB.map("3");
        m1.put(v2, 1.0);
        m1.put(v1, 2.0);
        m1.put(v3, 1.5);
        LinkedList<Int2ObjectMap.Entry<Double>> result = MinCutMaxClassifier.linkSortMap(m1, Collections.reverseOrder());
        assertEquals(v1, result.pop().getKey());
        assertEquals(v3, result.pop().getKey());
        assertEquals(v2, result.pop().getKey());
        assertTrue(result.isEmpty());
    }
    
    public void testClassify() throws Exception {
        
        MinCutMaxClassifier mcmc = new MinCutMaxClassifier(new KB()); 
        // Chain v1 <-> v2 <-> v3
        Int2ObjectMap<Int2ObjectMap<Double>> t1 = new Int2ObjectOpenHashMap<>();
        Int2ObjectMap<Double> m1 = new Int2ObjectOpenHashMap<>();
        Int2ObjectMap<Double> m2 = new Int2ObjectOpenHashMap<>();
        Int2ObjectMap<Double> m3 = new Int2ObjectOpenHashMap<>();
        int v1 = KB.map("1");
        int v2 = KB.map("2");
        int v3 = KB.map("3");
        m1.put(v2, 1.0);
        m2.put(v3, 2.0);
        m3.put(v2, 3.0);
        m2.put(v1, 4.0);
        t1.put(v1, m1);
        t1.put(v2, m2);
        t1.put(v3, m3);
        IntSet result = new IntOpenHashSet();
        
        // from v1
        result.add(v2);
        result.add(v3);
        assertEquals(result, mcmc.t_MinCutMax(t1, v1));
        result.clear();
        
        // from v2
        result.add(v3);
        assertEquals(result, mcmc.t_MinCutMax(t1, v2));
        result.clear();
        
        // from v3
        result.add(v1);
        result.add(v2);
        assertEquals(result, mcmc.t_MinCutMax(t1, v3));
        result.clear();
    }
}
