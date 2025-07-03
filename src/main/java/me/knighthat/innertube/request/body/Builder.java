package me.knighthat.innertube.request.body;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Builder<T extends RequestBody> {

    /**
     * Optional, may affect result from YT/YTM
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder<T> params( @Nullable String params );

    /**
     * Finalize all values.
     *
     * @return new instance of {@link T} with provided values
     */
    @NotNull T build();
}
