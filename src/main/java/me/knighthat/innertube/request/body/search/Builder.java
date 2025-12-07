package me.knighthat.innertube.request.body.search;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.innertube.SearchFilter;
import me.knighthat.innertube.request.body.SearchBody;

public interface Builder {

    /**
     * @param query what to search
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder query( @NotNull String query );

    /**
     * Mostly used to filter what type of content should be in
     * the search. Usually for search only for videos, artists, or albums, etc.
     *
     * @param params search filter
     *
     * @return {@link Builder} for further development
     *
     * @see SearchFilter
     */
    @NotNull Builder params( @MagicConstant(valuesFromClass = SearchFilter.class) @Nullable String params );

    @NotNull Builder continuation( String continuation );

    /**
     * Finalize all values.
     *
     * @return new instance if {@link SearchBody} with provided values
     */
    @NotNull SearchBody build();
}
