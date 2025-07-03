package me.knighthat.innertube.request.body.search.suggestions;

import me.knighthat.innertube.request.body.SearchSuggestionsBody;
import org.jetbrains.annotations.NotNull;

public interface Builder extends me.knighthat.innertube.request.body.Builder<SearchSuggestionsBody> {

    /**
     * @param input what to get suggestion for
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder input( @NotNull String input );
}
