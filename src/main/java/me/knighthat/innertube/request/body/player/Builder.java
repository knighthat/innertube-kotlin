package me.knighthat.innertube.request.body.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.innertube.request.body.PlayerBody;

public interface Builder extends me.knighthat.innertube.request.body.Builder<PlayerBody> {

    /**
     * Video to obtain playback URL.
     *
     * @param videoId unique ID of video
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder videoId( @NotNull String videoId );

    /**
     * Unknown, but seems to help with the response.
     */
    @NotNull Builder racyCheckOk( boolean racyCheckOk );

    /**
     * Unknown, but seems to help with the response.
     */
    @NotNull Builder contentCheckOk( boolean contentCheckOk );

    /**
     * Although optional, adding this reduces chances of
     * blocking significantly, especially on mobile devices.
     * <p>
     * A 12-character string is all required.
     *
     * @param serviceIntegrityDimensions an instance that holds poToken
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder serviceIntegrityDimensions( @NotNull PlayerBody.ServiceIntegrityDimensions serviceIntegrityDimensions );

    @NotNull Builder playbackContext( @Nullable PlayerBody.ContentPlaybackContext playbackContext );

    /**
     * Shortcut for {@code serviceIntegrityDimensions}
     *
     * @param poToken 12-character string
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder poToken( @NotNull String poToken );

    /**
     * @param cpn A string of 12 random characters
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder cpn( @NotNull String cpn );

    /**
     * A required parameter for HTML5 requests.
     *
     * @param signatureTimestamp the signature timestamp of the base JavaScript player file
     *
     * @return {@link Builder} for further development
     */
    @NotNull Builder signatureTimestamp( int signatureTimestamp );
}
