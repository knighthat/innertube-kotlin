package me.knighthat.innertube.request.body;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.innertube.request.body.search.suggestions.Builder;

class SearchSuggestionsBodyBuilder implements Builder {

    private final Context context;
    private       String  input;

    SearchSuggestionsBodyBuilder( @NotNull Context context ) {
        this.context = context;
    }

    @Override
    public @NotNull Builder input( @NotNull String input ) {
        this.input = input;
        return this;
    }

    @Override
    public @NotNull SearchSuggestionsBodyBuilder params( @Nullable String params ) {
        return this;
    }


    @Override
    public @NotNull Builder formData( @NotNull String... selectedValues ) {
        return this;
    }

    @Override
    public @NotNull SearchSuggestionsBody build() {
        assert input != null;
        return new SearchSuggestionsBody( input, context );
    }
}
