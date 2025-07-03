package me.knighthat.innertube.request.body.browse;

import me.knighthat.innertube.request.body.BrowseBody;
import me.knighthat.innertube.request.body.Builder;
import org.jetbrains.annotations.NotNull;

public interface TypeBuilder {

    /**
     * Used to search for a specific playlist, album, or artist.
     *
     * @param browseId unique id of album, artist, or a playlist
     *
     * @return {@link Builder} to set params or to build
     */
    @NotNull Builder<BrowseBody> browseId( @NotNull String browseId );

    /**
     * Used to retrieve next songs of a playlist.
     *
     * @param continuation unique string to get next songs from a playlist
     *
     * @return {@link Builder} to set params or to build
     */
    @NotNull Builder<BrowseBody> continuation( @NotNull String continuation );
}
