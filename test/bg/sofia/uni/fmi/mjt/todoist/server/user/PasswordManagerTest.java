package bg.sofia.uni.fmi.mjt.todoist.server.user;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception.PasswordFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordManagerTest {

    @Test
    void testValidate_PasswordIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PasswordManager.validate(null),
            "Should throw IllegalArgumentException on null password");
    }

    @Test
    void testValidate_PasswordIsTooShort_ShouldThrowPasswordFormatException() {
        String shortPassword = "Ab1"; // 3 символа
        assertThrows(PasswordFormatException.class, () -> PasswordManager.validate(shortPassword),
            "Should throw PasswordFormatException on password shorter than 4 characters");
    }

    @Test
    void testValidate_PasswordIsTooLong_ShouldThrowPasswordFormatException() {
        String longPassword = "Ab123456789012345678901234567"; // 28 символа
        assertThrows(PasswordFormatException.class, () -> PasswordManager.validate(longPassword),
            "Should throw PasswordFormatException on password longer than 25 characters");
    }

    @Test
    void testValidate_NoUpperCase_ShouldThrowPasswordFormatException() {
        String noUpperCase = "abc123";
        assertThrows(PasswordFormatException.class, () -> PasswordManager.validate(noUpperCase),
            "Should throw PasswordFormatException on non-capitalized password");
    }

    @Test
    void testValidate_NoLowerCase_ShouldThrowPasswordFormatException() {
        String noLowerCase = "ABC123";
        assertThrows(PasswordFormatException.class, () -> PasswordManager.validate(noLowerCase),
            "Should throw PasswordFormatException on password without lowercase letters");
    }

    @Test
    void testValidate_NoDigit_ShouldThrowPasswordFormatException() {
        String noDigit = "Abcdef";
        assertThrows(PasswordFormatException.class, () -> PasswordManager.validate(noDigit),
            "Should throw Password FormatException for numeric password");
    }

    @Test
    void testValidate_ValidPassword_ShouldNotThrowException() {
        String validPassword = "Abc1";
        assertDoesNotThrow(() -> PasswordManager.validate(validPassword),
            "Should not throw an exception on valid password");
    }

    @Test
    void testHashPassword_Null_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PasswordManager.hashPassword(null),
            "We expect an IllegalArgumentException when trying to hash a null password.");
    }

    @Test
    void testHashPassword_ValidPassword_ShouldReturnNonNullHash() {
        String password = "Abc1";
        String hash = PasswordManager.hashPassword(password);

        assertNotNull(hash, "The hash must not be null.");
        assertFalse(hash.isEmpty(), "The hash must not be an empty string.");
    }

    @Test
    void testHashPassword_Consistency_SamePasswordShouldReturnSameHash() {
        String password = "Abc1";

        String hash1 = PasswordManager.hashPassword(password);
        String hash2 = PasswordManager.hashPassword(password);

        assertEquals(hash1, hash2, "The same password must have the same hash");
    }

    @Test
    void testHashPassword_DifferentPasswordsShouldReturnDifferentHashes() {
        String password1 = "Abc1";
        String password2 = "Abc2";

        String hash1 = PasswordManager.hashPassword(password1);
        String hash2 = PasswordManager.hashPassword(password2);

        assertNotEquals(hash1, hash2, "Different passwords should have different hashes");
    }

    @Test
    void testHashPassword_LengthShouldBe64HexCharacters() {
        String password = "Abc1";
        String hash = PasswordManager.hashPassword(password);

        assertEquals(64, hash.length(), "The SHA-256 password hash must be 64 characters in hex format");
    }

}
