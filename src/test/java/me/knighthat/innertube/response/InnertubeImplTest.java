package me.knighthat.innertube.response;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import me.knighthat.innertube.Constants;
import me.knighthat.innertube.Endpoints;
import me.knighthat.innertube.InnertubeProvider;
import me.knighthat.innertube.request.Request;
import me.knighthat.innertube.request.body.Context;
import me.knighthat.innertube.request.body.NextBody;
import me.knighthat.innertube.request.body.SearchSuggestionsBody;
import me.knighthat.internal.InnertubeImpl;

public class InnertubeImplTest {

    private static final @NotNull InnertubeImpl innertube = new InnertubeImpl();

    @BeforeAll
    static void setup() {
        innertube.setProvider( new InnertubeProvider() );
    }

    @Test
    void testSendRequestToYouTube() {
        // Fabricating & sending request within [Assertions.assertDoesNotThrow]
        // to avoid try/catch
        Response response = assertDoesNotThrow( () -> {
            NextBody body = NextBody.builder( Context.WEB_DEFAULT )
                                    .videoId( "lYBUbBu4W08" )
                                    .build();
            return innertube.sendRequest(
                    Request.POST,
                    Constants.YOUTUBE_URL,
                    Endpoints.NEXT,
                    body,
                    Collections.emptyMap(),
                    false
            );
        });

        assertEquals( 200, response.getResponseCode() );
        assertFalse( response.getResponseBody().isBlank() );
    }

    @Test
    void testSendRequestToYouTubeMusic() {
        Response response = assertDoesNotThrow( () -> {
            SearchSuggestionsBody body = SearchSuggestionsBody.builder( Context.WEB_REMIX_DEFAULT )
                                                              .input( "million dollar baby" )
                                                              .build();
            return innertube.sendRequest(
                    Request.POST,
                    Constants.YOUTUBE_MUSIC_URL,
                    Endpoints.NEXT,
                    body,
                    Collections.emptyMap(),
                    false
            );
        });

        assertEquals( 200, response.getResponseCode() );
        assertFalse( response.getResponseBody().isBlank() );
    }
}
