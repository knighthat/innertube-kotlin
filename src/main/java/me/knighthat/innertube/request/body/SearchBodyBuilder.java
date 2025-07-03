package me.knighthat.innertube.request.body;

import lombok.Getter;
import me.knighthat.innertube.SearchFilter;
import me.knighthat.innertube.request.body.search.Builder;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
class SearchBodyBuilder implements Builder {

    private final Context context;
    private       String  query;
    @MagicConstant(valuesFromClass = SearchFilter.class)        // Place it here to prevent warning from IDE
    private       String  params;

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
    public @NotNull SearchBody build() {
        assert query != null;
        return new SearchBody( query, params, context );
    }
}
