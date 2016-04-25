package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * A mock source to provide data
 */
public class MockSource implements Source<Mock> {
    int index = 0;

    @Override
    public boolean hasNext() {
        return index < 1;
    }

    @Override
    public Collection<Mock> next() {
        return Lists.newArrayList(
            new Mock("1", null),
            new Mock("2", "content2"),
            new Mock("3", "content3")
        );
    }
}
