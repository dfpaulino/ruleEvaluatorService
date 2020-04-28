package org.farmtec.res.rules.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;

/**
 * Immutable implementation of {@link IntegerRuleLeaf}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableIntegerRuleLeaf.builder()}.
 * Use the static factory method to create immutable instances:
 * {@code ImmutableIntegerRuleLeaf.of()}.
 */
@SuppressWarnings("all")
public final class ImmutableIntegerRuleLeaf extends IntegerRuleLeaf {
  private final String tag;
  private final Operation operation;
  private final int value;
  private final PredicateFactory<Integer> predicateFactory;

  private ImmutableIntegerRuleLeaf(
      String tag,
      Operation operation,
      int value,
      PredicateFactory<Integer> predicateFactory) {
    this.tag = Objects.requireNonNull(tag, "tag");
    this.operation = Objects.requireNonNull(operation, "operation");
    this.value = value;
    this.predicateFactory = Objects.requireNonNull(predicateFactory, "predicateFactory");
  }

  private ImmutableIntegerRuleLeaf(
      ImmutableIntegerRuleLeaf original,
      String tag,
      Operation operation,
      int value,
      PredicateFactory<Integer> predicateFactory) {
    this.tag = tag;
    this.operation = operation;
    this.value = value;
    this.predicateFactory = predicateFactory;
  }

  /**
   * @return The value of the {@code tag} attribute
   */
  @Override
  public String getTag() {
    return tag;
  }

  /**
   * @return The value of the {@code operation} attribute
   */
  @Override
  public Operation getOperation() {
    return operation;
  }

  /**
   * @return The value of the {@code value} attribute
   */
  @Override
  public int getValue() {
    return value;
  }

  /**
   * @return The value of the {@code predicateFactory} attribute
   */
  @Override
  public PredicateFactory<Integer> getPredicateFactory() {
    return predicateFactory;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link IntegerRuleLeaf#getTag() tag} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param tag A new value for tag
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableIntegerRuleLeaf withTag(String tag) {
    if (this.tag.equals(tag)) return this;
    String newValue = Objects.requireNonNull(tag, "tag");
    return new ImmutableIntegerRuleLeaf(this, newValue, this.operation, this.value, this.predicateFactory);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link IntegerRuleLeaf#getOperation() operation} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param operation A new value for operation
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableIntegerRuleLeaf withOperation(Operation operation) {
    if (this.operation == operation) return this;
    Operation newValue = Objects.requireNonNull(operation, "operation");
    return new ImmutableIntegerRuleLeaf(this, this.tag, newValue, this.value, this.predicateFactory);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link IntegerRuleLeaf#getValue() value} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for value
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableIntegerRuleLeaf withValue(int value) {
    if (this.value == value) return this;
    return new ImmutableIntegerRuleLeaf(this, this.tag, this.operation, value, this.predicateFactory);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link IntegerRuleLeaf#getPredicateFactory() predicateFactory} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param predicateFactory A new value for predicateFactory
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableIntegerRuleLeaf withPredicateFactory(PredicateFactory<Integer> predicateFactory) {
    if (this.predicateFactory == predicateFactory) return this;
    PredicateFactory<Integer> newValue = Objects.requireNonNull(predicateFactory, "predicateFactory");
    return new ImmutableIntegerRuleLeaf(this, this.tag, this.operation, this.value, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableIntegerRuleLeaf} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableIntegerRuleLeaf
        && equalTo((ImmutableIntegerRuleLeaf) another);
  }

  private boolean equalTo(ImmutableIntegerRuleLeaf another) {
    return tag.equals(another.tag)
        && operation.equals(another.operation)
        && value == another.value
        && predicateFactory.equals(another.predicateFactory);
  }

  /**
   * Computes a hash code from attributes: {@code tag}, {@code operation}, {@code value}, {@code predicateFactory}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + tag.hashCode();
    h = h * 17 + operation.hashCode();
    h = h * 17 + value;
    h = h * 17 + predicateFactory.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code IntegerRuleLeaf} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "IntegerRuleLeaf{"
        + "tag=" + tag
        + ", operation=" + operation
        + ", value=" + value
        + ", predicateFactory=" + predicateFactory
        + "}";
  }

  /**
   * Construct a new immutable {@code IntegerRuleLeaf} instance.
   * @param tag The value for the {@code tag} attribute
   * @param operation The value for the {@code operation} attribute
   * @param value The value for the {@code value} attribute
   * @param predicateFactory The value for the {@code predicateFactory} attribute
   * @return An immutable IntegerRuleLeaf instance
   */
  public static ImmutableIntegerRuleLeaf of(String tag, Operation operation, int value, PredicateFactory<Integer> predicateFactory) {
    return new ImmutableIntegerRuleLeaf(tag, operation, value, predicateFactory);
  }

  /**
   * Creates an immutable copy of a {@link IntegerRuleLeaf} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable IntegerRuleLeaf instance
   */
  public static ImmutableIntegerRuleLeaf copyOf(IntegerRuleLeaf instance) {
    if (instance instanceof ImmutableIntegerRuleLeaf) {
      return (ImmutableIntegerRuleLeaf) instance;
    }
    return ImmutableIntegerRuleLeaf.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableIntegerRuleLeaf ImmutableIntegerRuleLeaf}.
   * @return A new ImmutableIntegerRuleLeaf builder
   */
  public static ImmutableIntegerRuleLeaf.Builder builder() {
    return new ImmutableIntegerRuleLeaf.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableIntegerRuleLeaf ImmutableIntegerRuleLeaf}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_TAG = 0x1L;
    private static final long INIT_BIT_OPERATION = 0x2L;
    private static final long INIT_BIT_VALUE = 0x4L;
    private static final long INIT_BIT_PREDICATE_FACTORY = 0x8L;
    private long initBits = 0xfL;

    private String tag;
    private Operation operation;
    private int value;
    private PredicateFactory<Integer> predicateFactory;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code IntegerRuleLeaf} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(IntegerRuleLeaf instance) {
      Objects.requireNonNull(instance, "instance");
      tag(instance.getTag());
      operation(instance.getOperation());
      value(instance.getValue());
      predicateFactory(instance.getPredicateFactory());
      return this;
    }

    /**
     * Initializes the value for the {@link IntegerRuleLeaf#getTag() tag} attribute.
     * @param tag The value for tag 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder tag(String tag) {
      this.tag = Objects.requireNonNull(tag, "tag");
      initBits &= ~INIT_BIT_TAG;
      return this;
    }

    /**
     * Initializes the value for the {@link IntegerRuleLeaf#getOperation() operation} attribute.
     * @param operation The value for operation 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder operation(Operation operation) {
      this.operation = Objects.requireNonNull(operation, "operation");
      initBits &= ~INIT_BIT_OPERATION;
      return this;
    }

    /**
     * Initializes the value for the {@link IntegerRuleLeaf#getValue() value} attribute.
     * @param value The value for value 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder value(int value) {
      this.value = value;
      initBits &= ~INIT_BIT_VALUE;
      return this;
    }

    /**
     * Initializes the value for the {@link IntegerRuleLeaf#getPredicateFactory() predicateFactory} attribute.
     * @param predicateFactory The value for predicateFactory 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder predicateFactory(PredicateFactory<Integer> predicateFactory) {
      this.predicateFactory = Objects.requireNonNull(predicateFactory, "predicateFactory");
      initBits &= ~INIT_BIT_PREDICATE_FACTORY;
      return this;
    }

    /**
     * Builds a new {@link ImmutableIntegerRuleLeaf ImmutableIntegerRuleLeaf}.
     * @return An immutable instance of IntegerRuleLeaf
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableIntegerRuleLeaf build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableIntegerRuleLeaf(null, tag, operation, value, predicateFactory);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_TAG) != 0) attributes.add("tag");
      if ((initBits & INIT_BIT_OPERATION) != 0) attributes.add("operation");
      if ((initBits & INIT_BIT_VALUE) != 0) attributes.add("value");
      if ((initBits & INIT_BIT_PREDICATE_FACTORY) != 0) attributes.add("predicateFactory");
      return "Cannot build IntegerRuleLeaf, some of required attributes are not set " + attributes;
    }
  }
}
