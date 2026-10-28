package com.example.melora.core.designsystem

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.melora.core.designsystem.components.MeloraText

/**
 * Centralized AppTypography tokens and composables for Melora.
 */
object AppTypography {

    // region Theme typography

    // Letter spacing ("tracking") per role. These are fractions of an sp, which SSP can't
    // express (it only has whole numbers), so they are plain sp values. Large text is
    // tightened (negative), small text is loosened (positive) to stay readable.
    private val TrackingDisplayLarge = (-0.5).sp
    private val TrackingDisplayMedium = (-0.25).sp
    private val TrackingHeadlineLarge = (-0.2).sp
    private val TrackingTight = 0.1.sp
    private val TrackingBody = 0.2.sp
    private val TrackingSmall = 0.3.sp
    private val TrackingCaption = 0.5.sp

    /**
     * Builds the Material 3 [Typography] for [MaterialTheme].
     *
     * Sizes and line heights come from [scale]; weights, tracking and font family are
     * defined here. It is a plain (non-composable) function, so call it once from the
     * theme inside `remember(dimensions)` instead of rebuilding it on every recomposition.
     *
     * @param scale sizes for every role, from `Dimensions.typeScale`.
     * @param fontFamily family applied to every role; change it here to switch fonts app-wide.
     */
    /**
     * Composable helper to resolve Material 3 [Typography] from the current theme's [LocalDimensions].
     */
    @Composable
    @ReadOnlyComposable
    fun toMaterial3Typography(
        fontFamily: FontFamily = FontFamily.SansSerif
    ): Typography = material3Typography(LocalDimensions.current.typeScale, fontFamily)

    fun material3Typography(
        scale: TypeScale,
        fontFamily: FontFamily = FontFamily.SansSerif,
    ): Typography {
        // Builds one role's TextStyle from its size metric plus the weight and tracking below.
        fun style(
            metric: TypeMetric,
            weight: FontWeight,
            letterSpacing: TextUnit = TextUnit.Unspecified,
        ) = TextStyle(
            fontFamily = fontFamily,
            fontWeight = weight,
            fontSize = metric.fontSize,
            lineHeight = metric.lineHeight,
            letterSpacing = letterSpacing,
        )

        return Typography(
            displayLarge = style(scale.displayLarge, FontWeight.ExtraBold, TrackingDisplayLarge),
            displayMedium = style(scale.displayMedium, FontWeight.Bold, TrackingDisplayMedium),
            displaySmall = style(scale.displaySmall, FontWeight.Bold),
            headlineLarge = style(scale.headlineLarge, FontWeight.Bold, TrackingHeadlineLarge),
            headlineMedium = style(scale.headlineMedium, FontWeight.SemiBold),
            headlineSmall = style(scale.headlineSmall, FontWeight.SemiBold),
            titleLarge = style(scale.titleLarge, FontWeight.SemiBold),
            titleMedium = style(scale.titleMedium, FontWeight.Medium, TrackingTight),
            titleSmall = style(scale.titleSmall, FontWeight.Medium, TrackingTight),
            bodyLarge = style(scale.bodyLarge, FontWeight.Normal, TrackingBody),
            bodyMedium = style(scale.bodyMedium, FontWeight.Normal, TrackingBody),
            bodySmall = style(scale.bodySmall, FontWeight.Normal, TrackingSmall),
            labelLarge = style(scale.labelLarge, FontWeight.SemiBold, TrackingTight),
            labelMedium = style(scale.labelMedium, FontWeight.Medium, TrackingSmall),
            labelSmall = style(scale.labelSmall, FontWeight.Medium, TrackingCaption),
        )
    }

    // endregion

    // region Core

    /**
     * Single implementation behind every role-based composable below.
     *
     * [style] is the role's base style (e.g. `typography.titleLarge`). The overrides are
     * applied with [TextStyle.merge], which ignores unspecified/null arguments, so a
     * caller's `fontSize`, `fontWeight` or `textDecoration` only wins when actually
     * provided and the role's own values are kept otherwise.
     */
    @Composable
    private fun StyledText(
        text: String,
        style: TextStyle,
        modifier: Modifier,
        color: Color,
        fontSize: TextUnit,
        fontWeight: FontWeight?,
        textDecoration: TextDecoration?,
        textAlign: TextAlign?,
        maxLines: Int,
        overflow: TextOverflow,
    ) {
        MeloraText(
            text = text,
            modifier = modifier,
            color = color,
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = overflow,
            style = style.merge(
                fontSize = fontSize,
                fontWeight = fontWeight,
                textDecoration = textDecoration,
            ),
        )
    }

    /**
     * Fully customizable text for cases no role fits.
     *
     * Mirrors Material's `Text` parameters. [style] defaults to [LocalTextStyle], so it
     * inherits the style set by parents such as buttons and chips. Prefer the role-based
     * composables below for regular content.
     */
    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        fontStyle: FontStyle? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        lineHeight: TextUnit = TextUnit.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
        style: TextStyle = LocalTextStyle.current,
    ) {
        MeloraText(
            text = text,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            style = style,
        )
    }

    // endregion

    // region Display & hero
    // Very large, expressive text used sparingly (onboarding, big numbers, empty states).

    /** Largest hero text, e.g. splash or onboarding headings. Role: `displayLarge`. */
    @Composable
    fun DisplayLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.displayLarge, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Large hero text for prominent screen headings. Role: `displayMedium`. */
    @Composable
    fun DisplayMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.displayMedium, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Smaller hero text, e.g. big numbers or summary values. Role: `displaySmall`. */
    @Composable
    fun DisplaySmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.displaySmall, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    // endregion

    // region Headlines
    // Screen and section headers.

    /** Main screen or section headline. Role: `headlineLarge`. */
    @Composable
    fun HeadlineLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.headlineLarge, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Section header within a screen. Role: `headlineMedium`. */
    @Composable
    fun HeadlineMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.headlineMedium, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Minor section header. Role: `headlineSmall`. */
    @Composable
    fun HeadlineSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.headlineSmall, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    // Heading aliases
    @Composable
    fun HeadingLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = HeadlineLarge(text, modifier, color, fontSize, fontWeight, textDecoration, textAlign, maxLines, overflow)

    @Composable
    fun HeadingMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = HeadlineMedium(text, modifier, color, fontSize, fontWeight, textDecoration, textAlign, maxLines, overflow)

    @Composable
    fun HeadingSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = HeadlineSmall(text, modifier, color, fontSize, fontWeight, textDecoration, textAlign, maxLines, overflow)

    // endregion

    // region Titles
    // TitleLarge / TitleMedium keep their previous default weights (Bold / SemiBold),
    // which differ from the theme's. Pass `fontWeight = null` to use the theme weight.
    // (The default is nullable on purpose: null means "keep the role's own weight".)

    /** Top app bar titles and large card titles. Role: `titleLarge`. Defaults to Bold (the theme uses SemiBold); pass `fontWeight = null` for the theme weight. */
    @Composable
    fun TitleLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = FontWeight.Bold,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.titleLarge, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Card, list-item and dialog titles. Role: `titleMedium`. Defaults to SemiBold (the theme uses Medium); pass `fontWeight = null` for the theme weight. */
    @Composable
    fun TitleMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = FontWeight.SemiBold,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.titleMedium, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Compact titles, e.g. in dense lists. Role: `titleSmall`. */
    @Composable
    fun TitleSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.titleSmall, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    // endregion

    // region Body
    // Regular reading text.

    /** Primary reading text for longer content. Role: `bodyLarge`. */
    @Composable
    fun BodyLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.bodyLarge, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Default body text for most content. Role: `bodyMedium`. */
    @Composable
    fun BodyMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.bodyMedium, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Secondary body text and short descriptions. Role: `bodySmall`. */
    @Composable
    fun BodySmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.bodySmall, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    // endregion

    // region Labels, buttons & badges
    // Short, functional text attached to controls.

    /** Prominent labels, e.g. tabs and larger chips. Role: `labelLarge`. */
    @Composable
    fun LabelLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.labelLarge, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Standard labels and metadata. Role: `labelMedium`. */
    @Composable
    fun LabelMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.labelMedium, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Smallest labels, e.g. timestamps and tiny tags. Role: `labelSmall`. */
    @Composable
    fun LabelSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.labelSmall, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Text inside buttons. Role: `labelLarge`, Bold by default. Does not offer `textDecoration`, since button text is never underlined. */
    @Composable
    fun ButtonText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = FontWeight.Bold,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.labelLarge, modifier, color, fontSize,
        fontWeight, null, textAlign, maxLines, overflow,
    )

    /** Text inside badges and counters. Role: `labelSmall`, Bold, single line by default. */
    @Composable
    fun BadgeText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = FontWeight.Bold,
        textAlign: TextAlign? = null,
        maxLines: Int = 1,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.labelSmall, modifier, color, fontSize,
        fontWeight, null, textAlign, maxLines, overflow,
    )

    // endregion

    // region Subtext, captions & helper text
    // De-emphasized text: defaults to a dimmed `onSurfaceVariant` color.

    // Opacity applied to `onSurfaceVariant` so secondary text is visibly quieter than primary text.
    // Higher = more prominent. Tweak here to restyle all de-emphasized text at once.
    private const val SubTextAlpha = 0.75f
    private const val CaptionAlpha = 0.6f
    private const val HelperTextAlpha = 0.7f

    /** De-emphasized supporting text below a title. Role: `labelMedium` at 75% `onSurfaceVariant` by default. */
    @Composable
    fun SubText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = SubTextAlpha),
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.labelMedium, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Small captions under images or inputs. Role: `labelSmall` at 60% `onSurfaceVariant`, Normal weight by default. */
    @Composable
    fun Caption(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = CaptionAlpha),
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = FontWeight.Normal,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.labelSmall, modifier, color, fontSize,
        fontWeight, textDecoration, textAlign, maxLines, overflow,
    )

    /** Hints and validation messages under form fields. Role: `bodySmall` at 70% `onSurfaceVariant`. */
    @Composable
    fun HelperText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = HelperTextAlpha),
        fontSize: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ) = StyledText(
        text, MaterialTheme.typography.bodySmall, modifier, color, fontSize,
        fontWeight, null, textAlign, maxLines, overflow,
    )

    // endregion
}