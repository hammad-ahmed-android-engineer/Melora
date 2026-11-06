import os
import math
import numpy as np
from PIL import Image, ImageDraw, ImageFont, ImageFilter

# Directories
PROJECT_DIR = r"d:\Programming\Android\GitHub\Melora"
BRAIN_DIR = r"C:\Users\Hamiye\.gemini\antigravity\brain\f7b84822-4a93-40d1-aa82-15ed136b8bc5"
USER_UPLOADS = os.path.join(BRAIN_DIR, ".user_uploaded")
OUTPUT_DIR = os.path.join(PROJECT_DIR, "play_store_assets")
ARTIFACT_OUTPUT_DIR = os.path.join(BRAIN_DIR, "play_store_assets")

os.makedirs(OUTPUT_DIR, exist_ok=True)
os.makedirs(ARTIFACT_OUTPUT_DIR, exist_ok=True)

FEATURE_BG_V2 = os.path.join(BRAIN_DIR, "melora_play_store_feature_graphic_v2_1793905222625.jpg")

RAW_SCREENSHOTS = {
    "songs": os.path.join(USER_UPLOADS, "media_1793904982067_7b0f061f.jpg"),
    "folders": os.path.join(USER_UPLOADS, "media_1793905021889_a5e906a4.png"),
    "albums": os.path.join(USER_UPLOADS, "media_1793905030094_f013560d.jpg"),
    "playlists": os.path.join(USER_UPLOADS, "media_1793905037134_31a59653.png"),
    "player": os.path.join(USER_UPLOADS, "media_1793905053016_b16bdb2a.jpg")
}

FONT_BOLD_PATH = r"C:\WINDOWS\Fonts\segoeuib.ttf"
FONT_REG_PATH = r"C:\WINDOWS\Fonts\segoeui.ttf"
FONT_SEMIBOLD_PATH = r"C:\WINDOWS\Fonts\seguisb.ttf"

def get_font(path, size):
    try:
        return ImageFont.truetype(path, size)
    except:
        return ImageFont.load_default()

def draw_clean_status_bar(im_draw, width, height, bg_color=(16, 23, 20)):
    im_draw.rectangle([0, 0, width, height], fill=bg_color)
    time_font = get_font(FONT_BOLD_PATH, int(height * 0.44))
    im_draw.text((int(width * 0.08), int(height * 0.22)), "10:00", fill=(240, 245, 242), font=time_font)
    
    r_margin = width - int(width * 0.08)
    y_center = height // 2
    
    bat_w = int(height * 0.58)
    bat_h = int(height * 0.32)
    bat_x = r_margin - bat_w
    bat_y = y_center - bat_h // 2
    im_draw.rounded_rectangle([bat_x, bat_y, bat_x + bat_w, bat_y + bat_h], radius=3, outline=(220, 230, 225), width=2)
    im_draw.rectangle([bat_x + bat_w, bat_y + 3, bat_x + bat_w + 3, bat_y + bat_h - 3], fill=(220, 230, 225))
    im_draw.rounded_rectangle([bat_x + 3, bat_y + 3, bat_x + bat_w - 3, bat_y + bat_h - 3], radius=2, fill=(114, 214, 160))
    
    g_font = get_font(FONT_BOLD_PATH, int(height * 0.36))
    g_x = bat_x - int(height * 0.65)
    im_draw.text((g_x, int(height * 0.26)), "5G", fill=(220, 230, 225), font=g_font)
    
    wifi_x = g_x - int(height * 0.55)
    for i in range(3):
        h_bar = int(height * 0.16) + i * 4
        x_bar = wifi_x + i * 5
        im_draw.rectangle([x_bar, y_center + 6 - h_bar, x_bar + 3, y_center + 6], fill=(220, 230, 225))

def clean_screenshot(raw_path):
    img = Image.open(raw_path).convert("RGBA")
    w, h = img.size
    top_bar_height = int(h * 0.052)
    sample_rect = img.crop((10, top_bar_height + 5, w - 10, top_bar_height + 15))
    avg_color = tuple(int(x) for x in np.array(sample_rect).mean(axis=(0, 1))[:3])
    draw = ImageDraw.Draw(img)
    draw_clean_status_bar(draw, w, top_bar_height, bg_color=avg_color)
    return img

def render_melora_logo(size=512):
    """Renders high-res Melora emblem: vinyl disc + mint music note + equalizer bars"""
    scale = size / 108.0
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Outer circle background gradient
    center = size / 2.0
    radius = size * 0.46
    
    # Draw soft radial circle
    for r in range(int(radius), 0, -2):
        frac = r / radius
        # Gradient #234937 to #0D1410
        r_col = int(35 * frac + 13 * (1 - frac))
        g_col = int(73 * frac + 20 * (1 - frac))
        b_col = int(55 * frac + 16 * (1 - frac))
        draw.ellipse([center - r, center - r, center + r, center + r], fill=(r_col, g_col, b_col, 255))
        
    # Concentric vinyl groove rings
    ring_radii = [radius * 0.85, radius * 0.70, radius * 0.55, radius * 0.40]
    for idx, rr in enumerate(ring_radii):
        alpha = int(40 + idx * 25)
        ring_col = (114, 214, 160, alpha)
        draw.ellipse([center - rr, center - rr, center + rr, center + rr], outline=ring_col, width=max(1, int(2 * scale * 0.4)))
        
    # Floating music note
    note_draw = ImageDraw.Draw(img)
    # Music note coordinates scaled from 108dp viewport
    # Left note head: center ~(36.5, 73.5), radius ~10.5
    # Right note head: center ~(57.5, 67.5), radius ~10.5
    # Left stem: (44, 38) to (47, 73.5)
    # Right stem: (65, 32) to (68, 67.5)
    # Top beam: (44, 36) -> (68, 30) -> (68, 37) -> (44, 43)
    
    def pt(x, y):
        return (x * scale, y * scale)
        
    # Top beam
    draw.polygon([pt(44, 36), pt(68, 30), pt(68, 38), pt(44, 44)], fill=(255, 255, 255, 255))
    # Stems
    draw.rectangle([pt(44, 40)[0], pt(44, 40)[1], pt(47.5, 72)[0], pt(47.5, 72)[1]], fill=(220, 245, 235, 255))
    draw.rectangle([pt(64.5, 34)[0], pt(64.5, 34)[1], pt(68, 66)[0], pt(68, 66)[1]], fill=(160, 235, 195, 255))
    
    # Left note head ellipse
    draw.ellipse([pt(27, 65)[0], pt(27, 65)[1], pt(47, 81)[0], pt(47, 81)[1]], fill=(114, 214, 160, 255))
    # Right note head ellipse
    draw.ellipse([pt(48, 59)[0], pt(48, 59)[1], pt(68, 75)[0], pt(68, 75)[1]], fill=(114, 214, 160, 255))
    
    # Equalizer wave bars on the right
    draw.rounded_rectangle([pt(74, 45)[0], pt(74, 45)[1], pt(77.5, 63)[0], pt(77.5, 63)[1]], radius=int(2*scale), fill=(181, 242, 209, 255))
    draw.rounded_rectangle([pt(81, 39)[0], pt(81, 39)[1], pt(84.5, 68)[0], pt(84.5, 68)[1]], radius=int(2*scale), fill=(138, 232, 182, 255))
    draw.rounded_rectangle([pt(88, 48)[0], pt(88, 48)[1], pt(91.5, 60)[0], pt(91.5, 60)[1]], radius=int(2*scale), fill=(114, 214, 160, 255))
    
    return img

def create_phone_mockup(screen_img, target_screen_w=780, target_screen_h=1680, corner_radius=52, bezel_thickness=20):
    """Wraps screen in sleek modern smartphone bezel with camera hole, glass rim, and shadow"""
    # Resize screen to target
    resized_screen = screen_img.resize((target_screen_w, target_screen_h), Image.Resampling.LANCZOS)
    
    # Mask screen with rounded corners
    screen_mask = Image.new("L", (target_screen_w, target_screen_h), 0)
    mask_draw = ImageDraw.Draw(screen_mask)
    mask_draw.rounded_rectangle([0, 0, target_screen_w, target_screen_h], radius=corner_radius - 8, fill=255)
    
    # Apply mask
    rounded_screen = Image.new("RGBA", (target_screen_w, target_screen_h), (0, 0, 0, 0))
    rounded_screen.paste(resized_screen, (0, 0), screen_mask)
    
    # Punch hole camera at top center
    cam_w = 28
    cam_h = 28
    cam_x = (target_screen_w - cam_w) // 2
    cam_y = 20
    cam_draw = ImageDraw.Draw(rounded_screen)
    cam_draw.ellipse([cam_x, cam_y, cam_x + cam_w, cam_y + cam_h], fill=(8, 12, 10, 255), outline=(25, 35, 30, 255), width=2)
    cam_draw.ellipse([cam_x + 8, cam_y + 8, cam_x + 16, cam_y + 16], fill=(15, 25, 20, 255))
    
    # Outer device size
    dev_w = target_screen_w + bezel_thickness * 2
    dev_h = target_screen_h + bezel_thickness * 2
    
    # Device body
    device = Image.new("RGBA", (dev_w, dev_h), (0, 0, 0, 0))
    dev_draw = ImageDraw.Draw(device)
    
    # Outer bezel dark titanium
    dev_draw.rounded_rectangle([0, 0, dev_w, dev_h], radius=corner_radius, fill=(24, 33, 28, 255), outline=(50, 70, 60, 255), width=3)
    # Inner rim highlight
    dev_draw.rounded_rectangle([2, 2, dev_w - 2, dev_h - 2], radius=corner_radius - 2, outline=(70, 100, 85, 120), width=1)
    
    # Paste rounded screen inside bezel
    device.paste(rounded_screen, (bezel_thickness, bezel_thickness), rounded_screen)
    
    # Top speaker slit on bezel
    spk_w = 70
    spk_h = 5
    spk_x = (dev_w - spk_w) // 2
    spk_y = 8
    dev_draw.rounded_rectangle([spk_x, spk_y, spk_x + spk_w, spk_y + spk_h], radius=2, fill=(12, 16, 14, 255))
    
    # Create soft drop shadow + mint ambient glow around the device
    padding = 100
    comp_w = dev_w + padding * 2
    comp_h = dev_h + padding * 2
    
    comp = Image.new("RGBA", (comp_w, comp_h), (0, 0, 0, 0))
    
    # Shadow layer
    shadow = Image.new("RGBA", (comp_w, comp_h), (0, 0, 0, 0))
    sh_draw = ImageDraw.Draw(shadow)
    # Black ground drop shadow
    sh_draw.rounded_rectangle([padding + 10, padding + 25, padding + dev_w - 10, padding + dev_h + 25], radius=corner_radius + 5, fill=(0, 0, 0, 190))
    # Soft mint ambient aura
    sh_draw.rounded_rectangle([padding - 8, padding - 8, padding + dev_w + 8, padding + dev_h + 8], radius=corner_radius + 15, fill=(114, 214, 160, 50))
    
    shadow_blurred = shadow.filter(ImageFilter.GaussianBlur(35))
    comp.paste(shadow_blurred, (0, 0), shadow_blurred)
    comp.paste(device, (padding, padding), device)
    
    return comp

print("Logo and Mockup builder defined.")

def create_showcase_screenshot(screen_key, badge_text, title_text, subtitle_text, out_filename):
    """Creates a high-end 1080x2400 presentation screenshot"""
    canvas_w = 1080
    canvas_h = 2400
    
    # Base background: luxurious deep obsidian to dark emerald forest gradient
    bg = Image.new("RGBA", (canvas_w, canvas_h), (0, 0, 0, 255))
    bg_draw = ImageDraw.Draw(bg)
    
    # Vertical gradient
    for y in range(canvas_h):
        ratio = y / canvas_h
        # Top: #09100D (9, 16, 13) -> Mid: #101E17 (16, 30, 23) -> Bottom: #0C1511 (12, 21, 17)
        if ratio < 0.5:
            sub_r = ratio / 0.5
            r = int(9 * (1 - sub_r) + 16 * sub_r)
            g = int(16 * (1 - sub_r) + 32 * sub_r)
            b = int(13 * (1 - sub_r) + 24 * sub_r)
        else:
            sub_r = (ratio - 0.5) / 0.5
            r = int(16 * (1 - sub_r) + 11 * sub_r)
            g = int(32 * (1 - sub_r) + 20 * sub_r)
            b = int(24 * (1 - sub_r) + 16 * sub_r)
        bg_draw.line([(0, y), (canvas_w, y)], fill=(r, g, b, 255))
        
    # Ambient decorative glowing circles / audio rings in background
    rings = Image.new("RGBA", (canvas_w, canvas_h), (0, 0, 0, 0))
    r_draw = ImageDraw.Draw(rings)
    cx, cy = canvas_w // 2, int(canvas_h * 0.65)
    for rad in [450, 600, 780, 960]:
        r_draw.ellipse([cx - rad, cy - rad, cx + rad, cy + rad], outline=(114, 214, 160, 22), width=3)
    r_blurred = rings.filter(ImageFilter.GaussianBlur(12))
    bg.paste(r_blurred, (0, 0), r_blurred)
    
    # Top Typography Header Area
    header_draw = ImageDraw.Draw(bg)
    
    # 1. Category Tag Pill
    badge_font = get_font(FONT_BOLD_PATH, 28)
    bbox = badge_font.getbbox(badge_text)
    bw = bbox[2] - bbox[0]
    bh = bbox[3] - bbox[1]
    pill_w = bw + 56
    pill_h = bh + 26
    pill_x = (canvas_w - pill_w) // 2
    pill_y = 120
    
    # Subtle glowing pill background
    header_draw.rounded_rectangle([pill_x, pill_y, pill_x + pill_w, pill_y + pill_h], radius=pill_h // 2, fill=(24, 46, 36, 255), outline=(114, 214, 160, 180), width=2)
    # Mini icon dot
    header_draw.ellipse([pill_x + 18, pill_y + (pill_h - 10) // 2, pill_x + 28, pill_y + (pill_h + 10) // 2], fill=(114, 214, 160, 255))
    header_draw.text((pill_x + 38, pill_y + (pill_h - bh) // 2 - 4), badge_text, fill=(114, 214, 160, 255), font=badge_font)
    
    # 2. Main Headline
    title_font = get_font(FONT_BOLD_PATH, 72)
    t_bbox = title_font.getbbox(title_text)
    tw = t_bbox[2] - t_bbox[0]
    header_draw.text(((canvas_w - tw) // 2, 205), title_text, fill=(255, 255, 255, 255), font=title_font)
    
    # 3. Subtitle
    sub_font = get_font(FONT_REG_PATH, 34)
    s_bbox = sub_font.getbbox(subtitle_text)
    sw = s_bbox[2] - s_bbox[0]
    header_draw.text(((canvas_w - sw) // 2, 305), subtitle_text, fill=(175, 205, 190, 255), font=sub_font)
    
    # Prepare and place Phone Mockup
    cleaned_screen = clean_screenshot(RAW_SCREENSHOTS[screen_key])
    mockup = create_phone_mockup(cleaned_screen, target_screen_w=760, target_screen_h=1650, corner_radius=54, bezel_thickness=22)
    
    # Paste mockup onto canvas
    mock_x = (canvas_w - mockup.size[0]) // 2
    mock_y = 420
    bg.paste(mockup, (mock_x, mock_y), mockup)
    
    # Save outputs
    out_path_proj = os.path.join(OUTPUT_DIR, out_filename)
    out_path_brain = os.path.join(ARTIFACT_OUTPUT_DIR, out_filename)
    bg.save(out_path_proj, quality=95)
    bg.save(out_path_brain, quality=95)
    print(f"Generated screenshot: {out_filename}")

# Generate all 5 showcase screenshots
screens_meta = [
    ("player", "NOW PLAYING", "Pure Sound. 100% Offline.", "Lossless local playback with real-time waveform visualizer", "01_screenshot_now_playing.png"),
    ("songs", "MUSIC LIBRARY", "Smart Song Library", "Instant search, smart filters & lightning-fast indexing", "02_screenshot_all_songs.png"),
    ("albums", "ALBUMS & ARTISTS", "Explore by Albums", "Vibrant album art grids and smooth library navigation", "03_screenshot_albums.png"),
    ("folders", "STORAGE NAVIGATION", "Direct Folder Browser", "Play music directly from your device folders & downloads", "04_screenshot_folders.png"),
    ("playlists", "CUSTOM COLLECTIONS", "Personalized Playlists", "Create unlimited playlists and keep track of favorites", "05_screenshot_playlists.png")
]

for item in screens_meta:
    create_showcase_screenshot(*item)

print("All 5 showcase screenshots successfully generated.")


def create_feature_graphic(target_w=1024, target_h=500, out_filename="feature_graphic_1024x500.png"):
    """Creates Google Play Store Landscape Feature Graphic"""
    scale = target_w / 1024.0
    
    # 1. Load and prepare background
    bg_raw = Image.open(FEATURE_BG_V2).convert("RGBA")
    
    # Crop / resize to target_w x target_h
    # Original is 1376x768 (ratio 1.79), target is 1024x500 (ratio 2.048)
    target_ratio = target_w / target_h
    raw_ratio = bg_raw.size[0] / bg_raw.size[1]
    
    if raw_ratio > target_ratio:
        # crop width
        new_w = int(bg_raw.size[1] * target_ratio)
        offset_x = (bg_raw.size[0] - new_w) // 2
        bg_cropped = bg_raw.crop((offset_x, 0, offset_x + new_w, bg_raw.size[1]))
    else:
        # crop height
        new_h = int(bg_raw.size[0] / target_ratio)
        offset_y = (bg_raw.size[1] - new_h) // 2
        bg_cropped = bg_raw.crop((0, offset_y, bg_raw.size[0], offset_y + new_h))
        
    bg = bg_cropped.resize((target_w, target_h), Image.Resampling.LANCZOS)
    
    # Apply soft dark gradient overlay on left 50% to make text ultra-legible
    text_shade = Image.new("RGBA", (target_w, target_h), (0, 0, 0, 0))
    ts_draw = ImageDraw.Draw(text_shade)
    for x in range(int(target_w * 0.65)):
        frac = x / (target_w * 0.65)
        # alpha goes from 210 down to 0
        alpha = int(210 * (1 - frac)**1.2)
        ts_draw.line([(x, 0), (x, target_h)], fill=(10, 16, 13, alpha))
    bg.paste(text_shade, (0, 0), text_shade)
    
    # Left Side: Branding and Typography
    left_x = int(58 * scale)
    curr_y = int(52 * scale)
    
    # App Logo
    logo_sz = int(88 * scale)
    logo_img = render_melora_logo(size=logo_sz)
    bg.paste(logo_img, (left_x, curr_y), logo_img)
    
    # Brand Pill Badge next to logo
    draw = ImageDraw.Draw(bg)
    badge_font = get_font(FONT_BOLD_PATH, int(15 * scale))
    badge_text = "OFFLINE MUSIC PLAYER"
    b_bbox = badge_font.getbbox(badge_text)
    bw = b_bbox[2] - b_bbox[0]
    bh = b_bbox[3] - b_bbox[1]
    b_pill_w = bw + int(24 * scale)
    b_pill_h = bh + int(14 * scale)
    b_pill_x = left_x + logo_sz + int(20 * scale)
    b_pill_y = curr_y + int(10 * scale)
    draw.rounded_rectangle([b_pill_x, b_pill_y, b_pill_x + b_pill_w, b_pill_y + b_pill_h], radius=b_pill_h//2, fill=(22, 42, 33, 230), outline=(114, 214, 160, 200), width=int(1.5 * scale))
    draw.text((b_pill_x + int(12 * scale), b_pill_y + int(7 * scale)), badge_text, fill=(114, 214, 160, 255), font=badge_font)
    
    # App Title "Melora"
    title_font = get_font(FONT_BOLD_PATH, int(42 * scale))
    draw.text((b_pill_x, b_pill_y + b_pill_h + int(6 * scale)), "Melora", fill=(255, 255, 255, 255), font=title_font)
    
    # Tagline
    curr_y = curr_y + logo_sz + int(24 * scale)
    headline_font = get_font(FONT_BOLD_PATH, int(34 * scale))
    draw.text((left_x, curr_y), "Pure Sound. Zero Noise.", fill=(255, 255, 255, 255), font=headline_font)
    
    curr_y += int(46 * scale)
    sub_font = get_font(FONT_SEMIBOLD_PATH, int(18 * scale))
    draw.text((left_x, curr_y), "High-fidelity offline audio playback with no ads or tracking.", fill=(160, 195, 178, 255), font=sub_font)
    
    # Feature Bullet Chips
    curr_y += int(42 * scale)
    features = [
        ("⚡", "100% Offline Playback"),
        ("🎚️", "Studio Waveform Visualizer"),
        ("📁", "Folders, Albums & Playlists"),
        ("🔋", "Battery-Friendly Jetpack Compose")
    ]
    
    chip_font = get_font(FONT_SEMIBOLD_PATH, int(14 * scale))
    for icon, text in features:
        c_bbox = chip_font.getbbox(f"{icon}  {text}")
        cw = c_bbox[2] - c_bbox[0]
        ch = c_bbox[3] - c_bbox[1]
        chip_w = cw + int(24 * scale)
        chip_h = ch + int(14 * scale)
        
        draw.rounded_rectangle([left_x, curr_y, left_x + chip_w, curr_y + chip_h], radius=int(6 * scale), fill=(18, 30, 24, 210), outline=(60, 95, 78, 180), width=1)
        draw.text((left_x + int(12 * scale), curr_y + int(6 * scale)), f"{icon}  {text}", fill=(210, 240, 225, 255), font=chip_font)
        curr_y += chip_h + int(10 * scale)
        
    # Right Side: Dual Floating Phone Mockups
    # Phone 1 (Back, slightly offset and rotated -4 deg): All Songs screen
    screen_songs = clean_screenshot(RAW_SCREENSHOTS["songs"])
    mock_songs = create_phone_mockup(screen_songs, target_screen_w=int(260 * scale), target_screen_h=int(575 * scale), corner_radius=int(24 * scale), bezel_thickness=int(8 * scale))
    mock_songs_rot = mock_songs.rotate(4, resample=Image.Resampling.BICUBIC, expand=True)
    
    # Phone 2 (Front hero, rotated -3 deg): Now Playing screen
    screen_player = clean_screenshot(RAW_SCREENSHOTS["player"])
    mock_player = create_phone_mockup(screen_player, target_screen_w=int(290 * scale), target_screen_h=int(640 * scale), corner_radius=int(26 * scale), bezel_thickness=int(9 * scale))
    mock_player_rot = mock_player.rotate(-3, resample=Image.Resampling.BICUBIC, expand=True)
    
    # Placement
    # Songs mock in back
    songs_x = int(target_w * 0.54)
    songs_y = int(target_h * 0.04)
    bg.paste(mock_songs_rot, (songs_x, songs_y), mock_songs_rot)
    
    # Player mock in front
    player_x = int(target_w * 0.70)
    player_y = int(target_h * 0.08)
    bg.paste(mock_player_rot, (player_x, player_y), mock_player_rot)
    
    # Save outputs
    out_path_proj = os.path.join(OUTPUT_DIR, out_filename)
    out_path_brain = os.path.join(ARTIFACT_OUTPUT_DIR, out_filename)
    bg.save(out_path_proj, quality=95)
    bg.save(out_path_brain, quality=95)
    print(f"Generated feature graphic: {out_filename} ({target_w}x{target_h})")

# Generate standard 1024x500 and high-res 2048x1000
create_feature_graphic(1024, 500, "feature_graphic_1024x500.png")
create_feature_graphic(2048, 1000, "feature_graphic_2048x1000_hd.png")


def create_client_presentation_overview(out_filename="melora_client_presentation_overview.png"):
    """Creates a 3200x1800 4K Executive Showcase Slide containing all 5 mockups"""
    canvas_w = 3200
    canvas_h = 1800
    
    # Background: deep dark forest obsidian gradient
    bg = Image.new("RGBA", (canvas_w, canvas_h), (0, 0, 0, 255))
    draw = ImageDraw.Draw(bg)
    
    for y in range(canvas_h):
        ratio = y / canvas_h
        r = int(9 * (1 - ratio) + 14 * ratio)
        g = int(16 * (1 - ratio) + 26 * ratio)
        b = int(13 * (1 - ratio) + 20 * ratio)
        draw.line([(0, y), (canvas_w, y)], fill=(r, g, b, 255))
        
    # Ambient grid / glow lines
    grid = Image.new("RGBA", (canvas_w, canvas_h), (0, 0, 0, 0))
    g_draw = ImageDraw.Draw(grid)
    for rad in [600, 900, 1300, 1700]:
        g_draw.ellipse([canvas_w//2 - rad, int(canvas_h*0.7) - rad, canvas_w//2 + rad, int(canvas_h*0.7) + rad], outline=(114, 214, 160, 25), width=2)
    g_blurred = grid.filter(ImageFilter.GaussianBlur(15))
    bg.paste(g_blurred, (0, 0), g_blurred)
    
    # Executive Top Header
    header_draw = ImageDraw.Draw(bg)
    
    # Logo
    logo = render_melora_logo(size=140)
    bg.paste(logo, (120, 80), logo)
    
    # Title & Subtitle
    title_font = get_font(FONT_BOLD_PATH, 58)
    header_draw.text((280, 85), "Melora — Android Offline Music Player", fill=(255, 255, 255, 255), font=title_font)
    
    sub_font = get_font(FONT_REG_PATH, 28)
    header_draw.text((280, 160), "Application UI Design Showcase & Screen Flows • Prepared for Client Presentation", fill=(160, 200, 180, 255), font=sub_font)
    
    # Badges on top right
    tags = ["Jetpack Compose Material 3", "Media3 ExoPlayer", "Deep Forest & Mint Theme", "100% Private Offline"]
    tag_font = get_font(FONT_BOLD_PATH, 22)
    cur_tag_x = canvas_w - 120
    for t in reversed(tags):
        t_box = tag_font.getbbox(t)
        tw = t_box[2] - t_box[0]
        th = t_box[3] - t_box[1]
        pw = tw + 36
        ph = th + 20
        cur_tag_x -= pw
        header_draw.rounded_rectangle([cur_tag_x, 110, cur_tag_x + pw, 110 + ph], radius=ph//2, fill=(20, 38, 29, 220), outline=(114, 214, 160, 180), width=2)
        header_draw.text((cur_tag_x + 18, 110 + 9), t, fill=(114, 214, 160, 255), font=tag_font)
        cur_tag_x -= 16
        
    # Divider line
    header_draw.line([(120, 260), (canvas_w - 120, 260)], fill=(45, 75, 60, 180), width=2)
    
    # Display all 5 screens
    screens = [
        ("player", "1. Now Playing", "Dynamic Audio Waveform & Player Controls"),
        ("songs", "2. Songs Library", "Fast Search, Filter Pills & 250+ Tracks"),
        ("albums", "3. Albums View", "Album Art Grids & Quick Categorization"),
        ("folders", "4. Folders Browser", "Storage Directories & Direct Playback"),
        ("playlists", "5. Custom Playlists", "User Curated Mixes & Favorites")
    ]
    
    # Calculate spacing
    n_screens = 5
    card_w = 540
    card_h = 1350
    total_w = canvas_w - 240
    step_x = total_w / n_screens
    
    lbl_title_font = get_font(FONT_BOLD_PATH, 28)
    lbl_sub_font = get_font(FONT_REG_PATH, 20)
    
    for idx, (s_key, s_title, s_desc) in enumerate(screens):
        x_center = 120 + int((idx + 0.5) * step_x)
        
        # Screen Mockup (target screen ~ 440 x 980)
        clean_img = clean_screenshot(RAW_SCREENSHOTS[s_key])
        mockup = create_phone_mockup(clean_img, target_screen_w=430, target_screen_h=950, corner_radius=32, bezel_thickness=12)
        
        m_x = x_center - mockup.size[0] // 2
        m_y = 300
        bg.paste(mockup, (m_x, m_y), mockup)
        
        # Bottom label box
        box_y = 1480
        box_w = 480
        box_h = 140
        box_x = x_center - box_w // 2
        
        header_draw.rounded_rectangle([box_x, box_y, box_x + box_w, box_y + box_h], radius=16, fill=(18, 30, 24, 230), outline=(50, 80, 65, 200), width=1)
        
        # Screen title
        t_box = lbl_title_font.getbbox(s_title)
        tw = t_box[2] - t_box[0]
        header_draw.text((x_center - tw // 2, box_y + 24), s_title, fill=(255, 255, 255, 255), font=lbl_title_font)
        
        # Screen desc
        d_box = lbl_sub_font.getbbox(s_desc)
        dw = d_box[2] - d_box[0]
        header_draw.text((x_center - dw // 2, box_y + 76), s_desc, fill=(160, 205, 185, 255), font=lbl_sub_font)
        
    out_path_proj = os.path.join(OUTPUT_DIR, out_filename)
    out_path_brain = os.path.join(ARTIFACT_OUTPUT_DIR, out_filename)
    bg.save(out_path_proj, quality=95)
    bg.save(out_path_brain, quality=95)
    print(f"Generated client presentation overview: {out_filename}")

create_client_presentation_overview()
