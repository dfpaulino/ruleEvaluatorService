package org.farmtec.res.service.model;

import org.immutables.value.Value;
import org.immutables.value.Value.Parameter;

@Value.Immutable
public abstract class Action {
    @Parameter
    public abstract String getType();

    @Parameter
    public abstract String getData();

    @Parameter
    public abstract int getPriority();

}
