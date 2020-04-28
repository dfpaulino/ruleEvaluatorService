package org.farmtec.res.rules.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;

/**
 * Immutable implementation of {@link StringRuleLeaf}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableStringRuleLeaf.builder()}.
 * Use the static factory method to create immutable instances:
 * {@code ImmutableStringRuleLeaf.of()}.
 */
@SuppressWarnings("all")
public final class ImmutableStringRuleLeaf extends StringRuleLeaf {
  private final String tag;
  private final Operation operation;
  private final String value;
  private final PredicateFactory<String> predicateFactory;

  private ImmutableStringRuleLeaf(
      String tag,
      Operation operation,
      String value,
      PredicateFactory<String> predicateFactory) {
    this.tag = Objects.requireNonNull(tag, "tag");
    this.operation = Objects.requireNonNull(operation, "operation");
    this.value = Objects.requireNonNull(value, "value");
    this.predicateFactory = Objects.requireNonNull(predicateFactory, "predicateFactory");
  }

  private ImmutableStringRuleLeaf(
      ImmutableStringRuleLeaf original,
      String tag,
      Operation operation,
      String value,
      PredicateFactory<String> predicateFactory) {
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
  public String getValue() {
    return value;
  }

  /**
   * @return The value of the {@code predicateFactory} attribute
   */
  @Override
  public PredicateFactory<String> getPredicateFactory() {
    return predicateFactory;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link StringRuleLeaf#getTag() tag} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param tag A new value for tag
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableStringRuleLeaf withTag(String tag) {
    if (this.tag.equals(tag)) return this;
    String newValue = Objects.requireNonNull(tag, "tag");
    return new ImmutableStringRuleLeaf(this, newValue, this.operation, this.value, this.predicateFactory);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link StringRuleLeaf#getOperation() operation} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param operation A new value for operation
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableStringRuleLeaf withOperation(Operation operation) {
    if (this.operation == operation) return this;
    Operation newValue = Objects.requireNonNull(operation, "operation");
    return new ImmutableStringRuleLeaf(this, this.tag, newValue, this.value, this.predicateFactory);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link StringRuleLeaf#getValue() value} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for value
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableStringRuleLeaf withValue(String value) {
    if (this.value.equals(value)) return this;
    String newValue = Objects.requireNonNull(value, "value");
    return new ImmutableStringRuleLeaf(this, this.tag, this.operation, newValue, this.predicateFactory);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link StringRuleLeaf#getPredicateFactory() predicateFactory} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param predicateFactory A new value for predicateFactory
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableStringRuleLeaf withPredicateFactory(PredicateFactory<String> predicateFactory) {
    if (this.predicateFactory == predicateFactory) return this;
    PredicateFactory<String> newValue = Objects.requireNonNull(predicateFactory, "predicateFactory");
    return new ImmutableStringRuleLeaf(this, this.tag, this.operation, this.value, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableStringRuleLeaf} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableStringRuleLeaf
        && equalTo((ImmutableStringRuleLeaf) another);
  }

  private boolean equalTo(ImmutableStringRuleLeaf another) {
    return tag.equals(another.tag)
        && operation.equals(another.operation)
        && value.equals(another.value)
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
    h = h * 17 + value.hashCode();
    h = h * 17 + predicateFactory.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code StringRuleLeaf} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "StringRuleLeaf{"
        + "tag=" + tag
        + ", operation=" + operation
        + ", value=" + value
        + ", predicateFactory=" + predicateFactory
        + "}";
  }

  /**
   * Construct a new immutable {@code StringRuleLeaf} instance.
   * @param tag The value for the {@code tag} attribute
   * @param operation The value for the {@code operation} attribute
   * @param value The value for the {@code value} attribute
   * @param predicateFactory The value for the {@code predicateFactory} attribute
   * @return An immutable StringRuleLeaf instance
   */
  public static ImmutableStringRuleLeaf of(String tag, Operation operation, String value, PredicateFactory<String> predicateFactory) {
    return new ImmutableStringRuleLeaf(tag, operation, value, predicateFactory);
  }

  /**
   * Creates an immutable copy of a {@link StringRuleLeaf} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable StringRuleLeaf instance
   */
  public static ImmutableStringRuleLeaf copyOf(StringRuleLeaf instance) {
    if (instance instanceof ImmutableStringRuleLeaf) {
      return (ImmutableStringRuleLeaf) instance;
    }
    return ImmutableStringRuleLeaf.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableStringRuleLeaf ImmutableStringRuleLeaf}.
   * @return A new ImmutableStringRuleLeaf builder
   */
  public static ImmutableStringRuleLeaf.Builder builder() {
    return new ImmutableStringRuleLeaf.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableStringRuleLeaf ImmutableStringRuleLeaf}.
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
    private String value;
    private PredicateFactory<String> predicateFactory;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code StringRuleLeaf} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(StringRuleLeaf instance) {
      Objects.requireNonNull(instance, "instance");
      tag(instance.getTag());
      operation(instance.getOperation());
      value(instance.getValue());
      predicateFactory(instance.getPredicateFactory());
      return this;
    }

    /**
     * Initializes the value for the {@link StringRuleLeaf#getTag() tag} attribute.
     * @param tag The value for tag 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder tag(String tag) {
      this.tag = Objects.requireNonNull(tag, "tag");
      initBits &= ~INIT_BIT_TAG;
      return this;
    }

    /**
     * Initializes the value for the {@link StringRuleLeaf#getOperation() operation} attribute.
     * @param operation The value for operation 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder operation(Operation operation) {
      this.operation = Objects.requireNonNull(operation, "operation");
      initBits &= ~INIT_BIT_OPERATION;
      return this;
    }

    /**
     * Initializes the value for the {@link StringRuleLeaf#getValue() value} attribute.
     * @param value The value for value 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder value(String value) {
      this.value = Objects.requireNonNull(value, "value");
      initBits &= ~INIT_BIT_VALUE;
      return this;
    }

    /**
     * Initializes the value for the {@link StringRuleLeaf#getPredicateFactory() predicateFactory} attribute.
     * @param predicateFactory The value for predicateFactory 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder predicateFactory(PredicateFactory<String> predicateFactory) {
      this.predicateFactory = Objects.requireNonNull(predicateFactory, "predicateFactory");
      initBits &= ~INIT_BIT_PREDICATE_FACTORY;
      return this;
    }

    /**
     * Builds a new {@link ImmutableStringRuleLeaf ImmutableStringRuleLeaf}.
     * @return An immutable instance of StringRuleLeaf
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableStringRuleLeaf build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableStringRuleLeaf(null, tag, operation, value, predicateFactory);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_TAG) != 0) attributes.add("tag");
      if ((initBits & INIT_BIT_OPERATION) != 0) attributes.add("operation");
      if ((initBits & INIT_BIT_VALUE) != 0) attributes.add("value");
      if ((initBits & INIT_BIT_PREDICATE_FACTORY) != 0) attributes.add("predicateFactory");
      return "Cannot build StringRuleLeaf, some of required attributes are not set " + attributes;
    }
  }
}
