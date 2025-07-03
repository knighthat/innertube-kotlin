package me.knighthat.innertube.request.body.next;

import me.knighthat.innertube.request.body.NextBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Builder extends me.knighthat.innertube.request.body.Builder<NextBody> {

    /**
     * Can be used to retrieve related songs.
     * <p>
     * Combining with {@code continuation} string to achieve
     * more related songs
     *
     * @param videoId unique ID of a song
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder videoId( @Nullable String videoId );

    /**
     * Can be used along with {@code videoId} and {@code continuation}
     * to retrieve songs of initial song and overall genre of a playlist.
     *
     * @param playlistId unique ID of a playlist
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder playlistId( @Nullable String playlistId );
}