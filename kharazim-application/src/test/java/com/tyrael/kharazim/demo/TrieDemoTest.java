package com.tyrael.kharazim.demo;

import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/5/30
 */
public class TrieDemoTest {

    @Test
    void wordCount() {

        Pairs<String, Integer> wordCountPairs = new Pairs<String, Integer>()
                .append("hello", 100)
                .append("world", 234)
                .append("java", 543)
                .append("tyrael", 876)
                .append("kharazim", 930)
                .append("archangel", 777)
                .append("大天使", 333)
                .append("泰瑞尔", 843)
                .append("卡拉辛姆", 568);

        Trie trie = new Trie();

        Map<String, Integer> map = wordCountPairs.stream()
                .collect(Collectors.toMap(Pair::left, Pair::right));
        List<String> words = new ArrayList<>(map.keySet());
        while (!map.isEmpty()) {
            String word = CollectionUtils.random(words);
            Integer count = map.get(word);
            count--;
            trie.insert(word);
            if (count > 0) {
                map.put(word, count);
            } else {
                map.remove(word);
                words = new ArrayList<>(map.keySet());
            }
        }

        Map<String, Integer> wordCountMap = trie.wordCount();
        System.out.println(wordCountMap);
    }

    public static class Trie {

        private final Map<Character, Trie> children = new HashMap<>();
        private Character c;
        private int count = 0;

        public void insert(String word) {
            insert(word.toCharArray(), 0);
        }

        private void insert(char[] chars, int index) {
            if (chars.length == index) {
                this.count++;
                return;
            }
            char c = chars[index];
            Trie child = children.get(c);
            if (child == null) {
                child = new Trie();
                child.c = c;
                children.put(c, child);
            }
            child.insert(chars, index + 1);
        }

        public Map<String, Integer> wordCount() {
            Set<WordCount> wordCounts = new TreeSet<>(Comparator.comparing(WordCount::count)
                    .reversed().thenComparing(WordCount::word));
            StringBuilder builder = new StringBuilder();
            for (Trie child : children.values()) {
                child.buildWordCount(builder, wordCounts);
            }
            Map<String, Integer> wordCountMap = new LinkedHashMap<>(wordCounts.size());
            for (WordCount wordCount : wordCounts) {
                wordCountMap.put(wordCount.word(), wordCount.count());
            }
            return wordCountMap;
        }

        private void buildWordCount(StringBuilder builder, Set<WordCount> wordCounts) {
            builder.append(c);
            if (this.count > 0) {
                wordCounts.add(new WordCount(builder.toString(), this.count));
            }
            for (Trie child : this.children.values()) {
                child.buildWordCount(builder, wordCounts);
            }
            builder.deleteCharAt(builder.length() - 1);
        }

        private record WordCount(String word, int count) {
        }

    }

}
