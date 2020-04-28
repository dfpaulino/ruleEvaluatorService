package org.farmtec.res.rules.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.rules.RuleComponent;

/**
 * Immutable implementation of {@link RuleGroupComposite}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableRuleGroupComposite.builder()}.
 * Use the static factory method to create immutable instances:
 * {@code ImmutableRuleGroupComposite.of()}.
 */
@SuppressWarnings("all")
public final class ImmutableRuleGroupComposite extends RuleGroupComposite {
  private final List<RuleComponent> rules;
  private final LogicalOperation logicalOperation;

  private ImmutableRuleGroupComposite(
      Iterable<? extends RuleComponent> rules,
      LogicalOperation logicalOperation) {
    this.rules = createUnmodifiableList(false, createSafeList(rules, true, false));
    this.logicalOperation = Objects.requireNonNull(logicalOperation, "logicalOperation");
  }

  private ImmutableRuleGroupComposite(
      ImmutableRuleGroupComposite original,
      List<RuleComponent> rules,
      LogicalOperation logicalOperation) {
    this.rules = rules;
    this.logicalOperation = logicalOperation;
  }

  /**
   * @return The value of the {@code rules} attribute
   */
  @Override
  public List<RuleComponent> getRules() {
    return rules;
  }

  /**
   * @return The value of the {@code logicalOperation} attribute
   */
  @Override
  public LogicalOperation getLogicalOperation() {
    return logicalOperation;
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link RuleGroupComposite#getRules() rules}.
   * @param elements The elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableRuleGroupComposite withRules(RuleComponent... elements) {
    List<RuleComponent> newValue = createUnmodifiableList(false, createSafeList(Arrays.asList(elements), true, false));
    return new ImmutableRuleGroupComposite(this, newValue, this.logicalOperation);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link RuleGroupComposite#getRules() rules}.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param elements An iterable of rules elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableRuleGroupComposite withRules(Iterable<? extends RuleComponent> elements) {
    if (this.rules == elements) return this;
    List<RuleComponent> newValue = createUnmodifiableList(false, createSafeList(elements, true, false));
    return new ImmutableRuleGroupComposite(this, newValue, this.logicalOperation);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link RuleGroupComposite#getLogicalOperation() logicalOperation} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param logicalOperation A new value for logicalOperation
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableRuleGroupComposite withLogicalOperation(LogicalOperation logicalOperation) {
    if (this.logicalOperation == logicalOperation) return this;
    LogicalOperation newValue = Objects.requireNonNull(logicalOperation, "logicalOperation");
    return new ImmutableRuleGroupComposite(this, this.rules, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableRuleGroupComposite} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableRuleGroupComposite
        && equalTo((ImmutableRuleGroupComposite) another);
  }

  private boolean equalTo(ImmutableRuleGroupComposite another) {
    return rules.equals(another.rules)
        && logicalOperation.equals(another.logicalOperation);
  }

  /**
   * Computes a hash code from attributes: {@code rules}, {@code logicalOperation}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + rules.hashCode();
    h = h * 17 + logicalOperation.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code RuleGroupComposite} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "RuleGroupComposite{"
        + "rules=" + rules
        + ", logicalOperation=" + logicalOperation
        + "}";
  }

  /**
   * Construct a new immutable {@code RuleGroupComposite} instance.
   * @param rules The value for the {@code rules} attribute
   * @param logicalOperation The value for the {@code logicalOperation} attribute
   * @return An immutable RuleGroupComposite instance
   */
  public static ImmutableRuleGroupComposite of(List<RuleComponent> rules, LogicalOperation logicalOperation) {
    return of((Iterable<? extends RuleComponent>) rules, logicalOperation);
  }

  /**
   * Construct a new immutable {@code RuleGroupComposite} instance.
   * @param rules The value for the {@code rules} attribute
   * @param logicalOperation The value for the {@code logicalOperation} attribute
   * @return An immutable RuleGroupComposite instance
   */
  public static ImmutableRuleGroupComposite of(Iterable<? extends RuleComponent> rules, LogicalOperation logicalOperation) {
    return new ImmutableRuleGroupComposite(rules, logicalOperation);
  }

  /**
   * Creates an immutable copy of a {@link RuleGroupComposite} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable RuleGroupComposite instance
   */
  public static ImmutableRuleGroupComposite copyOf(RuleGroupComposite instance) {
    if (instance instanceof ImmutableRuleGroupComposite) {
      return (ImmutableRuleGroupComposite) instance;
    }
    return ImmutableRuleGroupComposite.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableRuleGroupComposite ImmutableRuleGroupComposite}.
   * @return A new ImmutableRuleGroupComposite builder
   */
  public static ImmutableRuleGroupComposite.Builder builder() {
    return new ImmutableRuleGroupComposite.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableRuleGroupComposite ImmutableRuleGroupComposite}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_LOGICAL_OPERATION = 0x1L;
    private long initBits = 0x1L;

    private List<RuleComponent> rules = new ArrayList<RuleComponent>();
    private LogicalOperation logicalOperation;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code RuleGroupComposite} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * Collection elements and entries will be added, not replaced.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(RuleGroupComposite instance) {
      Objects.requireNonNull(instance, "instance");
      addAllRules(instance.getRules());
      logicalOperation(instance.getLogicalOperation());
      return this;
    }

    /**
     * Adds one element to {@link RuleGroupComposite#getRules() rules} list.
     * @param element A rules element
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addRules(RuleComponent element) {
      this.rules.add(Objects.requireNonNull(element, "rules element"));
      return this;
    }

    /**
     * Adds elements to {@link RuleGroupComposite#getRules() rules} list.
     * @param elements An array of rules elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addRules(RuleComponent... elements) {
      for (RuleComponent element : elements) {
        this.rules.add(Objects.requireNonNull(element, "rules element"));
      }
      return this;
    }

    /**
     * Sets or replaces all elements for {@link RuleGroupComposite#getRules() rules} list.
     * @param elements An iterable of rules elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder rules(Iterable<? extends RuleComponent> elements) {
      this.rules.clear();
      return addAllRules(elements);
    }

    /**
     * Adds elements to {@link RuleGroupComposite#getRules() rules} list.
     * @param elements An iterable of rules elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addAllRules(Iterable<? extends RuleComponent> elements) {
      for (RuleComponent element : elements) {
        this.rules.add(Objects.requireNonNull(element, "rules element"));
      }
      return this;
    }

    /**
     * Initializes the value for the {@link RuleGroupComposite#getLogicalOperation() logicalOperation} attribute.
     * @param logicalOperation The value for logicalOperation 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder logicalOperation(LogicalOperation logicalOperation) {
      this.logicalOperation = Objects.requireNonNull(logicalOperation, "logicalOperation");
      initBits &= ~INIT_BIT_LOGICAL_OPERATION;
      return this;
    }

    /**
     * Builds a new {@link ImmutableRuleGroupComposite ImmutableRuleGroupComposite}.
     * @return An immutable instance of RuleGroupComposite
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableRuleGroupComposite build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableRuleGroupComposite(null, createUnmodifiableList(true, rules), logicalOperation);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_LOGICAL_OPERATION) != 0) attributes.add("logicalOperation");
      return "Cannot build RuleGroupComposite, some of required attributes are not set " + attributes;
    }
  }

  private static <T> List<T> createSafeList(Iterable<? extends T> iterable, boolean checkNulls, boolean skipNulls) {
    ArrayList<T> list;
    if (iterable instanceof Collection<?>) {
      int size = ((Collection<?>) iterable).size();
      if (size == 0) return Collections.emptyList();
      list = new ArrayList<T>();
    } else {
      list = new ArrayList<T>();
    }
    for (T element : iterable) {
      if (skipNulls && element == null) continue;
      if (checkNulls) Objects.requireNonNull(element, "element");
      list.add(element);
    }
    return list;
  }

  private static <T> List<T> createUnmodifiableList(boolean clone, List<T> list) {
    switch(list.size()) {
    case 0: return Collections.emptyList();
    case 1: return Collections.singletonList(list.get(0));
    default:
      if (clone) {
        return Collections.unmodifiableList(new ArrayList<T>(list));
      } else {
        if (list instanceof ArrayList<?>) {
          ((ArrayList<?>) list).trimToSize();
        }
        return Collections.unmodifiableList(list);
      }
    }
  }
}
