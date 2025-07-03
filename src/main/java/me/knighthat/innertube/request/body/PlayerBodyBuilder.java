package me.knighthat.innertube.request.body;

import lombok.Getter;
import me.knighthat.innertube.request.body.player.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
class PlayerBodyBuilder implements Builder {

    private final Context                               context;
    private       String                                videoId;
    private       String                                params;
    private       Boolean                               racyCheckOk;
    private       Boolean                               contentCheckOk;
    private       PlayerBody.ServiceIntegrityDimensions serviceIntegrityDimensions;

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
        return serviceIntegrityDimensions( new PlayerBody.ServiceIntegrityDimensions( poToken ) );
    }

    @Override
    public @NotNull Builder params( @Nullable String params ) {
        this.params = params;
        return this;
    }

    @Override
    public @NotNull PlayerBody build() {
        assert videoId != null;
        return new PlayerBody( videoId, params, racyCheckOk, contentCheckOk, serviceIntegrityDimensions, context );
    }
}
