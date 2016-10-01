package com.epam.java.rt.lab.web.component.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * service-ms
 */
public class View implements Iterable<ViewControl> {

    private List<ViewControl> viewControlList;

    View() {
    }

    View copyDef() {
        View view = new View();
        List<ViewControl> viewControlList = new ArrayList<>();
        for (ViewControl viewControl : this.viewControlList)
            viewControlList.add(new ViewControl(viewControl.getControlDef()));
        view.setViewControlList(viewControlList);
        return view;
    }

    void setViewControlList(List<ViewControl> viewControlList) {
        this.viewControlList = viewControlList;
    }

    public int getControlListSize() {
        return this.viewControlList.size();
    }

    public ViewControl getControl(int index) {
        return this.viewControlList.get(index);
    }

    @Override
    public Iterator<ViewControl> iterator() {
        return new Iterator<ViewControl>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < viewControlList.size();
            }

            @Override
            public ViewControl next() {
                return viewControlList.get(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
