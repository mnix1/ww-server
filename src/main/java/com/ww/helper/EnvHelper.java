package com.ww.helper;

import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

public class EnvHelper {
    public static final String DB_PROD = "dbProd";//h2,cloud schema prod
    public static final String DB_UAT = "dbUat";//h2,cloud schema dev
    public static final String DB_DEV = "dbDev";//only h2

    public static final String SIGN_PROD = "logProd";//with oauth
    public static final String SIGN_DEV = "logDev";//only basic

    private static final String DB_PROD_SCHEMA = "prod";
    private static final String DB_UAT_SCHEMA = "dev";

    public static boolean initOutsideDb(Environment env) {
        return !Arrays.asList(env.getActiveProfiles()).contains(DB_PROD);
    }

    public static String outsideDbSchema(Environment env) {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        if (profiles.contains(DB_UAT)) {
            return DB_UAT_SCHEMA;
        }
        if (profiles.contains(DB_PROD)) {
            return DB_PROD_SCHEMA;
        }
        throw new IllegalArgumentException("Wrong arg (no schema for dbDev)");
    }

    public static String outsideCreateSchemaDb(Environment env) {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        if (profiles.contains(DB_UAT)) {
            return "create";
        }
        if (profiles.contains(DB_PROD)) {
            return "none";
        }
        throw new IllegalArgumentException("Wrong arg (no schema for dbDev)");
    }
}
