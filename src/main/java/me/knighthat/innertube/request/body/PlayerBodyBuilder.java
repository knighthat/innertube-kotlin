package me.knighthat.innertube.request.body;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import me.knighthat.innertube.request.body.player.Builder;

@Getter
class PlayerBodyBuilder implements Builder {

    private final Context                               context;
    private       String                                videoId;
    private       String                                params;
    private       Boolean                               racyCheckOk;
    private       Boolean                               contentCheckOk;
    private       PlayerBody.ServiceIntegrityDimensions serviceIntegrityDimensions;
    private       String                                cpn;
    private       Integer                               signatureTimestamp;

    PlayerBodyBuilder( @NotNull Context context ) {
        this.context = context;
        this.params = null;
        this.racyCheckOk = true;
        this.contentCheckOk = true;
    }

    @Override
    public @NotNull Builder videoId( @NotNull String videoId ) {
        this.videoId = videoId;
        return this;
    }

    @Override
    public @NotNull Builder racyCheckOk( boolean racyCheckOk ) {
        this.racyCheckOk = racyCheckOk;
        return this;
    }

    @Override
    public @NotNull Builder contentCheckOk( boolean contentCheckOk ) {
        this.contentCheckOk = contentCheckOk;
        return this;
    }

    @Override
    public @NotNull Builder serviceIntegrityDimensions( @Nullable PlayerBody.ServiceIntegrityDimensions serviceIntegrityDimensions ) {
        this.serviceIntegrityDimensions = serviceIntegrityDimensions;
        return this;
    }

    @Override
    public @NotNull Builder poToken( @NotNull String poToken ) {
        return serviceIntegrityDimensions(new PlayerBody.ServiceIntegrityDimensions(poToken));
    }

    @Override
    public @NotNull Builder cpn( @NotNull String cpn ) {
        this.cpn = cpn;
        return this;
    }

    @Override
    public @NotNull Builder signatureTimestamp( @Nullable Integer signatureTimestamp ) {
        this.signatureTimestamp = signatureTimestamp;
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
    public @NotNull PlayerBody build() {
        assert videoId != null;

        PlayerBody.ContentPlaybackContext playbackContext = this.signatureTimestamp != null
            ? new PlayerBody.ContentPlaybackContext("HTML5_PREF_WANTS", this.signatureTimestamp)
            : null;

        return new PlayerBody(videoId, params, racyCheckOk, contentCheckOk, serviceIntegrityDimensions, playbackContext, context);
    }
}
