// Created by ridhoae303
// https://github.com/ridhoae303

#include <jni.h>
#include <string>
#include <ctime>
#include <cstdlib>
#include <vector>
#include <EasyObfuse.h>

static const int kPromptCount = 7;
static const int kMinimumBirthYear = 1980;
static const int kMinimumAge = 18;

static void rQzLmNpVx() {
    static bool seeded = false;

    if (seeded) {
        return;
    }

    seeded = true;
    srand((unsigned int) time(nullptr));
}

static bool mVxRqLpNz(const std::string& value) {
    for (size_t i = 0; i < value.size(); ++i) {
        unsigned char c = (unsigned char) value[i];

        if (c < 32 || c > 126) {
            return false;
        }
    }

    return true;
}

static bool xPqLmVzRt(int year) {
    if (year % 400 == 0) {
        return true;
    }

    if (year % 100 == 0) {
        return false;
    }

    return year % 4 == 0;
}

static int lZrVqMpNx(int year, int month) {
    switch (month) {
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12:
            return 31;

        case 4:
        case 6:
        case 9:
        case 11:
            return 30;

        case 2:
            return xPqLmVzRt(year) ? 29 : 28;

        default:
            return 0;
    }
}

static bool tNmQxVrLp(
        int birthYear,
        int birthMonth,
        int birthDay,
        int currentYear,
        int currentMonth,
        int currentDay
) {
    if (birthYear > currentYear) {
        return true;
    }

    if (birthYear == currentYear && birthMonth > currentMonth) {
        return true;
    }

    return birthYear == currentYear
            && birthMonth == currentMonth
            && birthDay > currentDay;
}

static int vPrLxQmNz(
        int birthYear,
        int birthMonth,
        int birthDay,
        int currentYear,
        int currentMonth,
        int currentDay
) {
    int age = currentYear - birthYear;

    if (currentMonth < birthMonth) {
        age--;
    } else if (currentMonth == birthMonth && currentDay < birthDay) {
        age--;
    }

    return age;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_chiki_makigate_ChikiVerification_nRkVxQpLm(JNIEnv* env, jclass) {
    rQzLmNpVx();

    int index = rand() % kPromptCount;

    switch (index) {
        case 0:
            return env->NewStringUTF(
                    OBFUSCATE("Please verify your age before continuing.")
            );

        case 1:
            return env->NewStringUTF(
                    OBFUSCATE("This area requires age verification. Tap 18+ if you are eligible.")
            );

        case 2:
            return env->NewStringUTF(
                    OBFUSCATE("Secure access check required. Confirm that you are 18 or older.")
            );

        case 3:
            return env->NewStringUTF(
                    OBFUSCATE("Before entering, please complete the age verification.")
            );

        case 4:
            return env->NewStringUTF(
                    OBFUSCATE("Age verification is required to continue.")
            );

        case 5:
            return env->NewStringUTF(
                    OBFUSCATE("Confirm your age to unlock access.")
            );

        default:
            return env->NewStringUTF(
                    OBFUSCATE("Verify that you are 18+ to continue.")
            );
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_chiki_makigate_ChikiVerification_zMpwQbLrS(
        JNIEnv*,
        jclass,
        jint year,
        jint month,
        jint day
) {
    time_t now = time(nullptr);
    tm* currentTime = localtime(&now);

    if (currentTime == nullptr) {
        return JNI_FALSE;
    }

    int currentYear = currentTime->tm_year + 1900;
    int currentMonth = currentTime->tm_mon + 1;
    int currentDay = currentTime->tm_mday;

    int birthYear = (int) year;
    int birthMonth = ((int) month) + 1;
    int birthDay = (int) day;

    if (birthYear < kMinimumBirthYear) {
        return JNI_FALSE;
    }

    if (birthMonth < 1 || birthMonth > 12) {
        return JNI_FALSE;
    }

    int maxDay = lZrVqMpNx(birthYear, birthMonth);

    if (birthDay < 1 || birthDay > maxDay) {
        return JNI_FALSE;
    }

    if (tNmQxVrLp(
            birthYear,
            birthMonth,
            birthDay,
            currentYear,
            currentMonth,
            currentDay
    )) {
        return JNI_FALSE;
    }

    int age = vPrLxQmNz(
            birthYear,
            birthMonth,
            birthDay,
            currentYear,
            currentMonth,
            currentDay
    );

    return age >= kMinimumAge ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_chiki_makigate_ChikiVerification_qTrLxVmNp(
        JNIEnv*,
        jclass,
        jint
) {
    return JNI_FALSE;
}

static std::string kZxQpLmRv(const std::string& data) {
    static const std::string aZpQxLmRv = OBFUSCATE("m0dD1ngG4t3K3y!");

    std::string result = data;

    if (aZpQxLmRv.empty()) {
        return result;
    }

    for (size_t i = 0; i < result.size(); ++i) {
        result[i] ^= aZpQxLmRv[i % aZpQxLmRv.size()];
    }

    return result;
}

static std::string yMqVxLpNr(const std::string& input) {
    static const std::string bTrVxQmLp =
            OBFUSCATE("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");

    std::string output;

    int value = 0;
    int bits = -6;

    for (unsigned char c : input) {
        value = (value << 8) + c;
        bits += 8;

        while (bits >= 0) {
            output.push_back(bTrVxQmLp[(value >> bits) & 0x3F]);
            bits -= 6;
        }
    }

    if (bits > -6) {
        output.push_back(
                bTrVxQmLp[((value << 8) >> (bits + 8)) & 0x3F]
        );
    }

    while (output.size() % 4) {
        output.push_back('=');
    }

    return output;
}

static std::string cRpLzVqMx(const std::string& input) {
    static const std::string hLmQvZxRp =
            OBFUSCATE("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");

    std::string output;
    std::vector<int> table(256, -1);

    for (int i = 0; i < 64; i++) {
        table[(unsigned char) hLmQvZxRp[i]] = i;
    }

    int value = 0;
    int bits = -8;

    for (unsigned char c : input) {
        if (c == '=') {
            break;
        }

        if (table[c] == -1) {
            break;
        }

        value = (value << 6) + table[c];
        bits += 6;

        if (bits >= 0) {
            output.push_back(
                    char((value >> bits) & 0xFF)
            );

            bits -= 8;
        }
    }

    return output;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_chiki_makigate_ChikiVerification_pXvNqRtKm(
        JNIEnv* env,
        jclass,
        jstring plain
) {
    if (plain == nullptr) {
        return env->NewStringUTF("");
    }

    const char* utf = env->GetStringUTFChars(plain, nullptr);

    if (utf == nullptr) {
        return env->NewStringUTF("");
    }

    std::string input(utf);

    env->ReleaseStringUTFChars(plain, utf);

    std::string encrypted = kZxQpLmRv(input);
    std::string encoded = yMqVxLpNr(encrypted);

    return env->NewStringUTF(encoded.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_chiki_makigate_ChikiVerification_vLmQrXpNz(
        JNIEnv* env,
        jclass,
        jstring encrypted
) {
    if (encrypted == nullptr) {
        return env->NewStringUTF("");
    }

    const char* utf = env->GetStringUTFChars(encrypted, nullptr);

    if (utf == nullptr) {
        return env->NewStringUTF("");
    }

    std::string input(utf);

    env->ReleaseStringUTFChars(encrypted, utf);

    std::string decoded = cRpLzVqMx(input);
    std::string original = kZxQpLmRv(decoded);

    if (!mVxRqLpNz(original)) {
        return env->NewStringUTF("");
    }

    return env->NewStringUTF(original.c_str());
}