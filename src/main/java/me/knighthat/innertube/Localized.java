package me.knighthat.innertube;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a getter returns a string that may be localized based on the current request context.
 * <p>
 * The returned value should not be assumed to be constant or language-neutral
 */
@Retention( RetentionPolicy.SOURCE )
@Target( {ElementType.METHOD, ElementType.FIELD} )
public @interface Localized {
}
