package com.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * This class has methods to check if two collections or two maps contain the same elements.
 */
final public class CollectionsAndMapsUtils {

    private CollectionsAndMapsUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Method to check if two collections have the same objects, regardless of their order.
     * The two collections must be of the same kind.
     *
     * @param <T> a generic object.
     * @param col1 a collection of objects of type T; should never be null.
     * @param col2 a collection of objects of type T; should never be null.
     * @return true if both collections have the same objects in any order, false otherwise.
     * @throws NullPointerException when either col1 or col2 are null.
     * @throws UnsupportedOperationException when col1 and col2 are not of the same kind.
     */
    public static <T> boolean areCollectionsEqualIgnoringOrder(Collection<T> col1, Collection<T> col2) throws NullPointerException {
        if (col1.getClass() != col2.getClass()) { throw new UnsupportedOperationException(); }
        if (col1.isEmpty() ^ col2.isEmpty()) { return false; }
        if (col1.size() != col2.size()) { return false; }
        Map<T, Integer> frequencyMap = new HashMap<>();
        for (T element : col1) {
            frequencyMap.put(element, frequencyMap.getOrDefault(element, 0) + 1);
        }
        for (T element : col2) {
            if (!frequencyMap.containsKey(element)) {
                return false;
            }
            frequencyMap.put(element, frequencyMap.get(element) - 1);
            if (frequencyMap.get(element) == 0) {
                frequencyMap.remove(element);
            }
        }
        return frequencyMap.isEmpty();
    }

    /**
     * Method to check if two collections have the same objects, in the same order.
     * The two collections must be of the same kind.
     *
     * @param <T> a generic object.
     * @param col1 a collection of objects of type T; should never be null.
     * @param col2 a collection of objects of type T; should never be null.
     * @return true if both collections have the same objects in the same order, false otherwise.
     * @throws NullPointerException when either col1 or col2 are null.
     * @throws UnsupportedOperationException when col1 and col2 are not of the same kind.
     */
    public static <T> boolean areCollectionsEqual(Collection<T> col1, Collection<T> col2) throws NullPointerException{
        if (col1.getClass() != col2.getClass()) { throw new UnsupportedOperationException(); }
        if (col1.isEmpty() ^ col2.isEmpty()) { return false; }
        if (col1.size() != col2.size()) { return false; }
        Iterator<T> iter1 = col1.iterator();
        Iterator<T> iter2 = col2.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            T elem1 = iter1.next();
            T elem2 = iter2.next();
            if (!Objects.equals(elem1, elem2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to check if two maps have the same key-value pairs.
     * The two maps must be of the same kind.
     *
     * @param <K> a generic object, the key to the maps.
     * @param <V> a generic object, the value of the maps.
     * @param map1 a collection of objects of type T; should never be null.
     * @param map2 a collection of objects of type T; should never be null.
     * @return true if both maps have the same key-value pairs in any order, false otherwise.
     * @throws NullPointerException when either map1 or map2 are null.
     * @throws UnsupportedOperationException when map1 and map2 are not of the same kind.
     */
    public static <K, V> boolean areMapsEqual(Map<K, V> map1, Map<K, V> map2) throws NullPointerException{
        if (map1.getClass() != map2.getClass()) { throw new UnsupportedOperationException();  }
        if (map1.isEmpty() ^ map2.isEmpty()) { return false; }
        if (map1.size() != map2.size()) { return false; }
        for (Map.Entry<K, V> entry : map1.entrySet()) {
            K key = entry.getKey();
            V value1 = entry.getValue();
            V value2 = map2.get(key);
            if (value2 == null || !Objects.equals(value1, value2)) {
                return false;
            }
        }
        return true;
    }

}
