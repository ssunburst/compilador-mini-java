package SemanticModule;

import Exceptions.SemanticExceptions.PreexistingKeyException;

import java.util.*;

public class IterableDictionary<K,V> implements Iterable<V>
{
    private final Map<K, List<V>> map;
    private final List<V> allValuesList;

    public IterableDictionary()
    {
        this.map = new HashMap<>();
        this.allValuesList = new LinkedList<>();
    }

    public V get(K k)
    {
        List<V> list = map.get(k);
        if (list == null || list.isEmpty())
            return null;
        else
            return list.get(0);
    }

    public Iterable<V> getAll(K k)
    {
        Iterable<V> list = map.get(k);
        if (list == null)
            list = new LinkedList<>();
        return list;
    }

    public void secureInsert(K k, V v) throws PreexistingKeyException
    {
        if (map.get(k) == null)
        {
            List<V> valuesList = new LinkedList<>();
            valuesList.add(v);
            allValuesList.add(v);
            map.put(k, valuesList);
        }
        else
            throw new PreexistingKeyException();
    }

    public void insert(K k, V v)
    {
        List<V> valuesList = map.get(k);
        if (valuesList == null) {
            valuesList = new LinkedList<>();
            map.put(k, valuesList);
        }
        valuesList.add(v);
        allValuesList.add(v);
    }

    public int amountOf(K k)
    {
        List<V> results = map.get(k);
        if (results == null)
            return 0;
        else
            return results.size();
    }

    public int size()
    {
        return allValuesList.size();
    }

    @Override
    public Iterator<V> iterator()
    {
        return allValuesList.iterator();
    }
}
