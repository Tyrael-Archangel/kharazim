package com.tyrael.kharazim.demo;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/4/26
 */
public class HeroDemoTest {

    @Test
    void loadHeroAbilities() throws IOException {
        String file = "C:\\Users\\jay\\Desktop\\temp\\风暴英雄角色.txt";
        List<String> lines = Files.readAllLines(new File(file).toPath());
        String json = String.join("\n", lines);
        List<Hero> heroes = JSON.parseArray(json, Hero.class);
        List<Trait> allAbilities = new ArrayList<>();
        for (Hero hero : heroes) {
            hero.trait.supplier = hero.franchise;
            allAbilities.add(hero.trait);
            for (Trait ability : hero.abilities) {
                ability.supplier = hero.franchise;
            }
            allAbilities.addAll(hero.abilities);
            for (Trait ability : hero.heroicAbilities) {
                ability.supplier = hero.franchise;
            }
            allAbilities.addAll(hero.heroicAbilities);
        }
        for (Trait ability : allAbilities) {
            System.out.println("new HeroAbility(\"" + ability.name + "\", \"" + ability.description + "\", \"" + ability.icon + "\", \"" + ability.cooldown + "\", \"" + ability.supplier + "\"),");
        }
    }

    @Data
    static class Hero {
        private String name;
        private String title;
        private String description;
        private String franchise;
        private Trait trait;
        private List<Trait> abilities;
        private List<Trait> heroicAbilities;
    }

    @Data
    static class Trait {
        private String name;
        private String description;
        private String displayText;
        private String cooldown;
        private String icon;
        private String supplier;
    }
}
