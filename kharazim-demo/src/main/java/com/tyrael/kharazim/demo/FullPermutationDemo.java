package com.tyrael.kharazim.demo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/21
 */
public class FullPermutationDemo {

    public static void main(String[] args) {
        List<String> elements = List.of("A", "A", "A", "C", "D", "E", "F");
        List<List<String>> fullPermutation = getFullPermutation(elements);
        System.out.println(fullPermutation.size());
        fullPermutation.forEach(System.out::println);
    }

    public static <T> List<List<T>> getFullPermutation(List<T> elements) {

        LinkedHashSet<Element<T>> elementSet = new LinkedHashSet<>();
        int i = 0;
        for (T element : elements) {
            elementSet.add(new Element<>(element, i++));
        }

        List<List<T>> results = new ArrayList<>();
        execute(results, elementSet, new LinkedHashSet<>());
        return results;
    }

    private static <T> void execute(List<List<T>> results,
                                    LinkedHashSet<Element<T>> elements,
                                    LinkedHashSet<Element<T>> usedElements) {
        if (usedElements.size() == elements.size()) {
            List<T> result = usedElements.stream()
                    .map(Element::t)
                    .collect(Collectors.toList());
            results.add(result);
            return;
        }
        LinkedHashSet<T> currentLevelUsed = new LinkedHashSet<>();
        for (Element<T> element : elements) {
            if (!usedElements.contains(element)) {
                if (!currentLevelUsed.contains(element.t)) {
                    usedElements.add(element);
                    execute(results, elements, usedElements);
                    usedElements.remove(element);
                }
                currentLevelUsed.add(element.t);
            }
        }
    }

    private record Element<T>(T t, int index) {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Element<?> element)) {
                return false;
            }
            return index == element.index && Objects.equals(t, element.t);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t, index);
        }
    }

}
