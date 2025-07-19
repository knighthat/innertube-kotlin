package me.knighthat.innertube.request.body;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import me.knighthat.innertube.request.body.next.Builder;

@Getter
class NextBodyBuilder implements Builder {

    private final Context context;
    private       String  videoId;
    private       String  playlistId;
    private       String  params;

    NextBodyBuilder( @NotNull Context context ) {
        this.context = context;
        this.videoId = null;
        this.playlistId = null;
        this.params = null;
    }

    @Override
    public @NotNull Builder videoId( @Nullable String videoId ) {
        this.videoId = videoId;
        return this;
    }

    @Override
    public @NotNull Builder playlistId( @Nullable String playlistId ) {
        this.playlistId = playlistId;
        return this;
    }

    @Override
    public @NotNull Builder params( @Nullable String params ) {
        this.params = params;
        return this;
    }

    @Override
    public @NotNull Builder formData( @NotNull String... selectedValues ) {
        return this;
    }

    @Override
    public @NotNull NextBody build() {
        // Either of them must be a non-null value
        assert videoId != null || playlistId != null;
        return new NextBody(videoId, playlistId, params, context);
    }
}
