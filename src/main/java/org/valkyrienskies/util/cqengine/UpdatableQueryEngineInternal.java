package org.valkyrienskies.util.cqengine;

public interface UpdatableQueryEngineInternal<O> extends UpdatableQueryEngine<O> {

    public boolean isMutable();

}
