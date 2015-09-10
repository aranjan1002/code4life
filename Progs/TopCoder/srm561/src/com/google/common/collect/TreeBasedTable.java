/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Supplier;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.annotation.Nullable;

/**
 * Implementation of {@code Table} whose row keys and column keys are ordered
 * by their natural ordering or by supplied comparators. When constructing a
 * {@code TreeBasedTable}, you may provide comparators for the row keys and
 * the column keys, or you may use natural ordering for both.
 *
 * <p>The {@link #rowKeySet} method returns a {@link SortedSet} and the {@link
 * #rowMap} method returns a {@link SortedMap}, instead of the {@link Set} and
 * {@link Map} specified by the {@link Table} interface.
 *
 * <p>The views returned by {@link #column}, {@link #columnKeySet()}, and {@link
 * #columnMap()} have iterators that don't support {@code remove()}. Otherwise,
 * all optional operations are supported. Null row keys, columns keys, and
 * values are not supported.
 *
 * <p>Lookups by row key are often faster than lookups by column key, because
 * the data is stored in a {@code Map<R, Map<C, V>>}. A method call like {@code
 * column(columnKey).get(rowKey)} still runs quickly, since the row key is
 * provided. However, {@code column(columnKey).size()} takes longer, since an
 * iteration across all row keys occurs.
 *
 * <p>Note that this implementation is not synchronized. If multiple threads
 * access this table concurrently and one of the threads modifies the table, it
 * must be synchronized externally.
 *
 * @author Jared Levy
 * @since 7
 */
@GwtCompatible(serializable = true)
@Beta
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V> {
  private final Comparator<? super C> columnComparator;

  private static class Factory<C, V>
      implements Supplier<TreeMap<C, V>>, Serializable {
    final Comparator<? super C> comparator;
    Factory(Comparator<? super C> comparator) {
      this.comparator = comparator;
    }
    @Override
    public TreeMap<C, V> get() {
      return new TreeMap<C, V>(comparator);
    }
    private static final long serialVersionUID = 0;
  }

  /**
   * Creates an empty {@code TreeBasedTable} that uses the natural orderings
   * of both row and column keys.
   *
   * <p>The method signature specifies {@code R extends Comparable} with a raw
   * {@link Comparable}, instead of {@code R extends Comparable<? super R>},
   * and the same for {@code C}. That's necessary to support classes defined
   * without generics.
   */
  @SuppressWarnings("unchecked") // eclipse doesn't like the raw Comparable
  public static <R extends Comparable, C extends Comparable, V>
      TreeBasedTable<R, C, V> create() {
    return new TreeBasedTable<R, C, V>(Ordering.natural(),
        Ordering.natural());
  }

  /**
   * Creates an empty {@code TreeBasedTable} that is ordered by the specified
   * comparators.
   *
   * @param rowComparator the comparator that orders the row keys
   * @param columnComparator the comparator that orders the column keys
   */
  public static <R, C, V> TreeBasedTable<R, C, V> create(
      Comparator<? super R> rowComparator,
      Comparator<? super C> columnComparator) {
    checkNotNull(rowComparator);
    checkNotNull(columnComparator);
    return new TreeBasedTable<R, C, V>(rowComparator, columnComparator);
  }

  /**
   * Creates a {@code TreeBasedTable} with the same mappings and sort order
   * as the specified {@code TreeBasedTable}.
   */
  public static <R, C, V> TreeBasedTable<R, C, V> create(
      TreeBasedTable<R, C, ? extends V> table) {
    TreeBasedTable<R, C, V> result
        = new TreeBasedTable<R, C, V>(
            table.rowComparator(), table.columnComparator());
    result.putAll(table);
    return result;
  }

  TreeBasedTable(Comparator<? super R> rowComparator,
      Comparator<? super C> columnComparator) {
    super(new TreeMap<R, Map<C, V>>(rowComparator),
        new Factory<C, V>(columnComparator));
    this.columnComparator = columnComparator;
  }

  // TODO(jlevy): Move to StandardRowSortedTable?

  /**
   * Returns the comparator that orders the rows. With natural ordering,
   * {@link Ordering#natural()} is returned.
   */
  public Comparator<? super R> rowComparator() {
    return rowKeySet().comparator();
  }

  /**
   * Returns the comparator that orders the columns. With natural ordering,
   * {@link Ordering#natural()} is returned.
   */
  public Comparator<? super C> columnComparator() {
    return columnComparator;
  }

  // rowKeySet() and rowMap() are defined here so they appear in the Javadoc.

  @Override public SortedSet<R> rowKeySet() {
    return super.rowKeySet();
  }

  @Override public SortedMap<R, Map<C, V>> rowMap() {
    return super.rowMap();
  }

  // Overriding so NullPointerTester test passes.

  @Override public boolean contains(
      @Nullable Object rowKey, @Nullable Object columnKey) {
    return super.contains(rowKey, columnKey);
  }

  @Override public boolean containsColumn(@Nullable Object columnKey) {
    return super.containsColumn(columnKey);
  }

  @Override public boolean containsRow(@Nullable Object rowKey) {
    return super.containsRow(rowKey);
  }

  @Override public boolean containsValue(@Nullable Object value) {
    return super.containsValue(value);
  }

  @Override public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
    return super.get(rowKey, columnKey);
  }

  @Override public boolean equals(@Nullable Object obj) {
    return super.equals(obj);
  }

  @Override public V remove(
      @Nullable Object rowKey, @Nullable Object columnKey) {
    return super.remove(rowKey, columnKey);
  }

  /**
   * Overridden column iterator to return columns values in globally sorted
   * order.
   */
  @Override Iterator<C> createColumnKeyIterator() {
    return new MergingIterator<C>(
        Iterables.transform(backingMap.values(),
            new Function<Map<C, V>, Iterator<C>>() {
                @Override
                public Iterator<C> apply(Map<C, V> input) {
                  return input.keySet().iterator();
                }
              }), columnComparator());
  }

  /**
   * An iterator that performs a lazy N-way merge, calculating the next value
   * each time the iterator is polled. This amortizes the sorting cost over the
   * iteration and requires less memory than sorting all elements at once.
   * Duplicate values are omitted.
   *
   * <p>Retrieving a single element takes approximately O(log(M)) time, where M
   * is the number of iterators. (Retrieving all elements takes approximately
   * O(N*log(M)) time, where N is the total number of elements.)
   */
  // TODO(user): Push this into OrderedIterators or somewhere more generic.
  private static class MergingIterator<T> extends AbstractIterator<T> {
    private final Queue<PeekingIterator<T>> queue;
    private final Comparator<? super T> comparator;

    // The last value we returned, used for removing duplicate values.
    private T lastValue = null;

    public MergingIterator(
        Iterable<? extends Iterator<T>> iterators,
        Comparator<? super T> itemComparator) {
//    checkNotNull(iterators, "iterators");
//    checkNotNull(comparator, "comparator");
      this.comparator = itemComparator;

      // A comparator that's used by the heap, allowing the heap
      // to be sorted based on the top of each iterator.
      Comparator<PeekingIterator<T>> heapComparator =
          new Comparator<PeekingIterator<T>>() {
            @Override
            public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2) {
              return comparator.compare(o1.peek(), o2.peek());
            }
          };

      // Construct the heap with a minimum size of 1, because
      // Because PriorityQueue will fail if it's 0.
      queue = new PriorityQueue<PeekingIterator<T>>(
          Math.max(1, Iterables.size(iterators)), heapComparator);
      for (Iterator<T> iterator : iterators) {
        if (iterator.hasNext()) {
          queue.add(Iterators.peekingIterator(iterator));
        }
      }
    }

    @Override protected T computeNext() {
      while (!queue.isEmpty()) {
        PeekingIterator<T> nextIter = queue.poll();

        T next = nextIter.next();
        boolean duplicate =
            lastValue != null
            && comparator.compare(next, lastValue) == 0;

        if (nextIter.hasNext()) {
          queue.add(nextIter);
        }
        // Keep looping till we find a non-duplicate value.
        if (!duplicate) {
          lastValue = next;
          return lastValue;
        }
      }

      lastValue = null; // clear reference to unused data
      return endOfData();
    }
  }

  private static final long serialVersionUID = 0;
}
