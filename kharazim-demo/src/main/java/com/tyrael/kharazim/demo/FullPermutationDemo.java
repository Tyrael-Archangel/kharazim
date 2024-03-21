package com.tyrael.kharazim.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/21
 */
public class FullPermutationDemo {

    public static void main(String[] args) {
        List<String> elements = List.of("A", "B", "C", "D", "E", "F");
        List<List<String>> fullPermutation = getFullPermutation(elements);
        System.out.println(fullPermutation.size());
        fullPermutation.forEach(System.out::println);
    }

    private static List<List<String>> getFullPermutation(Collection<String> elements) {

        List<List<String>> results = new ArrayList<>();
        execute(results, new LinkedHashSet<>(elements), new LinkedHashSet<>());
        return results;
    }

    private static <T> void execute(List<List<T>> results,
                                    LinkedHashSet<T> elements,
                                    LinkedHashSet<T> usedElements) {
        if (usedElements.size() == elements.size()) {
            results.add(new ArrayList<>(usedElements));
            return;
        }
        for (T element : elements) {
            if (!usedElements.contains(element)) {
                usedElements.add(element);
                execute(results, elements, usedElements);
                usedElements.remove(element);
            }
        }
    }

}
