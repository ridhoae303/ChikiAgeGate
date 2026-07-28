<p align="center">
  <img src="./banner/chiki-agegate-banner.jpg" alt="Chiki AgeGate banner" width="100%">
</p>

<h1 align="center">Chiki AgeGate</h1>

<p align="center">
  A clean Android age gate built with Java, JNI, and a native 18+ date check.
</p>

<p align="center">
  <a href="./">
    <img src="https://img.shields.io/badge/Source-Repository-181717?style=for-the-badge&logo=github&logoColor=white" alt="Source Repository">
  </a>
  <a href="https://github.com/ridhoae303">
    <img src="https://img.shields.io/badge/GitHub-ridhoae303-6E35FF?style=for-the-badge&logo=github&logoColor=white" alt="ridhoae303 on GitHub">
  </a>
  <a href="./LICENSE">
    <img src="https://img.shields.io/badge/License-Chiki%20AgeGate-26A85A?style=for-the-badge" alt="Chiki AgeGate License">
  </a>
</p>

---

## About

Chiki AgeGate is a fullscreen age-verification gate for Android apps. It asks the user whether they are a minor or 18+, then checks the selected birth date through native C++ code before allowing the app to continue.

The whole interface is built in Java, so there is no XML layout maze and no giant UI dependency stack. The native side handles the date rules, prompt strings, and the local verification token.

It is an age gate, not an identity check. Keep that difference in mind before treating it like legal compliance magic.

## Preview

<p align="center">
  <strong>Alright, check this out — here's Chiki AgeGate in action.</strong>
</p>

<table>
  <tr>
    <td width="50%" align="center">
      <img
        src="https://github.com/user-attachments/assets/8d65633e-bb80-47d5-b758-84230bdba56e"
        alt="Chiki AgeGate preview 1"
        width="100%"
      />
    </td>
    <td width="50%" align="center">
      <img
        src="https://github.com/user-attachments/assets/cf7a254d-b007-4fda-bea0-2f16d3389223"
        alt="Chiki AgeGate preview 2"
        width="100%"
      />
    </td>
  </tr>
</table>

## What It Does

- Shows a non-cancelable fullscreen verification dialog.
- Offers clear **minor** and **18+** choices.
- Closes the app when the user selects the minor option.
- Uses a custom year, month, and day picker.
- Checks leap years, valid calendar dates, future dates, and the user's current age.
- Requires the user to be at least 18 years old.
- Keeps the selected birth date in memory only; the actual date is not saved to preferences.
- Stores only a local verified-state token after a successful check.
- Obfuscates native strings with `EasyObfuse`.
- Uses random native prompt text so the opening message is not always identical.
- Supports an optional second birth-date confirmation flow.
- Includes smooth entry, exit, picker, logo, and invalid-choice animations.
- Uses a custom logo and font from Android assets, with built-in fallbacks.
- Requires two back presses within a short window before leaving the main gate.
- Styles `NumberPicker` across different Android implementations without XML themes.

## Flow

```text
App opens
   ↓
Was this installation already verified?
   ├─ Yes → continue
   └─ No  → show Chiki AgeGate
                 ↓
          User says "minor"?
           ├─ Yes → close the app
           └─ No  → select birth date
                          ↓
                   Native 18+ check
                    ├─ Fail → show feedback and stay locked
                    └─ Pass → save verified token and continue
```

## Project Layout

Use this layout unless your build setup already has its own structure:

```text
app/
└── src/
    └── main/
        ├── java/
        │   └── com/chiki/makigate/
        │       └── ChikiVerification.java
        ├── cpp/
        │   ├── ChikiVerification.cpp
        │   └── EasyObfuse.h
        └── assets/
            └── chiki/
                └── age_verify/
                    ├── logo.png
                    └── font.ttf
```

The banner used by this README belongs here:

```text
banner/chiki-agegate-banner.jpg
```

## Requirements

- Android project with Java support
- Android NDK and JNI
- C++11 or newer
- CMake, ndk-build, or another compatible native build setup
- A generated native library named `libmakigate.so`
- `ChikiVerification.java`
- `ChikiVerification.cpp`
- `EasyObfuse.h`

The Java class loads the native library with:

```java
System.loadLibrary("makigate");
```

Your native target therefore needs to be named `makigate`.

## Calling Chiki AgeGate

Call the verifier from an `Activity`:

```java
import com.chiki.makigate.ChikiVerification;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ChikiVerification.verify(this);
}
```

The method signature is:

```java
public static void verify(Activity activity);
```

### Smali

The matching Smali call is:

```smali
invoke-static {p0}, Lcom/chiki/makigate/ChikiVerification;->verify(Landroid/app/Activity;)V
```

There is no `move-result` here because `verify(Activity)` returns `void`. This example also assumes `p0` contains the current `Activity`, which is normally true inside a non-static Activity method such as `onCreate`.

If the Activity is stored in another register, replace `p0` with that register.

## Native Bridge

The current Java class declares these native methods:

```java
public static native String nRkVxQpLm();
public static native boolean zMpwQbLrS(int year, int month, int day);
public static native boolean qTrLxVmNp(int year);
public static native String pXvNqRtKm(String plain);
public static native String vLmQrXpNz(String encrypted);
```

Their JNI exports are tied to this exact class path:

```text
com.chiki.makigate.ChikiVerification
```

Changing the package, class name, or native method names means changing the matching JNI symbols in `ChikiVerification.cpp` too. JNI is very literal; one wrong character and it simply refuses to cooperate.

For release builds, keep the bridge class from being renamed by R8/ProGuard:

```proguard
-keep class com.chiki.makigate.ChikiVerification { *; }
```

## Assets

Chiki AgeGate looks for these files:

```text
assets/chiki/age_verify/logo.png
assets/chiki/age_verify/font.ttf
```

`logo.png` is center-cropped into a circular image. When the logo is missing, the UI falls back to a generated purple circle.

`font.ttf` is used for the creator title. When it cannot be loaded, Android's default bold typeface is used instead.

Tapping the logo currently opens the hardcoded community link inside `openLogoLink()`. Replace that URL with your own page, group, or project link before publishing a fork.

## Current Defaults

| Setting | Current value |
|---|---:|
| Minimum age | `18` |
| Earliest accepted birth year | `1980` |
| Native library | `makigate` |
| Preference file | `maki_gate_enc` |
| Preference key | `vrf` |
| Double-back exit window | `1800 ms` |
| Random prompt count | `7` |
| Second date confirmation | Disabled |

The earliest birth year exists in both Java and C++:

```java
private static final int MIN_BIRTH_YEAR = 1980;
```

```cpp
static const int kMinimumBirthYear = 1980;
```

Change both values together. Updating only one side will create inconsistent behavior between the picker and the native validator.

## Optional Double Confirmation

The Java side already contains a second-step flow that asks the user to enter the same birth date again. Right now, it is disabled because the native method below always returns `JNI_FALSE`:

```cpp
Java_com_chiki_makigate_ChikiVerification_qTrLxVmNp(...)
```

That means successful users normally verify once and continue. The second picker can be enabled later by changing the native policy behind that method.

## Local Verification State

After a successful check, Chiki AgeGate stores an obfuscated `verified` token in private `SharedPreferences`.

The actual birth date is not written to preferences. It is used for the current verification flow and then discarded.

The stored token is wrapped with a lightweight native XOR-and-Base64 routine. That keeps it out of plain text, but it is not strong cryptography and should not be advertised as tamper-proof storage.

## Privacy and Security Notes

Chiki AgeGate performs its birth-date check locally. It does not upload the entered date or require a network request for verification. The only external action in the supplied code is the creator-logo link, which opens after the user taps the logo.

This project is a client-side age gate. A determined person can lie about a birth date, reset local app data, patch the APK, or modify the verification flow. Use it as a clear access barrier and user-facing confirmation step—not as proof of identity, a government-ID check, or a guarantee of compliance with every law or platform policy.

For stronger protection, pair it with whatever makes sense for your app:

- Server-side account rules
- Platform parental controls
- Region-aware compliance logic
- Code obfuscation and integrity checks
- Clear privacy and content notices
- A proper legal review for the countries you actually serve

## Behavior Notes

- Pressing **Nah, I'm a minor** clears the verification flag and closes the process.
- A failed or underage date keeps the picker open and plays a short shake animation.
- Pressing Back on the main gate once shows a warning; pressing it again within `1800 ms` exits.
- Pressing Back inside the date picker returns to the main gate.
- Once the verified token is accepted, later calls to `verify()` return immediately.
- Clearing the app's local data resets the verification state.

## Customization

The easiest parts to change are:

- Banner, logo, and font
- Creator title and subtitle
- Button labels
- Dialog colors and gradients
- Random native prompts
- Minimum age
- Earliest accepted birth year
- Community/project link
- Preference file and key
- Exit timing
- Second confirmation policy

Keep Java and C++ constants synchronized whenever the same rule exists on both sides.

## License

Chiki AgeGate uses the **Chiki AgeGate License**. Read the full terms in [`LICENSE`](./LICENSE).

This is a source-available license, not an OSI-approved open-source license. You may study, use, and modify the project under its terms, but you may not erase the original credit, pretend the original work is yours, or redistribute a renamed copy without proper attribution.

## Credits

Created by **ridhoae303**.

© 2026 ridhoae303. All rights reserved except where permission is granted in the license.