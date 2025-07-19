package me.knighthat.innertube.request.body;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import lombok.Getter;
import me.knighthat.innertube.request.body.browse.TypeBuilder;

@Getter
class BrowseBodyBuilder implements Builder<BrowseBody>, TypeBuilder {

    private final Context  context;
    private       String   browseId;
    private       String   params;
    private       String   continuation;
    private       FormData formData;

    BrowseBodyBuilder( @NotNull Context context ) {
        this.context = context;
        browseId = null;
        params = null;
        continuation = null;
    }

    @Override
    public @NotNull BrowseBodyBuilder params( @Nullable String params ) {
        this.params = params;
        return this;
    }

    @Override
    public @NotNull Builder<BrowseBody> formData( @NotNull String... selectedValues ) {
        this.formData = new FormData(List.of(selectedValues));
        return this;
    }

    @Override
    public @NotNull BrowseBody build() {
        // Either 1 of these 2 is a non-null value
        assert browseId != null || continuation != null;
        return new BrowseBody(browseId, params, continuation, formData, context);
    }

    @Override
    public @NotNull BrowseBodyBuilder browseId( @NotNull String browseId ) {
        this.browseId = browseId;
        return this;
    }

    @Override
    public @NotNull BrowseBodyBuilder continuation( @NotNull String continuation ) {
        this.continuation = continuation;
        return this;
    }
}
