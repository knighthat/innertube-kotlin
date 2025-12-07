package me.knighthat.innertube.request.body;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import me.knighthat.innertube.SearchFilter;
import me.knighthat.innertube.request.body.search.Builder;

@Getter
class SearchBodyBuilder implements Builder {

    private final Context context;
    private       String  query;
    @MagicConstant(valuesFromClass = SearchFilter.class)        // Place it here to prevent warning from IDE
    private       String  params;
    private       String  continuation;

    SearchBodyBuilder( @NotNull Context context ) {
        this.context = context;
        this.params = null;
    }

    @Override
    public @NotNull Builder query( @NotNull String query ) {
        this.query = query;
        return this;
    }

    @Override
    public @NotNull Builder params( @Nullable String params ) {
        this.params = params;
        return this;
    }

    @Override
    public @NotNull Builder continuation( @NotNull String continuation ) {
        this.continuation = continuation;
        return this;
    }

    @Override
    public @NotNull SearchBody build() {
        assert query != null || continuation != null;
        return new SearchBody( query, params, continuation, context );
    }
}
