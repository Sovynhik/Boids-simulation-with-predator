package com.prutzkow.resourcebundledemo;

import java.util.Locale;

import com.prutzkow.resourcer.ProjectResourcer;
import com.prutzkow.resourcer.Resourcer;

public class ResourceBundleDemoRunner {

    private static Resourcer resourcer = ProjectResourcer.getInstance();

    private ResourceBundleDemoRunner() {
    }

    public static void main(String[] args) {
        StringBuilder result;

        result = new StringBuilder();
        result.append(resourcer.getString("message.default.locale"))
                .append(Locale.getDefault()).append("\n")
                .append(resourcer.getString("message.test"));

        System.out.println(result);

        Locale.setDefault(Locale.US);

        result = new StringBuilder();
        result.append(resourcer.getString("message.default.locale"))
                .append(Locale.getDefault()).append("\n")
                .append(resourcer.getString("message.test"));

        System.out.println(result);
    }
}