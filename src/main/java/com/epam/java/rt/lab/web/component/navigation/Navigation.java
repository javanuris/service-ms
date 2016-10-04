package com.epam.java.rt.lab.web.component.navigation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * category-ms
 */
public class Navigation implements Iterable<Navigation> {

    private String uri;
    private String label;
    private List<Navigation> navigationList;

    public Navigation(String uri, String label) {
        this.uri = uri;
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }

    void addNavigation(Navigation navigation) {
        if (this.navigationList == null) this.navigationList = new ArrayList<>();
        this.navigationList.add(navigation);
    }

    @Override
    public Iterator<Navigation> iterator() {
        return new Iterator<Navigation>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < navigationList.size();
            }

            @Override
            public Navigation next() {
                return navigationList.get(index++);
            }
        };
    }
}
