package com.tyrael.kharazim.demo;

import lombok.Getter;

import java.util.UUID;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
public class CallSuperConstructDemo {

    public static void main(String[] args) {
        SubEntity subEntity = new SubEntity();
        System.out.println(subEntity.getName());
    }

    public static class SubEntity extends SuperEntity {

        public SubEntity() {
            this(buildName());
        }

        public SubEntity(String name) {
            super(name);
        }

        private static String buildName() {
            return UUID.randomUUID().toString();
        }

    }

    @Getter
    public static class SuperEntity {

        protected final String name;

        public SuperEntity(String name) {
            this.name = name;
        }
    }

}
